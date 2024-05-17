package org.example.connection.peer;

import org.example.connection.ConnectionLogic;
import org.example.connection.states.ConnectionStatusE;
import org.example.connection.states.MessageIdE;
import org.example.connection.states.PeerDownloadedE;
import org.example.exceptions.ReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class PeerTask {
    private static final Logger log = LoggerFactory.getLogger(PeerTask.class);
    private PeerDownloadedE downloaded;
    private String path;
    private int segmentId;
    private int segmentSize;
    private int curOffset;
    private int sizeBlock;
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
        curOffset = 0;
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

    public void LoadDataInBuf(byte[] partData) throws ReadException {
        if(curOffset + partData.length - 8 > segmentSize){
            log.error(" wrong data format: so big length");
            log.debug(" start LoadDaraInDuf curOffset: " + curOffset + " length data: " + partData.length + " segmentSize: " + segmentSize );
            throw new ReadException("wrong data format: so big length");
        }

        for(int i = 8; i < partData.length; ++i){
            segment[curOffset + i - 8] =  partData[i];
        }
        log.trace("local writed");
    }

    public void LoadNext(){
        curOffset += sizeBlock;
        downloaded = PeerDownloadedE.NOT_READY;
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
