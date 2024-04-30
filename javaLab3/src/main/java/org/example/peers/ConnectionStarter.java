package org.example.peers;

import java.util.ArrayList;

public class ConnectionStarter {
    public ConnectionStarter(ArrayList<Peer> peers){
        for(Peer peer : peers){
            Thread thread = new Thread(new PeerConnection(peer));
            thread.start();
        }
    }
}
