package org.example.file;

import java.util.ArrayList;

public class FileT {
    private String path;
    private int length;
    private SegmentManager segmentManager;

    public FileT(String path, int length, int countParts, int pieceLength){
        this.length = length;
        this.path = path;
        segmentManager = new SegmentManager(countParts, length, pieceLength);
    }

    public int GetLength(){
        return  length;
    }

    public SegmentManager GetSegmentManager(){
        return segmentManager;
    }

    public boolean IsDownloaded(){
        if(segmentManager.IsEmpty()){
            return true;
        }
        return false;
    }

    public String GetPath(){
        return path;
    }
}
