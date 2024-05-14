package org.example.connection.peer;

import org.example.connection.states.ConnectionStatusE;
import org.example.exceptions.ReadException;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PeerDataContoller {
    private PeerMessage message;
    private ArrayList<Integer> haveParts = new ArrayList<>();
    private int countParts;
    private ByteBuffer buffer;
    private ConnectionStatusE status;
    private int neededByte;
    private int UnSuccessful;

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

    public void SetParts(){
        byte[] parts = message.GetMes();
        int index = 0;
        for(byte part : parts){
            for(int i = 7; i >= 0; --i){
                int mask = 1 << i;
                boolean bit = (part & mask) != 0;
                haveParts.add(index);
                index++;
                if(index >= countParts){
                    return;
                }
            }
        }
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

    public void SaveBuffer(){
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

    public void  BufferDisconnect(){
        buffer = null;
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
}
