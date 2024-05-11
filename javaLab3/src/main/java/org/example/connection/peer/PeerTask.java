package org.example.connection.peer;

import org.example.connection.states.ConnectionStatusE;
import org.example.connection.states.MessageIdE;
import org.example.connection.states.PeerDownloadedE;

import java.nio.ByteBuffer;

public class PeerTask {
    private PeerDownloadedE downloaded;
    private String path;
    private int segmentId;
    private int segmentSize;
    private int curOffset;
    private int sizeBlock;
    private int index;
    private byte[] segment;


    public PeerTask(String path){
        downloaded = PeerDownloadedE.START;
        sizeBlock = 14 * 1024;
        segmentSize = sizeBlock * 2;
        this.path = path;
    }

    public void SetTask(int segmentId, int segmentSize){
        this.segmentId = segmentId;
        this.segmentSize = segmentSize;
        segment = new byte[segmentSize];
        downloaded = PeerDownloadedE.NOT_READY;
    }

    public int GetSegmentId(){
        return segmentId;
    }

    public byte[] GetSegment(){
        return segment;
    }

    public int GetOffset(){
        return curOffset;
    }

    public int GetSizeBlock(){
        if(curOffset + sizeBlock <= segmentSize){
            return  sizeBlock;
        }
        return segmentSize - curOffset;
    }

    public void LoadDataInBuf(byte[] partData){
        for(int i = 0; i < partData.length; ++i){
            segment[curOffset + i] =  partData[i];
        }
        System.out.println("local writed");
    }

    public void LoadNext(){
        curOffset += sizeBlock;
    }

    public PeerDownloadedE Downloaded(){
        return downloaded;
    }

    public void Ready(MessageIdE id){
        if(id == MessageIdE.UNCHOKE){
            downloaded = PeerDownloadedE.NEED_WORK;
        }
        else if (curOffset + sizeBlock < segmentSize){
            downloaded = PeerDownloadedE.READY_PART;
        }
        else{
            downloaded = PeerDownloadedE.READY_All;
        }
    }
}
