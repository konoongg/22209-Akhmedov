package org.example.file;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SegmentManager {
    private HashSet<Integer> needDownload = new HashSet<>();
    private  HashSet<Integer>nowDownload = new HashSet<>();
    private  HashSet<Integer> downloaded = new HashSet<>();
    private int countSegment;
    private int length;
    private int pieceLength;

    public SegmentManager(int countParts, int length, int pieceLength){
        countSegment = countParts;
        this.pieceLength = pieceLength;
        this.length = length;
        for(int i = 0; i < countParts; ++i){
            needDownload.add(i);
        }
    }

    public boolean IsEmpty(){
        return needDownload.isEmpty() & nowDownload.isEmpty();
    }

    public int Segment(ArrayList<Integer> haveParts){
        HashSet<Integer> setNeed = new HashSet<>(needDownload);
        HashSet<Integer> setHave = new HashSet<>(haveParts);
        setNeed.retainAll(setHave);
        if(setNeed.isEmpty()){
            return -1;
        }
        else{
            Random random = new Random();
            Integer[]  intersection = setNeed.toArray(new Integer[0]);
            int randomIndex = random.nextInt(intersection.length);
            nowDownload.add(randomIndex);
            needDownload.remove(randomIndex);
            return randomIndex;
        }
    }

    public int SegmentSize(int segment){
        if(length % countSegment == 0){
            return length / countSegment;
        }
        else if(segment == countSegment - 1){
            return length % countSegment;
        }
        else{
            return pieceLength;
        }
    }

    public void CompliteDownload(int index){
        nowDownload.remove(index);
        downloaded.add(index);
    }

    public void CantDownload(int index){
        nowDownload.remove(index);
        needDownload.add(index);
    }
}
