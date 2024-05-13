package org.example.connection;

import org.example.connection.peer.Peer;
import org.example.connection.peer.PeerDataContoller;
import org.example.connection.peer.PeerMessage;
import org.example.connection.peer.PeerTask;
import org.example.connection.states.*;
import org.example.exceptions.ReadException;
import org.example.exceptions.WriteException;
import org.example.torrent.TorrentClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ConnectionLogic {
    private HandShake hsManager;
    private TorrentClient torrent;
    public ConnectionLogic(TorrentClient torrent){
        hsManager = new HandShake(torrent);
        this.torrent = torrent;
    }

    private void ReadHandshake(SelectionKey key, Selector selector) throws ReadException {
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        con.BufferConnect(68);
        int bytesRead = 0;
        try{
            bytesRead = channel.read(con.GetBuffer());
        }
        catch (IOException e){
            channel.keyFor(selector).cancel();
            throw new ReadException("ERROR cant read handshake: " +  peer.GetHost() + ":" + peer.GetPort() +  " " + e.getMessage());
        }
        if(bytesRead == -1){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() +  " can't read");
        }
        con.ReadByte(bytesRead);
        if(con.GetNeededWrite() != 0){
            channel.keyFor(selector).cancel();
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " Wait part byte: " + con.GetNeededWrite());
        }
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " get all byte ");
        con.GetBuffer().flip();
        byte[] data = new byte[68];
        con.GetBuffer().get(data);
        if(hsManager.CheckHandShacke(data) == HandShackeStatusE.SUCCESSFUL){
            con.BufferDisconnect();
            con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " HANDSHAKE SUCCESSFULE");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ByteBuffer test = ByteBuffer.allocate(100);
        }
        else{
            channel.keyFor(selector).cancel();
            System.out.println("can't handShake: " + peer.GetHost() + ":" + peer.GetPort());
        }
    }

    private void HandleFinishRead(Peer peer){
        PeerDataContoller con = peer.GetPeerDataCon();
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " handle finish read" );
        ConnectionStatusE status = con.GetStatus();
        con.SaveBuffer();
        con.BufferDisconnect();
        if(status == ConnectionStatusE.LISTENER_LENGTH){
            con.ChangeStatus(ConnectionStatusE.LISTENER_ID);
        }
        else if(status == ConnectionStatusE.LISTENER_ID){
            con.ChangeStatus(ConnectionStatusE.LISTENER_DATA);
        }
    }

    private void ReadData(SocketChannel channel, Peer peer) throws ReadException {
        int bytesRead;
        PeerDataContoller con = peer.GetPeerDataCon();
        ByteBuffer buffer  = con.GetBuffer();
        try{
            bytesRead = channel.read(buffer);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " read byte: " + bytesRead);
        }
        catch (IOException e){
            throw new ReadException("ERROR cant data-length: " + peer.GetHost() + ":" + peer.GetPort() +  " " + e.getMessage());
        }
        if(bytesRead == -1){
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.DISCONNECTED);
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() +  " can't read, no data");
        }
        con.ReadByte(bytesRead);
        if(con.GetNeededWrite() != 0){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " Wait part byte: " + con.GetNeededWrite());
        }
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " get all byte ");
        HandleFinishRead(peer);
    }

    private void DefineId(Peer peer) throws ReadException {
        PeerDataContoller con = peer.GetPeerDataCon();
        int id = con.GetMessage().GetId();
        if(id == MessageIdE.BITFIELD.getValue()){
            con.ChangeStatus(ConnectionStatusE.INTERESTED);
            con.SetParts();
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  BITFIELD");
        }
        else if(id == MessageIdE.UNCHOKE.getValue() && peer.GetPeerStatus() == PeerStatusE.CHOKE){
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " befpre UNCHOKE " + con.GetStatus());
            peer.Unchoke();
            if(con.GetStatus() != ConnectionStatusE.LOAD_DATA){
                con.ChangeStatus(ConnectionStatusE.WAIT_TASK);
                peer.GetTask().Ready(MessageIdE.UNCHOKE);
            }
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  UNCHOKE");
        }
        else if(id == MessageIdE.CHOKE.getValue()){
            peer.Choke();
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  CHOKE");
        }
        else if(id == MessageIdE.HAVE.getValue()){
            con.ApplyHave();
            if(con.GetStatus() != ConnectionStatusE.LOAD_DATA){
                con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            }
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  HAVE");
        }
        else if(id == MessageIdE.PIECE.getValue()){
            con.ChangeStatus(ConnectionStatusE.LOAD_DATA);
            peer.GetTask().Ready(MessageIdE.PIECE);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  PIECE");
        }
        else{
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " unexpected id " + id);
        }
    }

    private void ListenPeer(SelectionKey key) throws ReadException {
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " LISTEN_PEER");
        if(con.GetStatus() == ConnectionStatusE.LISTENER_LENGTH){
            con.BufferConnect(4);
            ReadData(channel, peer);
        }
        if(con.GetStatus() == ConnectionStatusE.LISTENER_ID){
            con.BufferConnect(1);
            ReadData(channel, peer);
        }
        if(con.GetStatus() == ConnectionStatusE.LISTENER_DATA){
            PeerMessage message = con.GetMessage();
            con.BufferConnect(message.GetLength() - 1);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " lenght: " +  message.GetLength() + " ID: " + message.GetId());
            ReadData(channel, peer);
        }
        DefineId(peer);
    }

    public void DefineRead(SelectionKey key, Selector selector) throws ReadException {
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        ConnectionStatusE status = con.GetStatus();
        if(status == ConnectionStatusE.TRY_HANDSHAKE){
            ReadHandshake(key, selector);
        }
        else if(status == ConnectionStatusE.LISTENER_ID || status == ConnectionStatusE.LISTENER_LENGTH || status == ConnectionStatusE.LISTENER_DATA){
            ListenPeer(key);
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
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " HANSHAKE SEND");
        }
        catch (IOException e){
            channel.keyFor(selector).cancel();
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
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " SEND INTERESTE" );
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

            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " REQUESTED: " + ":" + task.GetOffset() + ":" + sizeBlock );
            byte[] bytes = byteStream.toByteArray();
            for(int i = 0; i < 12; ++i){
                reqv[curIndex] = bytes[i];
                curIndex++;
            }
            ByteBuffer buffer = ByteBuffer.wrap(reqv);
            try {
                int write = channel.write(buffer);
                System.out.println(peer.GetHost() + ":" + peer.GetPort() + " SEND REQV: " + write);
                PeerDataContoller con = peer.GetPeerDataCon();
                con.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            } catch (IOException e) {
                throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send reqv: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send reqv: " + e.getMessage());
        }
    }

    public void DefineWrite(SelectionKey key, Selector selector) throws WriteException {
        Peer peer = (Peer)key.attachment();
        PeerDataContoller con = peer.GetPeerDataCon();
        ConnectionStatusE status = con.GetStatus();
        if(status == ConnectionStatusE.CONNECTED){
            WriteHanshake(key, selector);
        }
        else if(status == ConnectionStatusE.INTERESTED){
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " INTERESTED");
            WriteInterested(key);
        }
        else if(status == ConnectionStatusE.REQUESTED){
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " REQUESTED");
            WriteReq(key);
        }
    }
}
