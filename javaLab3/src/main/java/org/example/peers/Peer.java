package org.example.peers;

import java.net.InetAddress;

public class Peer {
    private InetAddress host;
    private int port;

    public Peer(InetAddress host, int port){
        this.host = host;
        this.port = port;
    }

    public InetAddress GetHost(){
        return host;
    }

    public int GetPort(){
        return port;
    }
}
