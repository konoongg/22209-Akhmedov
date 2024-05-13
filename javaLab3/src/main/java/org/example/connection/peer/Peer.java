package org.example.connection.peer;

import org.example.connection.states.PeerStatusE;

import java.net.InetAddress;

public class Peer {
    private InetAddress host;
    private int port;
    private PeerStatusE choked;
    private PeerDataContoller peerDataContoller;
    private PeerTask peerTask;

    public Peer(InetAddress host, int port, int countParts){
        this.host = host;
        choked = PeerStatusE.CHOKE;
        this.port = port;
        peerDataContoller = new PeerDataContoller(countParts);
    }

    public void SetTask(String path){
        peerTask = new PeerTask(path);
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

    public PeerDataContoller GetPeerDataCon(){
        return peerDataContoller;
    }

    public PeerTask GetTask(){return peerTask;}

    public PeerStatusE GetPeerStatus(){
        return choked;
    }
}
