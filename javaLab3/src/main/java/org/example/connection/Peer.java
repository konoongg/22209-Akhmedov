package org.example.connection;

import org.example.connection.states.ConnectionStatusE;
import org.example.connection.states.PeerStatusE;
import org.example.connection.states.ReadMesE;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.BitSet;

public class Peer {
    private InetAddress host;
    private int port;
    private PeerStatusE choked;
    private BitSet haveParts;
    private int neededByte;
    private ByteBuffer buffer;
    private ConnectionStatusE status;
    private PeerMessage message;

    public Peer(InetAddress host, int port, int countParts){
        this.host = host;
        choked = PeerStatusE.CHOKE;
        this.port = port;
        buffer = null;
        haveParts = new BitSet(countParts);
        status = ConnectionStatusE.CONNECTED;
        message = new PeerMessage();
    }

    public InetAddress GetHost(){
        return host;
    }

    public int GetPort(){
        return port;
    }

    public void Unchoke(){
        choked = PeerStatusE.UNCHOKE;
    }

    public void Choke(){
        choked = PeerStatusE.CHOKE;
    }

    public void SetParts(){
        byte[] parts = message.GetMes();
        int index = 0;
        for(byte part : parts){
            for(int i = 7; i >= 0; --i){
                int mask = 1 << i;
                boolean bit = (part & mask) != 0;
                haveParts.set(index, bit);
                index++;
            }
        }
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

    public void ChangeStatus(ConnectionStatusE newStatus){
        status = newStatus;
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

    public PeerMessage GetMessage(){
        return message;
    }

    public PeerStatusE peerStatus(){
        return choked;
    }
}
