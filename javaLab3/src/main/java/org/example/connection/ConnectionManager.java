package org.example.connection;

import org.example.connection.peer.Peer;
import org.example.connection.peer.PeerDataContoller;
import org.example.connection.peer.PeerTask;
import org.example.connection.states.ConnectionStatusE;
import org.example.connection.states.PeerDownloadedE;
import org.example.exceptions.*;
import org.example.file.save.FileSaveManager;
import org.example.file.FileT;
import org.example.file.SegmentManager;
import org.example.torrent.TorrentClient;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

public class ConnectionManager {
    private TorrentClient torrent;
    private Map<String, ConnectionStatusE> connectionStatus = new HashMap<>();
    private ConnectionLogic connectionLogic;
    private FileT fileT;
    private FileSaveManager fileSaveManager;

    private void CheckReadyWrite(Peer peer) throws SaveDataException {
        PeerTask task = peer.GetTask();
        PeerDataContoller con = peer.GetPeerDataCon();
        PeerDataContoller controller = peer.GetPeerDataCon();
        if(task.Downloaded() == PeerDownloadedE.NEED_WORK){
            SegmentManager segmentManager = fileT.GetSegmentManager();
            int segment = segmentManager.Segment(controller.GetParts());
            task.SetTask(segment, segmentManager.SegmentSize(segment));
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " NEED_WORK " + segment);
        }
        else if(task.Downloaded() == PeerDownloadedE.READY_All){
            task.LoadDataInBuf(peer.GetPeerDataCon().GetMessage().GetMes());
            SegmentManager segmentManager = fileT.GetSegmentManager();
            int segmentId = segmentManager.Segment(controller.GetParts());
            task.SetTask(segmentId, segmentManager.SegmentSize(segmentId));
            if(fileSaveManager.Write(task.GetSegmentId(), task.GetSegment())){
                fileT.GetSegmentManager().CompliteDownload(segmentId);
            }
            else{
                fileT.GetSegmentManager().CantDownload(segmentId);
            }
            con.ChangeStatus(ConnectionStatusE.REQUESTED);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " GET TASK: " + segmentId + ":"+ task.GetOffset());
        }
        else if(task.Downloaded() == PeerDownloadedE.READY_PART){
            task.LoadDataInBuf(peer.GetPeerDataCon().GetMessage().GetMes());
            task.LoadNext();
            con.ChangeStatus(ConnectionStatusE.REQUESTED);
            System.out.println(peer.GetHost() + ":" + peer.GetPort() + " NEW BLOCK" +  ":" + task.GetOffset());
        }
    }

    private void CheckConnect(SelectionKey key){
        Peer peer = (Peer)key.attachment();
        if(peer.GetPeerDataCon().GetStatus() == ConnectionStatusE.DISCONNECTED){
            key.cancel();
        }
    }

    public ConnectionManager(ArrayList<Peer> peers, TorrentClient torrent, FileT fileT, String folderPath) throws ConnectionError, CantcreateFile, SaveDataException {
        this.torrent = torrent;
        fileSaveManager = new FileSaveManager(torrent, folderPath);
        this.fileT = fileT;
        connectionLogic = new ConnectionLogic(torrent);
        Connect(peers);
    }

    private void Connect(ArrayList<Peer> peers) throws ConnectionError, SaveDataException {

        Selector selector = null;
        try{
            selector = Selector.open();
        }
        catch (IOException e){
            throw new ConnectionError("connection error: " + e.getMessage());
        }
        for(Peer peer : peers){
            try {
                if(peer.GetPort() != 26009){
                    continue;
                }
                SocketChannel socketChannel = SelectorProvider.provider().openSocketChannel();
                socketChannel.socket().connect(new InetSocketAddress(peer.GetHost(), peer.GetPort()), 1000);
                if(socketChannel.isConnected()){
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, peer);
                    System.out.println("Connectrd: " + peer.GetHost() + ":" + peer.GetPort());
                }
            }
            catch (IOException e){
                System.out.println("connection error " + peer.GetHost() + " " + peer.GetPort() + ": " + e);
            }
        }
        while(!fileT.IsDownloaded()){
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
                keyIterator.remove();
                if (key.isReadable()) {
                    try {
                        connectionLogic.DefineRead(key, selector);
                    } catch (ReadException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if(key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    try {
                        connectionLogic.DefineWrite(key, selector);
                    } catch (WriteException e) {
                        System.out.println(e.getMessage());
                    }
                }
                Peer peer = (Peer)key.attachment();
                CheckReadyWrite(peer);
                CheckConnect(key);
            }
        }
    }
}
