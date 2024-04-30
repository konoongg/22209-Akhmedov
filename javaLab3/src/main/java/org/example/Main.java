package org.example;

import org.example.peers.ConnectionStarter;
import org.example.peers.Peer;
import org.example.torrent.FileSaver;
import org.example.torrent.Torrent;
import org.example.tracker.Tracker;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args)  {
        String torrentPath = args[0];
        String folderPath = args[1];
        try{
            Torrent torrent = new Torrent(torrentPath, folderPath);
            InfoPrinter infoPrinter = new InfoPrinter();
            infoPrinter.PrintStartInfo(torrent);
            FileSaver fileSaver = new FileSaver(torrent.GetFiles(), folderPath, torrent.GetName());
            Tracker serverComm = new Tracker(torrent);
            ArrayList<Peer> peers = serverComm.GetPeers();
            infoPrinter.PrintPeers(peers);
            ConnectionStarter connectionStarter = new ConnectionStarter(peers);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}