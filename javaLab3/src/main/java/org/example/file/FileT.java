package org.example.file;

import org.example.exceptions.ReadDataFromFileException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileT {
    private final String path;
    private final int length;
    private final SegmentManager segmentManager;

    public FileT(String path, int length, int countParts, int pieceLength, String fileName){
        this.length = length;
        this.path = path + "/" + fileName;
        segmentManager = new SegmentManager(countParts, length, pieceLength);
    }

    public int GetLength(){
        return  length;
    }

    public SegmentManager GetSegmentManager(){
        return segmentManager;
    }

    public boolean IsDownloaded(){
        return segmentManager.IsEmpty();
    }

    public byte[] GetData(int segmentId, int offset, int length) throws ReadDataFromFileException {
        int segmentSize = segmentManager.SegmentSize(0);
        int realOffset = segmentId * segmentSize + offset;
        try (RandomAccessFile raf = new RandomAccessFile(new File(path), "r")) {
            raf.seek(realOffset);
            byte[] data = new byte[length];
            raf.read(data);
            return data;
        } catch (IOException e) {
            throw new ReadDataFromFileException("can't read data from:" + path);
        }
    }
}
