package org.example.connection;

import org.example.torrent.TorrentClient;

import java.util.ArrayList;

public class ConnectionManager {
    public ConnectionManager(ArrayList<Peer> peers, TorrentClient torrent){
        for(Peer peer : peers){
            Thread thread = new Thread(new PeerConnection(peer, torrent));
            thread.start();
        }
    }
}
