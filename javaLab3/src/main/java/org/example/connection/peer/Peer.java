package org.example.connection.peer;

import org.example.connection.states.PeerStatusE;

import java.net.InetAddress;

public class Peer {
    private InetAddress host;
    private int port;
    private PeerStatusE choked;
    private PeerDataContoller peerDataContoller;
    private PeerTask peerTask;
    private PeerServerTask peerServerTask;
    private boolean serverPeer;

    public Peer(InetAddress host, int port, int countParts){
        serverPeer = false;
        this.host = host;
        choked = PeerStatusE.CHOKE;
        this.port = port;
        peerDataContoller = new PeerDataContoller(countParts);
        peerServerTask = new PeerServerTask();
    }

    public void SetTask(String path){
        peerTask = new PeerTask(path);
    }

    public void SetServerTask(byte[] args){
        peerServerTask.SetServerTask(args);
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

    public PeerServerTask GetServerTask(){return  peerServerTask;}

    public PeerStatusE GetPeerStatus(){
        return choked;
    }

    public void  MadeItServer(){
        serverPeer = true;
    }

    public boolean IsItServer(){
        return serverPeer;
    }
}
