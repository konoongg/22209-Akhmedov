package org.example.connection;
import org.example.Main;
import org.example.connection.peer.Peer;
import org.example.connection.peer.PeerDataContoller;
import org.example.connection.peer.PeerServerTask;
import org.example.connection.peer.PeerTask;
import org.example.connection.states.ConnectionStatusE;
import org.example.connection.states.PeerDownloadedE;
import org.example.exceptions.*;
import org.example.file.save.FileSaveManager;
import org.example.file.FileT;
import org.example.file.SegmentManager;
import org.example.torrent.TorrentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;


public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private TorrentClient torrent;
    private Map<String, ConnectionStatusE> connectionStatus = new HashMap<>();
    private ConnectionLogic connectionLogic;
    private FileT fileT;
    private FileSaveManager fileSaveManager;
    private PeerBlackList peerBlackList;

    private int GetWork(Peer peer) throws SelectionSegmentException {
        SegmentManager segmentManager = fileT.GetSegmentManager();
        int segmentId = segmentManager.Segment(peer.GetPeerDataCon().GetParts());
        if(segmentId == -1){
            log.debug(peer.GetHost() + ":" + peer.GetPort() + "download all his part");
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.LISTENER_LENGTH);
            return - 1;
        }
        peer.GetTask().SetTask(segmentId, segmentManager.SegmentSize(segmentId));
        log.trace(peer.GetHost() + ":" + peer.GetPort() + " GET TASK: " + segmentId + ":"+ peer.GetTask().GetOffset());
        return  0;
    }

   private void CheckServerReadyWrite(Peer peer) throws ReadDataFromFileException {
        PeerDataContoller con = peer.GetPeerDataCon();
        if(!peer.GetServerTask().IsNeedHave()){
            peer.GetPeerDataCon().UpdateParts(fileT.GetSegmentManager().GetDownloadedParts(), peer.GetServerTask());
        }
        if(con.GetStatus() == ConnectionStatusE.LOAD_SERVER_DATA){
            PeerServerTask serverTask = peer.GetServerTask();
            int segmentId = serverTask.GetSegmentId();
            int offset = serverTask.GetOffset();
            int length = serverTask.getLength();
            serverTask.SetData(fileT.GetData(segmentId, offset, length));
            con.ChangeStatus(ConnectionStatusE.READY_SEND_SEG);
        }
    }

    private void CheckReadyWrite(Peer peer) throws SaveDataException, SelectionSegmentException, ReadDataFromFileException {
        PeerTask task = peer.GetTask();
        PeerDataContoller con = peer.GetPeerDataCon();
        if(task.Downloaded() == PeerDownloadedE.NEED_WORK){
            if(GetWork(peer) == -1){
                return;
            }
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " NEED WORk: ");
            peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.REQUESTED);
        }
        else if(task.Downloaded() == PeerDownloadedE.READY_All){
            try {
                task.LoadDataInBuf(peer.GetPeerDataCon().GetMessage().GetMes());
            } catch (ReadException e) {
                fileT.GetSegmentManager().CantDownload(task.GetSegmentId());
                if(GetWork(peer) == -1){
                    return;
                }
                return;
            }
            log.debug(peer.GetHost() + ":" + peer.GetPort() + " try write segment");
            if(fileSaveManager.Write(task.GetSegmentId(), task.GetSegment())){
                fileT.GetSegmentManager().CompliteDownload(task.GetSegmentId());
            }
            else{
                fileT.GetSegmentManager().CantDownload(task.GetSegmentId());
            }
            if(GetWork(peer) == -1){
                return;
            }
            con.ChangeStatus(ConnectionStatusE.REQUESTED);
        }
        else if(task.Downloaded() == PeerDownloadedE.READY_PART){
            try{
                task.LoadDataInBuf(peer.GetPeerDataCon().GetMessage().GetMes());
            }
            catch (ReadException e) {
                fileT.GetSegmentManager().CantDownload(task.GetSegmentId());
                if(GetWork(peer) == -1){
                    return;
                }
                return;
            }
            task.LoadNext();
            con.ChangeStatus(ConnectionStatusE.REQUESTED);
            log.trace(peer.GetHost() + ":" + peer.GetPort() + " NEW BLOCK" +  ":" + task.GetOffset());
        }
    }

    private void Reconnect(SelectionKey key, Selector selector, Peer peer){
        String address = peer.GetHost() + ":" + peer.GetPort();
        log.warn(address + " DISCONNECTED");
        peerBlackList.Disconnect(address);
        key.cancel();
        if(!peerBlackList.IsBlock(address)){
            try{
                PeerConnect(peer, selector);
                peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.CONNECTED);
                peer.GetPeerDataCon().SuccessfulReading();
                log.info(address + " RECONNECTED");
            }
            catch (IOException e){
                peerBlackList.Disconnect(address);
                log.warn(address + " CANT RECONNECTED");
            }
        }
        else{
            log.warn(address + " FYNNALY DISCONNECTED");
        }
    }

    private void CheckConnect(SelectionKey key, Selector selector){
        Peer peer = (Peer)key.attachment();
        if(peer.GetPeerDataCon().GetUnSuccessful() >= 10 || peer.GetPeerDataCon().GetStatus() == ConnectionStatusE.DISCONNECTED){
            Reconnect(key, selector, peer);
        }
    }

    public ConnectionManager(ArrayList<Peer> peers, TorrentClient torrent, FileT fileT, String folderPath) throws ConnectionError, CantcreateFile, SaveDataException, SelectionSegmentException, ReadDataFromFileException {
        this.torrent = torrent;
        fileSaveManager = new FileSaveManager(torrent, folderPath);
        this.fileT = fileT;
        connectionLogic = new ConnectionLogic(torrent);
        peerBlackList = new PeerBlackList();
        Connect(peers);
    }

    private void PeerConnect(Peer peer, Selector selector) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(peer.GetHost(), peer.GetPort()));
        channel.register(selector, SelectionKey.OP_CONNECT, peer);
    }

    private void ServerInit(Selector selector, int listenPort) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(listenPort));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void Connect(ArrayList<Peer> peers) throws ConnectionError, SaveDataException, SelectionSegmentException, ReadDataFromFileException {
        Selector selector = null;
        try{
            selector = Selector.open();
        }
        catch (IOException e){
            throw new ConnectionError("connection error: " + e.getMessage());
        }
        for(Peer peer : peers){
            try {
                PeerConnect(peer, selector);
            }
            catch (IOException e){
                log.warn("connection error " + peer.GetHost() + " " + peer.GetPort() + ": " + e);
            }
        }
        try {
            ServerInit(selector, torrent.GetPort());
            log.info("server init: ");
        }
        catch (IOException e){
            log.warn("server connection error: " + e);
        }
        while(true){
            int readyChannels = 0;
            try{
                readyChannels = selector.select();
            }
            catch(IOException e){
                log.warn("connection error: " + e.getMessage());
            }
            if (readyChannels == 0){
                continue;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    try {
                        if (channel.finishConnect()) {
                            log.info("Connected to " + channel.getRemoteAddress());
                            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        }
                    }
                    catch (IOException e) {
                        Peer peer = (Peer) key.attachment();
                        log.warn(peer.GetHost() + ":" + peer.GetPort() + " can't connaction: " + e.getMessage());
                    }
                }
                else if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    try{
                        SocketChannel client = server.accept();
                        InetSocketAddress clientAddress = (InetSocketAddress) client.getRemoteAddress();
                        Peer peer = new Peer(clientAddress.getAddress(), clientAddress.getPort(), fileT.GetSegmentManager().GetCountSegment());
                        peer.GetPeerDataCon().SetParts(fileT.GetSegmentManager().GetDownloadedParts());
                        peer.GetPeerDataCon().ChangeStatus(ConnectionStatusE.SERVER_HANDSHAKE);
                        peer.MadeItServer();
                        peers.add(peer);
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, peer);
                        log.info("server new client: " + client.getRemoteAddress());
                    }
                    catch(IOException e) {
                        log.warn("can't accept address");
                    }
                }
                else{
                    if (key.isReadable()) {
                        try {
                            connectionLogic.DefineRead(key, selector);
                        } catch (ReadException | WriteException e) {
                            log.debug(e.getMessage());
                        }
                    }
                    if(key.isWritable()) {
                        try {
                            connectionLogic.DefineWrite(key, selector, fileT.GetSegmentManager().GetCountSegment());
                        } catch (WriteException e) {
                            log.debug(e.getMessage());
                        }
                    }
                    Peer peer = (Peer)key.attachment();
                    if(!peer.IsItServer()){
                        if(!fileT.IsDownloaded()){
                            CheckReadyWrite(peer);
                        }
                        else{
                            log.info(peer.GetHost() + ":" + peer.GetPort() + " disconnect:");
                            key.cancel();
                        }
                    }
                    if(peer.IsItServer()){
                        CheckServerReadyWrite(peer);
                    }
                    CheckConnect(key, selector);
                }
            }
        }
    }
}
