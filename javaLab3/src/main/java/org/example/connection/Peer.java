package org.example.connection;

import java.net.InetAddress;
import java.util.BitSet;

public class Peer {
    private InetAddress host;
    private int port;
    private PeerStatusE choked;
    private BitSet haveParts;

    public Peer(InetAddress host, int port, int countParts){
        this.host = host;
        choked = PeerStatusE.CHOKE;
        this.port = port;
        haveParts = new BitSet(countParts);
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

    public void SetParts(byte[] parts){
        int index = 0;
        for(byte part : parts){
            for(int i =7; i >= 0; --i){
                int mask = 1 << i;
                boolean bit = (part & mask) != 0;
                haveParts.set(index, bit);
                index++;
            }
        }
    }

    public PeerStatusE peerStatus(){
        return choked;
    }
}
