package org.example;

import org.example.peers.Peer;
import org.example.torrent.Torrent;

import java.util.ArrayList;

public class InfoPrinter {

    public void PrintStartInfo(Torrent torrent){
        System.out.println("torrent name: " + torrent.GetName());
        System.out.println("comment: " + torrent.GetComment());
        System.out.println("created by: " + torrent.GetCreatedBy());
        System.out.println("created date: " + torrent.GetCreatedDate());
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
