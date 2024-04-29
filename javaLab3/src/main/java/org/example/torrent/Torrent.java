package org.example.torrent;

import org.example.exceptions.WrongTorrentFileFormat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Torrent {
    private byte[] announce;
    private String name;
    private String comment;
    private String createdBy;
    private Date createdDate;
    private ArrayList<byte[]> segmentSha1 = new ArrayList<>();
    private ArrayList<FileT> files = new ArrayList<>();
    private byte[] info;
    private int downloadSize;

    private void  DefineFields(Map<String, byte[]>  mainDict, Map<String, byte[]>  infoDict, String folderPath) throws WrongTorrentFileFormat {
        Definer definer = new Definer();
        announce = definer.DefineAnnounce(mainDict);
        definer.DefineSegments(infoDict, segmentSha1);
        definer.DefineFiles(infoDict, folderPath, files);
        name = definer.DefineName(infoDict);
        comment = definer.DefineComment(mainDict);
        createdBy = definer.DefineCreatedBy(mainDict);
        createdDate = definer.DefineCreatedDate(mainDict);
        downloadSize = definer.DefineDownloadSize();
    }

    public Torrent(String torrentPath, String folderPath) throws WrongTorrentFileFormat {
        Map<String, byte[]>  mainDict = new HashMap<>();
        Map<String, byte[]>  infoDict = new HashMap<>();
        TorrentParser parser = new TorrentParser(mainDict, torrentPath);
        if(!mainDict.containsKey("info")){
            throw new WrongTorrentFileFormat("torrent file don't have field info");
        }
        parser.ParseText(infoDict, mainDict.get("info"));
        info = mainDict.get("info");

        String filePath = "example.txt"; // Путь к файлу, в который мы хотим записать массив байт
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(info); // Записываем массив байт в файл
            System.out.println("Массив байт успешно записан в файл.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }

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

    public byte[] GetInfo(){
        return info;
    }

    public int GetDownloadSize(){
        return downloadSize;
    }

    public String GetAnnounce() {
        return new String(announce);
    }
}
