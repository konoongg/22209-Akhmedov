package org.example.file.save;

import org.example.exceptions.SaveDataException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SegmentChecker {
    private ArrayList<byte[]> segmentSha1 = new ArrayList<>();

    public SegmentChecker(ArrayList<byte[]> segmentSha1){
        this.segmentSha1 = segmentSha1;
    }

    public boolean CheckSegment(int segmentId, byte[] segment) throws SaveDataException {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] sha1 = digest.digest(segment);
            if(sha1 == segmentSha1.get(segmentId)){
                return true;
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new SaveDataException("can't check sha-1 segment");
        }
    }
}
