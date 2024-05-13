package org.example.file;

import org.example.exceptions.SaveDataException;
import org.example.exceptions.SelectionSegmentException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SegmentManager {
    private HashSet<Integer> needDownload = new HashSet<>();
    private HashSet<Integer> nowDownload = new HashSet<>();
    private HashSet<Integer> downloaded = new HashSet<>();
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

    public int Segment(ArrayList<Integer> haveParts) throws SelectionSegmentException {
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
            int randomElem = intersection[randomIndex];
            nowDownload.add(randomElem);
            needDownload.remove(randomElem);
            return randomElem;
        }
    }

    public int SegmentSize(int segment){
        if(length % countSegment == 0){
            return length / countSegment;
        }
        else if(segment == countSegment - 1){
            return length % pieceLength;
        }
        else{
            return pieceLength;
        }
    }

    public void CompliteDownload(int index) throws SelectionSegmentException {
        if(!downloaded.add(index)){
            System.out.println("this part yet downloaded: " + index);
            throw new SelectionSegmentException("this part yet downloaded: " + index);
        }
        if(!nowDownload.remove(index)){
            System.out.println("this part not download: " + index);
            throw new SelectionSegmentException("this part not download: " + index);
        }
        System.out.println("NEED DOWNLOAD: " + needDownload.size() + " NOW DOWNLOAD: " + nowDownload.size() + " DoWNLOAD: " + downloaded.size());
    }

    public void CantDownload(int index){
        nowDownload.remove(index);
        needDownload.add(index);
    }
}
