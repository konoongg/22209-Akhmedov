package org.example.connection;

import org.example.exceptions.ConnectionError;
import org.example.exceptions.ReadException;
import org.example.exceptions.WriteException;
import org.example.torrent.TorrentClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    public String GetHostAddress(SocketChannel channel){
        Socket socket = channel.socket();
        InetAddress host = socket.getInetAddress();
        String address = host.getHostAddress() + " " + socket.getPort();
        return address;
    }

    private void ReadHandshake(SocketChannel channel, Map<String, ConnectionStatusE> connectionStatus, Selector selector) throws ReadException {
        ByteBuffer buffer = ByteBuffer.allocate(68);
        int bytesRead = 0;
        try{
            bytesRead = channel.read(buffer);
        }
        catch (IOException e){
            channel.keyFor(selector).cancel();
            throw new ReadException("ERROR cant read handshake: " +  GetHostAddress(channel) +  " " + e.getMessage());
        }
        if(bytesRead < 68){
            channel.keyFor(selector).cancel();
            throw new ReadException("ERROR cant read handshake bytes read: " + bytesRead + " "+ GetHostAddress(channel));
        }
        buffer.flip();
        byte[] data = new byte[bytesRead];
        buffer.get(data);
        if(hsManager.CheckHandShacke(data) == HandShackeStatusE.SUCCESSFUL){
            connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.LISTENER);
            System.out.println("HANDSHAKE  " + GetHostAddress(channel));
        }
        else{
            channel.keyFor(selector).cancel();
            System.out.println("can't handShake: " + GetHostAddress(channel));
        }
    }

    private void ReadData(SocketChannel channel, ByteBuffer buffer, int  dataSize) throws ReadException {
        int bytesRead;
        try{
            bytesRead = channel.read(buffer);
            buffer.rewind();
        }
        catch (IOException e){
            throw new ReadException("ERROR cant data-length: " +  GetHostAddress(channel) +  " " + e.getMessage());
        }
        if(bytesRead < dataSize){
            throw new ReadException("wrong  data length: " + bytesRead + " but expected  " +  dataSize + " " +  GetHostAddress(channel));
        }
    }

    private void DefineId(int id, SelectionKey key, Map<String, ConnectionStatusE> connectionStatus, ByteBuffer data) throws ReadException {
        if(id == MessageIdE.BITFIELD.getValue()){
            SocketChannel channel = (SocketChannel) key.channel();
            connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.INTERESTED);
            Peer peer = (Peer)key.attachment();
            peer.SetParts(data.array());
        }
        else if(id == MessageIdE.UNCHOKE.getValue()){
            Peer peer = (Peer)key.attachment();
            peer.Unchoke();
        }
        else if(id == MessageIdE.CHOKE.getValue()){
            Peer peer = (Peer)key.attachment();
            peer.Choke();
        }
        else{
            throw new ReadException("unexpected id " + id);
        }
    }

    private void ListenPeer(SocketChannel channel, SelectionKey key, Map<String, ConnectionStatusE> connectionStatus) throws ReadException {
        ByteBuffer lengthB = ByteBuffer.allocate(4);
        ByteBuffer idB = ByteBuffer.allocate(1);
        ReadData(channel, lengthB, 4);
        ReadData(channel, idB, 1);
        int length = lengthB.getInt();
        int  id = idB.get() & 0xFF;
        System.out.println(GetHostAddress(channel) + " " + id + " " + length);
        ByteBuffer data = ByteBuffer.allocate(length - 1);
        ReadData(channel, data, length - 1 );
        DefineId(id, key, connectionStatus, data);
    }


    public void DefineRead(SelectionKey key, Map<String, ConnectionStatusE> connectionStatus, Selector selector) throws ReadException {
        SocketChannel channel = (SocketChannel) key.channel();
        if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.TRY_HANDSHAKE){
            ReadHandshake(channel, connectionStatus, selector);
        }
        else if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.LISTENER){
            ListenPeer(channel, key, connectionStatus);
        }
    }

    private void WriteHanshake(SocketChannel channel, Map<String, ConnectionStatusE> connectionStatus, Selector selector) throws WriteException {
        try{
            channel.configureBlocking(false);
            hsManager.DoHandShake(channel);
            connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.TRY_HANDSHAKE);
            System.out.println("HANSHAKE SEND: " + GetHostAddress(channel));
        }
        catch (IOException e){
            channel.keyFor(selector).cancel();
            throw new WriteException("can't connection: " + GetHostAddress(channel) +  " " +  e.getMessage());
        }
    }

    private void WriteInterested(SocketChannel channel, Map<String, ConnectionStatusE> connectionStatus) throws WriteException {
        byte[] interested = new byte[5];
        interested[0] = 0;
        interested[1] = 0;
        interested[2] = 0;
        interested[3] = 1;
        interested[4] = 2;
        ByteBuffer buffer = ByteBuffer.wrap(interested);
        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new WriteException("can't send interested: " + e.getMessage());
        }
        connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.REQUESTED);
        System.out.println("SEND INTERSTE  " + GetHostAddress(channel));
    }

    private void WriteReq(SocketChannel channel, Map<String, ConnectionStatusE> connectionStatus, int segment, int offset) throws WriteException {
        int sizeBlock = 16 * 1024;
        byte[] reqv = new byte[17];
        reqv[0] = 0;
        reqv[1] = 0;
        reqv[2] = 0;
        reqv[3] = 13;
        reqv[4] = (byte)MessageIdE.REQUEST.getValue();
        int curIndex = 5;
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
                channel.write(buffer);
                connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.LISTENER);
            } catch (IOException e) {
                throw new WriteException("can't send reqv: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new WriteException("can't send reqv: " + e.getMessage());
        }
    }

    public void DefineWrite(SocketChannel channel, Map<String, ConnectionStatusE> connectionStatus, Selector selector) throws WriteException {
        while (true){
            if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.CONNECTED){
                WriteHanshake(channel, connectionStatus, selector);
            }
            else if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.INTERESTED){
                WriteInterested(channel, connectionStatus);
            }
            else if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.REQUESTED){
                WriteReq(channel, connectionStatus, 0, 0);
            }
            else {
                break;
            }
        }
    }
}
