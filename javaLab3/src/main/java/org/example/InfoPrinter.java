package org.example;

import org.example.torrent.Torrent;

public class InfoPrinter {
    public void PrintStartInfo(Torrent torrent){
        System.out.println("torrent name: " + torrent.GetName());
        System.out.println("comment: " + torrent.GetComment());
        System.out.println("created by: " + torrent.GetCreatedBy());
        System.out.println("created date: " + torrent.GetCreatedDate());
    }
}
