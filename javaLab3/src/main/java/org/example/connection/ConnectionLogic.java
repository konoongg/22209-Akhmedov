package org.example.connection;

import org.example.connection.peer.*;
import org.example.connection.states.*;
import org.example.exceptions.ReadException;
import org.example.exceptions.WriteException;
import org.example.torrent.TorrentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;

public class ConnectionLogic {
    private static final Logger log = LoggerFactory.getLogger(ConnectionLogic.class);
    private HandShake hsManager;
    public ConnectionLogic(TorrentClient torrent){
        hsManager = new HandShake(torrent);
    }

    private void ReadHandshake(SelectionKey key, Selector selector) throws ReadException, WriteException {
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        con.BufferConnect(68);
        int bytesRead = 0;
        try{
            bytesRead = channel.read(con.GetBuffer());
            log.trace(peer.GetHost() + ":" + peer.GetPort() + "read handshake: read byte: " + bytesRead);
        }
        catch (IOException e){
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            throw new ReadException("ERROR cant read handshake: " +  peer.GetHost() + ":" + peer.GetPort() +  " " + e.getMessage());
        }
        if(bytesRead == -1){
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() +  " can't read: bytesRead -1");
        }
        con.ReadByte(bytesRead);
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " con ReadByte " + bytesRead);
        if(con.GetNeededWrite() != 0){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " Wait part byte: " + con.GetNeededWrite());
        }
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " get all byte ");
        con.GetBuffer().flip();
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " buffer fliped ");
        log.trace("buffer size real: " + con.GetBuffer().remaining());
        if(con.GetBuffer().remaining() < 68){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " buffer size incorrect: " + con.GetNeededWrite());
        }
        byte[] data = new byte[68];
        con.GetBuffer().get(data);
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " con getBuffer " + bytesRead);
        if(con.GetStatus() == ConnectionStatusE.TRY_HANDSHAKE){
            if(hsManager.CheckHandShacke(data) == HandShackeStatusE.SUCCESSFUL){
                con.BufferDisconnect();
                con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
                log.debug(peer.GetHost() + ":" + peer.GetPort() + " HANDSHAKE SUCCESSFULE");
            }
            else{
                peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
                log.warn("can't handShake: " + peer.GetHost() + ":" + peer.GetPort());
            }
        }
        else if(con.GetStatus() == ConnectionStatusE.SERVER_HANDSHAKE){
            ByteBuffer buffer = ByteBuffer.wrap(data);
            try {
                channel.write(buffer);
                con.ChangeStatus(ConnectionStatusE.SEND_BITSET);
                con.BufferDisconnect();
            } catch (IOException e) {
                throw new WriteException("server can't send handshake");
            }
        }
    }

    private void HandleFinishRead(Peer peer){
        PeerDataContoller con = peer.GetPeerDataCon();
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " handle finish read" );
        ConnectionStatusE status = con.GetStatus();
        try{
            con.SaveBuffer();
        }
        catch(ReadException e){
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            return;
        }
        con.BufferDisconnect();
        if(status == ConnectionStatusE.LISTENER_LENGTH){
            con.ChangeStatus(ConnectionStatusE.LISTENER_ID);
        }
        else if(status == ConnectionStatusE.LISTENER_ID){
            con.ChangeStatus(ConnectionStatusE.LISTENER_DATA);
        }
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " handle finish safe" );
    }

    private void ReadData(SocketChannel channel, Peer peer) throws ReadException {
        int bytesRead;
        PeerDataContoller con = peer.GetPeerDataCon();
        ByteBuffer buffer  = con.GetBuffer();
        try{
            bytesRead = channel.read(buffer);
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " read byte: " + bytesRead);
        }
        catch (IOException e){
            throw new ReadException("ERROR cant data-length: " + peer.GetHost() + ":" + peer.GetPort() +  " " + e.getMessage());
        }
        if(bytesRead == -1){
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() +  " can't read, no data");
        }
        else if(bytesRead == 0){
            peer.GetPeerDataCon().UnSuccessfulReading();
        }
        else{
            peer.GetPeerDataCon().SuccessfulReading();
        }
        con.ReadByte(bytesRead);
        if(con.GetNeededWrite() != 0){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " Wait part byte: " + con.GetNeededWrite());
        }
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " get all byte ");
        HandleFinishRead(peer);
    }

    private void DefineId(Peer peer) throws ReadException {
        PeerDataContoller con = peer.GetPeerDataCon();
        int id = con.GetMessage().GetId();
        if(id == MessageIdE.BITFIELD.getValue()){
            con.ChangeStatus(ConnectionStatusE.INTERESTED);
            con.SetParts();
          log.debug(peer.GetHost() + ":" + peer.GetPort() + "  BITFIELD");
        }
        else if(id == MessageIdE.UNCHOKE.getValue() && peer.GetPeerStatus() == PeerStatusE.CHOKE){
            peer.Unchoke();
            if(con.GetStatus() != ConnectionStatusE.LOAD_DATA){
                con.ChangeStatus(ConnectionStatusE.WAIT_TASK);
                peer.GetTask().Ready(MessageIdE.UNCHOKE);
            }
          log.debug(peer.GetHost() + ":" + peer.GetPort() + "  UNCHOKE");
        }
        else if(id == MessageIdE.CHOKE.getValue()){
            peer.Choke();
            con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            log.debug(peer.GetHost() + ":" + peer.GetPort() + "  CHOKE");
        }
        else if(id == MessageIdE.HAVE.getValue()){
            con.ApplyHave();
            if(con.GetStatus() != ConnectionStatusE.LOAD_DATA){
                con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            }
            log.debug(peer.GetHost() + ":" + peer.GetPort() + "  HAVE");
        }
        else if(id == MessageIdE.PIECE.getValue()){
            con.ChangeStatus(ConnectionStatusE.LOAD_DATA);
            peer.GetTask().Ready(MessageIdE.PIECE);
            log.debug(peer.GetHost() + ":" + peer.GetPort() + "  PIECE");
        }
        else if(id == MessageIdE.REQUEST.getValue()){
            peer.SetServerTask(con.GetMessage().GetMes());
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.LOAD_SERVER_DATA);
        }
        else{
            con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " unexpected id " + id);
        }
    }

    private void ListenPeer(SelectionKey key) throws ReadException {
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        log.debug(peer.GetHost() + ":" + peer.GetPort() + " LISTEN_PEER");
        if(con.GetStatus() == ConnectionStatusE.LISTENER_LENGTH){
            con.BufferConnect(4);
            con.BufferConnect(4);
            ReadData(channel, peer);
        }
        if(con.GetStatus() == ConnectionStatusE.LISTENER_ID){
            con.BufferConnect(1);
            ReadData(channel, peer);
        }
        if(con.GetStatus() == ConnectionStatusE.LISTENER_DATA){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " start listened data");
            PeerMessage message = con.GetMessage();
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " try to connect: " + (message.GetLength() - 1));
            con.BufferConnect(message.GetLength() - 1);
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " lenght: " +  message.GetLength() + " ID: " + message.GetId());
            ReadData(channel, peer);
        }
        DefineId(peer);
    }

    public void DefineRead(SelectionKey key, Selector selector) throws ReadException, WriteException {
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        ConnectionStatusE status = con.GetStatus();
        if(status == ConnectionStatusE.TRY_HANDSHAKE){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " start read handshake");
            ReadHandshake(key, selector);
        }
        else if(status == ConnectionStatusE.LISTENER_ID || status == ConnectionStatusE.LISTENER_LENGTH || status == ConnectionStatusE.LISTENER_DATA){
            ListenPeer(key);
        }
        else if(status == ConnectionStatusE.SERVER_HANDSHAKE){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " start read handshake");
            ReadHandshake(key, selector);
        }
    }

    private void WriteHanshake(SelectionKey key,  Selector selector) throws WriteException {
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        try{
            channel.configureBlocking(false);
            try{
                hsManager.DoHandShake(channel);
            }
            catch (WriteException e){
                peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            }
            PeerDataContoller con = peer.GetPeerDataCon();
            con.ChangeStatus(ConnectionStatusE.TRY_HANDSHAKE);
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " HANSHAKE SEND");
        }
        catch (IOException e){
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't connection: " +  e.getMessage());
        }
    }

    private void WriteInterested(SelectionKey key) throws WriteException {
        byte[] interested = new byte[5];
        interested[0] = 0;
        interested[1] = 0;
        interested[2] = 0;
        interested[3] = 1;
        interested[4] = 2;
        ByteBuffer buffer = ByteBuffer.wrap(interested);
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer) key.attachment();
        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send interested: " + e.getMessage());
        }
        PeerDataContoller con = peer.GetPeerDataCon();
        if(con.GetStatus() != ConnectionStatusE.LOAD_DATA){
            con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
        }
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " SEND INTERESTE" );
    }

    private void WriteReq(SelectionKey key) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerTask task = peer.GetTask();
        int sizeBlock = task.GetSizeBlock();
        byte[] reqv = new byte[17];
        reqv[0] = 0;
        reqv[1] = 0;
        reqv[2] = 0;
        reqv[3] = 13;
        reqv[4] = (byte)MessageIdE.REQUEST.getValue();
        int curIndex = 5;
        SocketChannel channel = (SocketChannel) key.channel();
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()){
            DataOutputStream out = new DataOutputStream(byteStream);
            out.writeInt(task.GetSegmentId());
            out.writeInt(task.GetOffset());
            out.writeInt(sizeBlock);

            log.debug(peer.GetHost() + ":" + peer.GetPort() + " REQUESTED: " + ":" + task.GetOffset() + ":" + sizeBlock );
            byte[] bytes = byteStream.toByteArray();
            for(int i = 0; i < 12; ++i){
                reqv[curIndex] = bytes[i];
                curIndex++;
            }
            ByteBuffer buffer = ByteBuffer.wrap(reqv);
            try {
                int write = channel.write(buffer);
                log.debug(peer.GetHost() + ":" + peer.GetPort() + " SEND REQV: " + write);
                PeerDataContoller con = peer.GetPeerDataCon();
                con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            } catch (IOException e) {
                throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send reqv: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send reqv: " + e.getMessage());
        }
    }

    public void WriteBitset(SelectionKey key, int countSegment) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        BitSet bitSet = new BitSet(countSegment);
        int bitSetSize = (int)Math.ceil((double)countSegment / 8);
        log.trace(" server bitsetsize: " + bitSetSize);
        int length = bitSetSize + 1;
        byte[] bitSetMes = new byte[length + 4];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeInt(length);
        } catch (IOException e) {
            throw new WriteException("server can't write bitset length:" + e.getMessage());
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if(bytes.length != 4){
            throw new WriteException("server wrong length size:" );
        }
        System.arraycopy(bytes, 0, bitSetMes, 0, bytes.length);
        bitSetMes[4] = (byte)MessageIdE.BITFIELD.getValue();
        for(int i = 0; i < countSegment; ++i){
            bitSet.set(i, false);
        }
        ArrayList<Integer> haveParts = con.GetHaveParts();
        for(Integer index : haveParts){
            bitSet.set(index, true);
        }
        byte[] byteBitSet = bitSet.toByteArray();
        if(countSegment % 8 != 0){
            int countOffset = 8 - countSegment % 8;
            byteBitSet [byteBitSet.length - 1] <<= countOffset;
        }
        for(int i = 5; i < 5 + byteBitSet.length; ++i){
            bitSetMes[i] = byteBitSet[i - 5];
        }
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.wrap(bitSetMes);
        try {
            int write = channel.write(buffer);
            log.trace("server bitset write: " + write);
        } catch (IOException e) {
            throw new WriteException("server can't write bitset" );
        }
        con.ChangeStatus(ConnectionStatusE.SEND_UNCHOKE);
    }

    public void WriteUnchoke(SelectionKey key) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        byte[] unchoke = new byte[5];
        unchoke[0] = 0;
        unchoke[1] = 0;
        unchoke[2] = 0;
        unchoke[3] = 1;
        unchoke[4] = 1;
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.wrap(unchoke);
        try {
            int write = channel.write(buffer);
            log.trace("server bitset write: " + write);
        } catch (IOException e) {
            throw new WriteException("server can't write bitset" );
        }
        con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
    }

    public void WriteSeg(SelectionKey key) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerServerTask peerServerTask = peer.GetServerTask();
        PeerDataContoller con = peer.GetPeerDataCon();
        byte[] seg = peerServerTask.GetData();
        int segmentId = peerServerTask.GetSegmentId();
        int offset = peerServerTask.GetOffset();;
        int bufferSize = 4 + 1 + 4 + 4 + seg.length;
        int length = 1 + 4 + 4 + seg.length;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(length);
        buffer.put((byte)MessageIdE.PIECE.getValue());
        buffer.putInt(segmentId);
        buffer.putInt(offset);
        buffer.put(seg);
        buffer.flip();
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            int write = channel.write(buffer);
            log.trace("server seg write: " + write);
        } catch (IOException e) {
            throw new WriteException("server can't write piece" );
        }
        con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
    }

    public void WriteHave(SelectionKey key) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerServerTask peerServerTask = peer.GetServerTask();
        PeerDataContoller con = peer.GetPeerDataCon();
        HashSet<Integer> haveParts = peerServerTask.GetNeedHave();
        SocketChannel channel = (SocketChannel) key.channel();
        for(Integer index : haveParts){
            byte[] have = new byte[9];
            have[0] = 0;
            have[1] = 0;
            have[2] = 0;
            have[3] = 5;
            have[4] = (byte)MessageIdE.HAVE.getValue();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeInt(index);
            } catch (IOException e) {
                throw new WriteException("server can't write have length:" + e.getMessage());
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            if(bytes.length != 4){
                throw new WriteException("server HAVE wrong length size:" );
            }
            System.arraycopy(bytes, 0, have, 5, bytes.length);
            ByteBuffer buffer = ByteBuffer.wrap(have);
            try {
                int write = channel.write(buffer);
                log.trace("server have write: " + write);
            } catch (IOException e) {
                throw new WriteException("server can't write have" );
            }
        }
        con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
    }

    public void DefineWrite(SelectionKey key, Selector selector, int countSegment) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        ConnectionStatusE status = con.GetStatus();
        if(status == ConnectionStatusE.CONNECTED){
            WriteHanshake(key, selector);
        }
        else if(status == ConnectionStatusE.INTERESTED){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " INTERESTED");
            WriteInterested(key);
        }
        else if(status == ConnectionStatusE.REQUESTED){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " REQUESTED");
            WriteReq(key);
        }
        else if(status == ConnectionStatusE.SEND_UNCHOKE){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " UNCHOKE");
            WriteUnchoke(key);
        }
        else if(status == ConnectionStatusE.SEND_BITSET){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " BITSET");
            WriteBitset(key, countSegment);
        }
        else if(status == ConnectionStatusE.READY_SEND_SEG){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " READY_SEND_SEG");
            WriteSeg(key);
        }
        else if(status == ConnectionStatusE.SEND_HAVE){
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " SEND_HAVE");
            WriteHave(key);
        }
    }
}
