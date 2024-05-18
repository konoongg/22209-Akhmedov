package org.example.torrent;

import org.example.file.FileT;
import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TorrentFile {
    private byte[] announce;
    private String name;
    private String comment;
    private String createdBy;
    private Date createdDate;
    private ArrayList<byte[]> segmentsSha1 = new ArrayList<>();
    private ArrayList<FileT> files = new ArrayList<>();
    private byte[] info;
    private int downloadSize;
    private int countParts;
    private int pieceLength;

    private void  DefineFields(Map<String, byte[]>  mainDict, Map<String, byte[]>  infoDict, String folderPath) throws WrongTorrentFileFormat {
        DefinerFields definer = new DefinerFields();
        announce = definer.DefineAnnounce(mainDict);
        definer.DefineSegments(infoDict, segmentsSha1);
        countParts = definer.DefineCountParts(infoDict);
        pieceLength = definer.DefinePieceLength(infoDict);
        name = definer.DefineName(infoDict);
        definer.DefineFiles(infoDict, folderPath, files, GetCountParts(), pieceLength, name);
        comment = definer.DefineComment(mainDict);
        createdBy = definer.DefineCreatedBy(mainDict);
        createdDate = definer.DefineCreatedDate(mainDict);
        downloadSize = definer.DefineDownloadSize();
    }

    public TorrentFile(String torrentPath, String folderPath) throws WrongTorrentFileFormat, ServerCommunicateError {
        Map<String, byte[]>  mainDict = new HashMap<>();
        Map<String, byte[]>  infoDict = new HashMap<>();
        byte[] text = null;
        try (FileInputStream fis = new FileInputStream(torrentPath)) {
            text = fis.readAllBytes();
            text = Arrays.copyOfRange(text, 1, text.length - 1);
        } catch (IOException e) {
            throw new WrongTorrentFileFormat("can't read torrent file: " + e.getMessage());
        }
        TorrentParser parser = new TorrentParser();
        parser.ParseText(mainDict, text);
        if(!mainDict.containsKey("info")){
            throw new WrongTorrentFileFormat("torrent file don't have field info");
        }
        parser.ParseText(infoDict, mainDict.get("info"));
        info = mainDict.get("info");
        DefineFields(mainDict, infoDict, folderPath);
    }

    public ArrayList<FileT> GetFiles(){
        return files;
    }

    public String GetName(){
        return name;
    }

    public String GetComment(){
        return comment;
    }

    public String GetCreatedBy(){
        return createdBy;
    }

    public Date GetCreatedDate(){
        return createdDate;
    }

    public int GetDownloadSize(){
        return downloadSize;
    }

    public String GetAnnounce() {
        return new String(announce);
    }

    public int GetCountParts(){
        return countParts;
    }

    public int GetPieceLength(){
        return pieceLength;
    }

    public ArrayList<byte[]> GetSegmentsSha1(){
        return segmentsSha1;
    }

    public byte[] GetInfo(){
        byte[] extendedInfo = new byte[info.length + 2];
        extendedInfo[0] = (byte) 'd';
        System.arraycopy(info, 0, extendedInfo, 1, info.length);
        extendedInfo[extendedInfo.length - 1] = (byte) 'e';
        return  extendedInfo;
    }
}
