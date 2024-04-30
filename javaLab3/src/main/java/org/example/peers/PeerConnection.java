package org.example.peers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PeerConnection implements Runnable{
    private Peer peer;

    public PeerConnection(Peer peer){
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(peer.GetHost(), peer.GetPort()), 5000);
            System.out.println("connection with " + peer.GetHost() + ":" + peer.GetPort());
            socket.close();
        } catch (IOException e) {
            System.out.println("can't connection with " + peer.GetHost() + ":" + peer.GetPort());
        }
    }
}
