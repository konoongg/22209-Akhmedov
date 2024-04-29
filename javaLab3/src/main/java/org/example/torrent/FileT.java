package org.example.torrent;

public class FileT {
    private String path;
    private int length;

    public FileT(String path, int length){
        this.length = length;
        this.path = path;
    }

    public int GetLength(){
        return  length;
    }

    public String GetPath(){
        return path;
    }
}
