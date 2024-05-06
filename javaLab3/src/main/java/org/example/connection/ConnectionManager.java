package org.example.connection;

import org.example.exceptions.ConnectionError;
import org.example.torrent.TorrentClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

public class ConnectionManager {
    private TorrentClient torrent;
    private Map<String, ConnectionStatusE> connectionStatus = new HashMap<>();
    private HandShake hsManager;

    private String GetHostAddress(SocketChannel channel){
        Socket socket = channel.socket();
        InetAddress host = socket.getInetAddress();
        String address = host.getHostAddress() + " " + socket.getPort();
        return address;
    }

    public ConnectionManager(ArrayList<Peer> peers, TorrentClient torrent) throws ConnectionError {
        this.torrent = torrent;
        hsManager = new HandShake(torrent);
        Selector selector = null;
        try{
            selector = Selector.open();
        }
        catch (IOException e){
            System.out.println("connection error: " + e.getMessage());
        }
        for(Peer peer : peers){
            try {
                SocketChannel socketChannel = SelectorProvider.provider().openSocketChannel();
                socketChannel.socket().connect(new InetSocketAddress(peer.GetHost(), peer.GetPort()), 1000);
                if(socketChannel.isConnected()){
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    connectionStatus.put(GetHostAddress(socketChannel), ConnectionStatusE.CONNECTED);
                    System.out.println("Connectrd: " + peer.GetHost() );
                }
            }
            catch (IOException e){
                System.out.println("connection error: " + e.getMessage());
            }
        }
        while(true){
            int readyChannels = 0;
            try{
                readyChannels = selector.select();
            }
            catch(IOException e){
                System.out.println("connection error: " + e.getMessage());
            }
            if (readyChannels == 0){
                continue;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                    if(key.isWritable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.CONNECTED){
                        try{
                            channel.configureBlocking(false);
                            hsManager.DoHandShake(channel, key);
                            connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.HANDSHAKED);
                            System.out.println("HANSHAKE SEND: " + GetHostAddress(channel));
                        }
                        catch (IOException e){
                            channel.keyFor(selector).cancel();
                            System.out.println("can't connection: " + GetHostAddress(channel) +  " " +  e.getMessage());
                            continue;
                        }
                    }

                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if(connectionStatus.get(GetHostAddress(channel)) == ConnectionStatusE.HANDSHAKED){
                        ByteBuffer buffer = ByteBuffer.allocate(68);
                        int bytesRead = 0;
                        try{
                            bytesRead = channel.read(buffer);
                        }
                        catch (IOException e){
                            channel.keyFor(selector).cancel();
                            System.out.println("ERROR cant read handshake: " +  GetHostAddress(channel) +  " " + e.getMessage());
                            continue;
                        }
                        System.out.println(buffer);
                        if(bytesRead < 68){
                            channel.keyFor(selector).cancel();
                            System.out.println("ERROR cant read handshake bytes read: " + bytesRead + " "+ GetHostAddress(channel));
                            continue;
                        }
                        buffer.flip();
                        byte[] data = new byte[bytesRead];
                        buffer.get(data);
                        if( hsManager.CheckHandShacke(data) == HandShackeStatusE.SUCCESSFUL){
                            connectionStatus.put(GetHostAddress(channel), ConnectionStatusE.HANDSHAKED);
                            System.out.println("HANDSHAKE  " + GetHostAddress(channel));
                        }
                        else{
                            channel.keyFor(selector).cancel();
                            System.out.println("can't handShake: " + GetHostAddress(channel));
                        }
                    }
                }
            }
        }

//        for(Peer peer : peers){
//            Thread thread = new Thread(new PeerConnection(peer, torrent));
//            thread.start();
//        }
    }
}
