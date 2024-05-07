package org.example.connection;

import org.example.exceptions.ConnectionError;
import org.example.exceptions.ReadException;
import org.example.exceptions.WriteException;
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
    private ConnectionLogic connectionLogic;

    public ConnectionManager(ArrayList<Peer> peers, TorrentClient torrent) throws ConnectionError {
        this.torrent = torrent;
        Selector selector = null;
        connectionLogic = new ConnectionLogic(torrent);
        try{
            selector = Selector.open();
        }
        catch (IOException e){
            throw new ConnectionError("connection error: " + e.getMessage());
        }
        for(Peer peer : peers){
            try {
                SocketChannel socketChannel = SelectorProvider.provider().openSocketChannel();
                socketChannel.socket().connect(new InetSocketAddress(peer.GetHost(), peer.GetPort()), 1000);
                if(socketChannel.isConnected()){
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, peer);
                    connectionStatus.put(connectionLogic.GetHostAddress(socketChannel), ConnectionStatusE.CONNECTED);
                    System.out.println("Connectrd: " + peer.GetHost() );
                }
            }
            catch (IOException e){
                System.out.println("connection error " + peer.GetHost() + " " + peer.GetPort() + ": " + e);
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
                    try {
                        connectionLogic.DefineWrite(channel, connectionStatus, selector);
                    } catch (WriteException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (key.isReadable()) {
                    try {
                        connectionLogic.DefineRead(key , connectionStatus, selector);
                    } catch (ReadException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
