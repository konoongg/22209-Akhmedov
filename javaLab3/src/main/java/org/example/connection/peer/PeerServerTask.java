package org.example.connection.peer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;

public class PeerServerTask {
    private int segmentId;
    private int offset;
    private int length;
    private byte[] data;
    private HashSet<Integer> needHave;
    private static final Logger log = LoggerFactory.getLogger(PeerServerTask.class);

    public void SetServerTask(byte[] args){
        ByteBuffer buffer = ByteBuffer.wrap(args).order(ByteOrder.BIG_ENDIAN);
        segmentId = buffer.getInt();
        offset = buffer.getInt();
        length = buffer.getInt();
    }

    public void SetData(byte[] data){
        this.data = data;
    }

    public byte[] GetData(){
        return data;
    }
    public int GetSegmentId(){
        return segmentId;
    }

    public int getLength() {
        return length;
    }

    public int GetOffset() {
        return offset;
    }

    public void SetNeedHave(HashSet<Integer> needHave){
        this.needHave = needHave;
    }

    public HashSet<Integer> GetNeedHave(){
        return needHave;
    }

    public boolean IsNeedHave(){
        if(needHave == null){
            return false;
        }
        if(needHave.size() == 0){
            return false;
        }
        return true;
    }

    public void ClearHave(){
        log.trace("SEND HAVE CLEAR");
        needHave = new HashSet<>();
    }
}
