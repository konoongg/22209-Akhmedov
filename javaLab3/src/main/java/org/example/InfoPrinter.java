package org.example;

import org.example.connection.Peer;
import org.example.torrent.TorrentClient;
import org.example.torrent.TorrentFile;

import java.util.ArrayList;

public class InfoPrinter {

    public void PrintStartInfo(TorrentClient torrent){
        TorrentFile torrentFile = torrent.GetTorrentFile();
        System.out.println("torrent name: " + torrentFile.GetName());
        System.out.println("comment: " + torrentFile.GetComment());
        System.out.println("created by: " + torrentFile.GetCreatedBy());
        System.out.println("created date: " + torrentFile.GetCreatedDate());
        System.out.println();
        System.out.println();
    }

    public void PrintPeers(ArrayList<Peer> peers){
        System.out.println("peers list");
        for(Peer peer : peers){
            System.out.println("peer: " + peer.GetHost() +":"+ peer.GetPort());
        }
        System.out.println();
        System.out.println();
    }
}
