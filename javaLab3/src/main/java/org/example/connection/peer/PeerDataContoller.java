package org.example.connection.peer;

import org.example.connection.states.ConnectionStatusE;
import org.example.exceptions.ReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;

public class PeerDataContoller {
    private PeerMessage message;
    private ArrayList<Integer> haveParts = new ArrayList<>();
    private int countParts;
    private ByteBuffer buffer;
    private ConnectionStatusE status;
    private int neededByte;
    private int UnSuccessful;
    private static final Logger log = LoggerFactory.getLogger(PeerDataContoller.class);

    public PeerDataContoller(int countParts){
        message = new PeerMessage();
        haveParts = new ArrayList<>(countParts);
        this.countParts = countParts;
        buffer = null;
        UnSuccessful = 0;
        status = ConnectionStatusE.CONNECTED;
    }

    public void BufferConnect(int size){
        if(buffer == null){
            neededByte = size;
            buffer  = ByteBuffer.allocate(size);
        }
    }

    public void  BufferDisconnect(){
        buffer = null;
    }

    public void SetParts(){
        byte[] parts = message.GetMes();
        int index = 0;
        for(byte part : parts){
            for(int i = 7; i >= 0; --i){
                int mask = 1 << i;
                boolean bit = (part & mask) != 0;
                if(bit){
                    haveParts.add(index);
                }
                index++;
                if(index >= countParts){
                    return;
                }
            }
        }
    }

    public void SetParts(HashSet<Integer> donloaded){
        haveParts.addAll(donloaded);
    }

    public void UpdateParts(HashSet<Integer> donloaded, PeerServerTask peerServerTask){
        HashSet<Integer> havePartsSet = new HashSet<>(haveParts);
        HashSet<Integer> downloadedSet = new HashSet<>(donloaded);
        downloadedSet.removeIf(havePartsSet::contains);
        haveParts.addAll(downloadedSet);
        if(downloadedSet.size() != 0){
            log.trace("SEND HAVE UPDATE: " + downloadedSet);
        }
        peerServerTask.SetNeedHave(downloadedSet);
    }

    public void UnSuccessfulReading(){
        UnSuccessful++;
    }

    public void SuccessfulReading(){
        UnSuccessful = 0;
    }

    public void ApplyHave() throws ReadException {
        byte[] indexB = message.GetMes();
        ByteBuffer buffer = ByteBuffer.wrap(indexB);
        int index = buffer.getInt();
        if(index >= countParts){
            throw new ReadException("uncorrect have index");
        }
        haveParts.add(index);

    }

    public void SaveBuffer() throws ReadException {
        if(status == ConnectionStatusE.LISTENER_LENGTH){
            message.SetLength(buffer);
        }
        else if(status == ConnectionStatusE.LISTENER_ID){
            message.SetId(buffer);
        }
        else if(status == ConnectionStatusE.LISTENER_DATA){
            message.SetMes(buffer);
        }
    }

    public ArrayList<Integer> GetParts(){
        return haveParts;
    }

    public PeerMessage GetMessage(){
        return message;
    }

    public ByteBuffer GetBuffer(){
        return buffer;
    }

    public void ReadByte(int count){
        neededByte -= count;
    }

    public int GetNeededWrite(){
        return neededByte;
    }

    public ConnectionStatusE GetStatus(){
        return status;
    }

    public int GetUnSuccessful(){
        return UnSuccessful;
    }

    public void ChangeStatus(ConnectionStatusE newStatus){
        status = newStatus;
    }

    public ArrayList<Integer> GetHaveParts(){
        return haveParts;
    }
}
