package org.example.connection;

import java.nio.ByteBuffer;

public class PeerMessage {
    private int length;
    private int id;
    private byte[] mes;

    public void SetLength(ByteBuffer buffer){
        buffer.flip();
        length = buffer.getInt();
    }

    public void SetId(ByteBuffer buffer){
        buffer.flip();
        id = buffer.get() & 0xFF;
    }

    public void SetMes(ByteBuffer buffer){
        buffer.flip();
        mes = buffer.array();
    }

    public int GetLength(){
        return length;
    }

    public int GetId(){
        return id;
    }

    public byte[] GetMes(){
        return mes;
    }
}
