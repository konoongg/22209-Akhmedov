package org.example.connection.peer;

import org.example.connection.ConnectionLogic;
import org.example.exceptions.ReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class PeerMessage {
    private int length;
    private int id;
    private byte[] mes;
    private static final Logger log = LoggerFactory.getLogger(PeerMessage.class);

    public void SetLength(ByteBuffer buffer) throws ReadException {
        buffer.flip();
        length = buffer.getInt();
        if(length <= 0){
            log.warn("length can't be less then 0");
            throw new ReadException("length can't be less then 0");
        }
        log.trace("length: " + length);
    }

    public void SetId(ByteBuffer buffer){
        buffer.flip();
        id = buffer.get() & 0xFF;
        log.trace("id: " + id);
    }

    public void SetMes(ByteBuffer buffer){
        log.trace("start safe mes");
        buffer.flip();
        mes = buffer.array();
    }

    public int GetLength(){
        log.trace("length: " + length);
        return length;
    }

    public int GetId(){
        return id;
    }

    public byte[] GetMes(){
        return mes;
    }
}
