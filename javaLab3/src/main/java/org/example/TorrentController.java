package org.example;

import org.example.exceptions.*;
import org.example.connection.ConnectionManager;
import org.example.connection.peer.Peer;
import org.example.file.save.FileSaver;
import org.example.torrent.TorrentClient;
import org.example.torrent.TorrentFile;
import org.example.tracker.Tracker;

import java.io.IOException;
import java.util.ArrayList;

public class TorrentController {

    public TorrentController(String torrentPath, String folderPath) throws WrongTorrentFileFormat, ServerCommunicateError, CantcreateFile, IOException, ConnectionError, SaveDataException {
        TorrentClient torrent = new TorrentClient(torrentPath, folderPath);
        TorrentFile torrentFile = torrent.GetTorrentFile();
        InfoPrinter infoPrinter = new InfoPrinter();
        infoPrinter.PrintStartInfo(torrent);
        Tracker serverComm = new Tracker();
        ArrayList<Peer> peers = serverComm.GetPeers(torrent, folderPath);
        infoPrinter.PrintPeers(peers);
        ConnectionManager connectionManager = new ConnectionManager(peers,  torrent, torrentFile.GetFiles().get(0), folderPath);
    }
}
