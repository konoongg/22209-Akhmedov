package org.example.connection;

import org.example.connection.states.ConnectionStatusE;
import org.example.connection.states.HandShackeStatusE;
import org.example.connection.states.MessageIdE;
import org.example.exceptions.ReadException;
import org.example.exceptions.WriteException;
import org.example.torrent.TorrentClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

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
        peer.BufferConnect(68);
        int bytesRead = 0;
        try{
            bytesRead = channel.read(peer.GetBuffer());
        }
        catch (IOException e){
            channel.keyFor(selector).cancel();
            throw new ReadException("ERROR cant read handshake: " +  peer.GetHost() + ":" + peer.GetPort() +  " " + e.getMessage());
        }
        if(bytesRead == -1){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() +  " can't read");
        }
        peer.ReadByte(bytesRead);
        if(peer.GetNeededWrite() != 0){
            channel.keyFor(selector).cancel();
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " Wait part byte: " + peer.GetNeededWrite());
        }
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " get all byte ");
        peer.GetBuffer().flip();
        byte[] data = new byte[68];
        peer.GetBuffer().get(data);
        if(hsManager.CheckHandShacke(data) == HandShackeStatusE.SUCCESSFUL){
            peer.BufferDisconnect();
            peer.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
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
        ConnectionStatusE status = peer.GetStatus();
        peer.SaveBuffer();
        peer.BufferDisconnect();
        if(status == ConnectionStatusE.LISTENER_LENGTH){
            peer.ChangeStatus(ConnectionStatusE.LISTENER_ID);
        }
        else if(status == ConnectionStatusE.LISTENER_ID){
            peer.ChangeStatus(ConnectionStatusE.LISTENER_DATA);
        }
    }

    private void ReadData(SocketChannel channel, Peer peer) throws ReadException {
        int bytesRead;
        ByteBuffer buffer  = peer.GetBuffer();
        try{
            bytesRead = channel.read(buffer);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " read byte: " + bytesRead);
        }
        catch (IOException e){
            throw new ReadException("ERROR cant data-length: " + peer.GetHost() + ":" + peer.GetPort() +  " " + e.getMessage());
        }
        if(bytesRead == -1){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() +  " can't read, no data");
        }
        peer.ReadByte(bytesRead);
        if(peer.GetNeededWrite() != 0){
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " Wait part byte: " + peer.GetNeededWrite());
        }
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " get all byte ");
        HandleFinishRead(peer);
    }

    private void DefineId(Peer peer) throws ReadException {
        int id = peer.GetMessage().GetId();
        if(id == MessageIdE.BITFIELD.getValue()){
            peer.ChangeStatus(ConnectionStatusE.INTERESTED);
            peer.SetParts();
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  BITFIELD");
        }
        else if(id == MessageIdE.UNCHOKE.getValue()){
            peer.Unchoke();
            peer.ChangeStatus(ConnectionStatusE.REQUESTED);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  UNCHOKE");
        }
        else if(id == MessageIdE.CHOKE.getValue()){
            peer.Choke();
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + "  CHOKE");
        }
        else{
            throw new ReadException(peer.GetHost() + ":" + peer.GetPort() + " unexpected id " + id);
        }
    }

    private void ListenPeer(SelectionKey key) throws ReadException {
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " LISTEN_PEER");
        if(peer.GetStatus() == ConnectionStatusE.LISTENER_LENGTH){
            peer.BufferConnect(4);
            ReadData(channel, peer);
        }
        if(peer.GetStatus() == ConnectionStatusE.LISTENER_ID){
            peer.BufferConnect(1);
            ReadData(channel, peer);
        }
        if(peer.GetStatus() == ConnectionStatusE.LISTENER_DATA){
            PeerMessage message = peer.GetMessage();
            peer.BufferConnect(message.GetLength() - 1);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " lenght: " +  message.GetLength() + " ID: " + message.GetId());
            ReadData(channel, peer);
        }
        DefineId(peer);
    }

    public void DefineRead(SelectionKey key, Selector selector) throws ReadException {
        Peer peer = (Peer)key.attachment();
        ConnectionStatusE status = peer.GetStatus();
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
            hsManager.DoHandShake(channel);
            peer.ChangeStatus(ConnectionStatusE.TRY_HANDSHAKE);
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
        peer.ChangeStatus(ConnectionStatusE.REQUESTED);
        System.out.println(peer.GetHost() + ":" + peer.GetPort() + " SEND INTERESTE" );
    }

    private void WriteReq(SelectionKey key, int segment, int offset) throws WriteException {
        int sizeBlock = 16 * 1024;
        byte[] reqv = new byte[17];
        reqv[0] = 0;
        reqv[1] = 0;
        reqv[2] = 0;
        reqv[3] = 13;
        reqv[4] = (byte)MessageIdE.REQUEST.getValue();
        int curIndex = 5;
        SocketChannel channel = (SocketChannel) key.channel();
        Peer peer = (Peer)key.attachment();
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()){
            DataOutputStream out = new DataOutputStream(byteStream);
            out.writeInt(segment);
            out.writeInt(offset);
            out.writeInt(sizeBlock);
            byte[] bytes = byteStream.toByteArray();
            for(int i = 0; i < 12; ++i){
                reqv[curIndex] = bytes[i];
                curIndex++;
            }
            ByteBuffer buffer = ByteBuffer.wrap(reqv);
            try {
                int write = channel.write(buffer);
                System.out.println(peer.GetHost() + ":" + peer.GetPort() + " SEND REQV: " + write);
                peer.ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //ByteBuffer testBuffer = ByteBuffer.allocate(20000);
                //int bytesRead = channel.read(testBuffer);

                //if(bytesRead == 216){
//                    System.out.println("WOW");
//                    testBuffer.rewind();
//                    for (int i = 0; i < bytesRead; i++) {
//                        System.out.println("B " + testBuffer.get());
//                    }
                //}

                //System.out.println("test " + bytesRead);
            } catch (IOException e) {
                throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send reqv: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new WriteException(peer.GetHost() + ":" + peer.GetPort() + " can't send reqv: " + e.getMessage());
        }
    }

    public void DefineWrite(SelectionKey key, Selector selector) throws WriteException {
        Peer peer = (Peer)key.attachment();
        while (true){
            ConnectionStatusE status = peer.GetStatus();
            if(status == ConnectionStatusE.CONNECTED){
                WriteHanshake(key, selector);
            }
            else if(status == ConnectionStatusE.INTERESTED){
                System.out.println(peer.GetHost() + ":" + peer.GetPort() + " INTERESTED");
                WriteInterested(key);
            }
            else if(status == ConnectionStatusE.REQUESTED){
                System.out.println(peer.GetHost() + ":" + peer.GetPort() + " REQUESTED");
                WriteReq(key, 0, 0);
            }
            else {
                break;
            }
        }
    }
}
