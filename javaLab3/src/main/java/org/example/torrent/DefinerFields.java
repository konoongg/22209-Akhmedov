package org.example.torrent;

import org.example.file.FileT;
import org.example.exceptions.WrongTorrentFileFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class DefinerFields {
    private int downloadSize = 0;

    public byte[] DefineAnnounce(Map<String, byte[]> mainDict) throws WrongTorrentFileFormat {
        if(!mainDict.containsKey("announce")){
            throw new WrongTorrentFileFormat("torrent file don't have field announce");
        }
        return mainDict.get("announce");
    }

    public void  DefineSegments(Map<String, byte[]>  infoDict, ArrayList<byte[]> segmentSha1) throws WrongTorrentFileFormat {
        if(!infoDict.containsKey("pieces")){
            throw new WrongTorrentFileFormat("torrent file don't have field info/pieces");
        }
        if(!infoDict.containsKey("piece length")){
            throw new WrongTorrentFileFormat("torrent file don't have field info/piece length");
        }
        int offset = 0;
        byte[] data = infoDict.get("pieces");
        int segmentsSize = data.length;
        while(offset < segmentsSize){
            byte[] subData = Arrays.copyOfRange(data, offset, offset + 20);
            offset += 20;
            segmentSha1.add(subData);
        }
    }

    public int DefineCountParts(Map<String, byte[]>  infoDict) throws WrongTorrentFileFormat {
        if(!infoDict.containsKey("pieces")){
            throw new WrongTorrentFileFormat("torrent file don't have field info/pieces");
        }
        byte[] data = infoDict.get("pieces");
        int segmentsSize = data.length;
        return segmentsSize / 20;
    }

    public void DefineFiles(Map<String, byte[]>  infoDict, String folderPath, ArrayList<FileT> files, int countParts, int pieceLength) throws WrongTorrentFileFormat {
        if(infoDict.containsKey("length")){
            int length = Integer.valueOf(new String(infoDict.get("length")));
            downloadSize = length;
            files.add(new FileT(folderPath, length, countParts, pieceLength));
        }
        else if(infoDict.containsKey("files")){
            //
        }
        else{
            throw new WrongTorrentFileFormat("torrent file don't have field info/length or info/files");
        }
    }

    public int DefinePieceLength(Map<String, byte[]>  infoDict) throws WrongTorrentFileFormat {
        if(!infoDict.containsKey("piece length")){
            throw new WrongTorrentFileFormat("torrent file don't have field info/length or info/name");
        }
        byte[] data = infoDict.get("piece length");
        String length = new String(data);
        return  Integer.valueOf(length);
    }

    public String DefineName(Map<String, byte[]>  infoDict) throws WrongTorrentFileFormat {
        if(!infoDict.containsKey("name")){
            throw new WrongTorrentFileFormat("torrent file don't have field info/length or info/name");
        }
        return  new String(infoDict.get("name"));
    }

    public String DefineComment(Map<String, byte[]>  mainDict) throws WrongTorrentFileFormat {
        if(!mainDict.containsKey("comment")){
            throw new WrongTorrentFileFormat("torrent file don't have field or comment");
        }
        return  new String(mainDict.get("comment"));
    }

    public String DefineCreatedBy(Map<String, byte[]>  mainDict) throws WrongTorrentFileFormat {
        if(!mainDict.containsKey("created by")){
            throw new WrongTorrentFileFormat("torrent file don't have field created by");
        }
        return new String(mainDict.get("created by"));
    }

    public Date DefineCreatedDate(Map<String, byte[]>  mainDict) throws WrongTorrentFileFormat {
        if(!mainDict.containsKey("creation date")){
            throw new WrongTorrentFileFormat("torrent file don't have field creation date");
        }
        long unixeDate = Long.valueOf(new String(mainDict.get("creation date")));
        return new Date(unixeDate * 1000);
    }

    public int DefineDownloadSize(){
        return downloadSize;
    }
}
