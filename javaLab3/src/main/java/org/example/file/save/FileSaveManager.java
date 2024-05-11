package org.example.file.save;

import org.example.exceptions.CantcreateFile;
import org.example.exceptions.SaveDataException;
import org.example.torrent.TorrentClient;
import org.example.torrent.TorrentFile;

import java.io.IOException;

public class FileSaveManager {
    private SegmentChecker segmentChecker;
    private FileSaver fileSaver;
    private int segmentSize;

    public FileSaveManager(TorrentClient torrent, String folderPath) throws CantcreateFile {
        segmentChecker = new SegmentChecker(torrent.GetTorrentFile().GetSegmentsSha1());
        TorrentFile torrentFile = torrent.GetTorrentFile();
        segmentSize = torrentFile.GetDownloadSize();
        fileSaver = new FileSaver(torrentFile.GetFiles(), folderPath, torrentFile.GetName());
    }

    private int DefineOffset(int segmentId){
        return segmentId * segmentSize;
    }

    public boolean Write(int segmentId, byte[] segment) throws SaveDataException {
        if(segmentChecker.CheckSegment(segmentId, segment)){
            System.out.println("______WRITE_______ : " + segmentId);
            fileSaver.Write(segment, DefineOffset(segmentId) );
            return true;
        }

        System.out.println("______ NOT WRITE_______ : " + segmentId);
        return false;
    }

}
