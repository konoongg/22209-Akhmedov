package org.example.file.save;

import org.example.exceptions.CantcreateFile;
import org.example.exceptions.SaveDataException;
import org.example.torrent.TorrentClient;
import org.example.torrent.TorrentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSaveManager {
    private static final Logger log = LoggerFactory.getLogger(FileSaveManager.class);
    private SegmentChecker segmentChecker;
    private FileSaver fileSaver;
    private int segmentSize;
    private int downloadedPieces;

    public FileSaveManager(TorrentClient torrent, String folderPath) throws CantcreateFile {
        segmentChecker = new SegmentChecker(torrent.GetTorrentFile().GetSegmentsSha1());
        TorrentFile torrentFile = torrent.GetTorrentFile();
        segmentSize = torrentFile.GetPieceLength();
        downloadedPieces = 0;
        fileSaver = new FileSaver(torrentFile.GetFiles(), folderPath, torrentFile.GetName());
    }

    private int DefineOffset(int segmentId){
        return segmentId * segmentSize;
    }

    public boolean Write(int segmentId, byte[] segment) throws SaveDataException {
        if(segmentChecker.CheckSegment(segmentId, segment)){
            downloadedPieces++;
           log.info("______WRITE_______ : " + segmentId + ", downloaded pieces: " + downloadedPieces);
            fileSaver.Write(segment, DefineOffset(segmentId));
            return true;
        }
       log.debug("______ NOT WRITE_______ : " + segmentId);
        return false;
    }
}
