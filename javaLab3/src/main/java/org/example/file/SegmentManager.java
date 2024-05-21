package org.example.file;

import org.example.exceptions.SelectionSegmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SegmentManager {
    private static final Logger log = LoggerFactory.getLogger(SegmentManager.class);
    private final HashSet<Integer> needDownload = new HashSet<>();
    private final HashSet<Integer> nowDownload = new HashSet<>();
    private final HashSet<Integer> downloaded = new HashSet<>();
    private final int countSegment;
    private final int length;
    private final int pieceLength;

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
        if(needDownload.isEmpty()){
            HashSet<Integer> setNow = new HashSet<>(nowDownload);
            HashSet<Integer> setHave = new HashSet<>(haveParts);
            setNow.retainAll(setHave);
            if(setNow.isEmpty()){
                return -1;
            }
            else{
                Random random = new Random();
                Integer[]  intersection = setNow.toArray(new Integer[0]);
                int randomIndex = random.nextInt(intersection.length);
                int randomElem = intersection[randomIndex];
                nowDownload.add(randomElem);
                needDownload.remove(randomElem);
                return randomElem;
            }
        }
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
            log.debug("this part yet downloaded: " + index);
        }
        if(!nowDownload.remove(index)){
            log.debug("this part not download: " + index);
        }
        log.debug("NEED DOWNLOAD: " + needDownload.size() + " NOW DOWNLOAD: " + nowDownload.size() + " DoWNLOAD: " + downloaded.size());
    }

    public void CantDownload(int index){
        nowDownload.remove(index);
        needDownload.add(index);
    }

    public int GetCountSegment(){
        return countSegment;
    }

    public HashSet<Integer> GetDownloadedParts(){
        return downloaded;
    }
}
