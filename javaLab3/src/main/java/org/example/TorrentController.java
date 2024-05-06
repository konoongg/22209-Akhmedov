package org.example;

import org.example.exceptions.CantcreateFile;
import org.example.exceptions.ConnectionError;
import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;
import org.example.connection.ConnectionManager;
import org.example.connection.Peer;
import org.example.torrent.TorrentClient;
import org.example.torrent.TorrentFile;
import org.example.tracker.Tracker;

import java.io.IOException;
import java.util.ArrayList;

public class TorrentController {

    public TorrentController(String torrentPath, String folderPath) throws WrongTorrentFileFormat, ServerCommunicateError, CantcreateFile, IOException, ConnectionError {
        TorrentClient torrent = new TorrentClient(torrentPath, folderPath);
        TorrentFile torrentFile = torrent.GetTorrentFile();
        InfoPrinter infoPrinter = new InfoPrinter();
        infoPrinter.PrintStartInfo(torrent);
        FileSaver fileSaver = new FileSaver(torrentFile.GetFiles(), folderPath, torrentFile.GetName());
        Tracker serverComm = new Tracker();
        ArrayList<Peer> peers = serverComm.GetPeers(torrent);
        infoPrinter.PrintPeers(peers);
        ConnectionManager connectionManager = new ConnectionManager(peers, torrent);
    }
}
