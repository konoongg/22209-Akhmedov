package org.example.connection;

import org.example.exceptions.ConnectionError;
import org.example.torrent.TorrentClient;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PeerConnection implements Runnable{
    private Peer peer;
    private TorrentClient torrent;
    private InputStream inputStream;
    private OutputStream outputStream;

    public PeerConnection(Peer peer, TorrentClient torrent){
        this.peer = peer;
        this.torrent = torrent;
    }

    private byte[] CreateHandShakeMes(){
        byte[] mes = new byte[68];
        int index = 0;
        mes[index] =  19;
        index++;
        byte[] btp = new byte[]{'B', 'i', 't', 'T', 'o', 'r', 'r', 'e', 'n', 't', ' ', 'p', 'r', 'o', 't', 'o', 'c', 'o', 'l'};
        for(int i = 0; i < 19; ++i){
            mes[index] = (btp[i]);
            index++;
        }
        for(int i = 0; i < 8; ++i){
            mes[index] = 0;
            index++;
        }
        byte[] infoHashB = torrent.GetInfoHashB();
        for(int i = 0; i < 20; ++i){
            mes[index] = infoHashB[i];
            index++;
        }
        byte[] peerIdB = torrent.GetPeerIdB();
        for(int i = 0; i < 20; ++i){
            mes[index] = peerIdB[i];
        }
        return mes;
    }

    private HandShackeStatusE CheckHandShacke(byte[] buffer){
        int index = 0;
        if(buffer[index] != 19){
            return HandShackeStatusE.NOT_SUCCESSFUL;
        }
        index++;
        byte[] btp = new byte[]{'B', 'i', 't', 'T', 'o', 'r', 'r', 'e', 'n', 't', ' ', 'p', 'r', 'o', 't', 'o', 'c', 'o', 'l'};
        for(int i = 0; i < 19; ++i){
            if (buffer[index] != btp[i]){
                return HandShackeStatusE.NOT_SUCCESSFUL;
            }
            index++;
        }
        index += 8;
        byte[] infoHashB = torrent.GetInfoHashB();
        for(int i = 0; i < 20; ++i){
            if(buffer[index] != infoHashB[i]){
                return HandShackeStatusE.NOT_SUCCESSFUL;
            }
            index++;
        }
        return  HandShackeStatusE.SUCCESSFUL;
    }

    private void DoHandShake() throws ConnectionError {
        try {
            byte[] hsMes = CreateHandShakeMes();
            outputStream.write(hsMes);
            byte[] buffer = new byte[68];
            inputStream.read(buffer);
            if(CheckHandShacke(buffer) == HandShackeStatusE.NOT_SUCCESSFUL){
                throw  new ConnectionError("wrong hand shacke answer " + peer.GetHost() + ":" + peer.GetPort());
            }
            else{
                System.out.println("correct hand Shacke" + peer.GetHost() + ":" + peer.GetPort());
            }
        } catch (IOException e) {
            throw new ConnectionError("can't handShake with " + peer.GetHost() + ":" + peer.GetPort());
        }
    }

    private void SendInterested() throws ConnectionError {
        byte[] interested = new byte[5];
        interested[0] = 0;
        interested[1] = 0;
        interested[2] = 0;
        interested[3] = 1;
        interested[4] = 2;
        try {
            outputStream.write(interested);
        } catch (IOException e) {
            throw new ConnectionError("can't send interested: " + e.getMessage());
        }
    }

    private void DifineStatus(byte status, byte[] data) throws ConnectionError {
        if(status == MessageIdE.BITFIELD.getValue()){
            SendInterested();
            peer.SetParts(data);
        }
    }

    private void Read() throws ConnectionError {
        byte[] lenghtB = new byte[4];
        byte[] statusB = new byte[1];
        while(true){
            try {
                inputStream.read(lenghtB);
                inputStream.read(statusB);
                ByteBuffer buffer = ByteBuffer.wrap(lenghtB);
                buffer.order(ByteOrder.BIG_ENDIAN);
                System.out.println(lenghtB[0] + " " +  lenghtB[1] + " " + lenghtB[2] + " " + lenghtB[3]);
                int length = buffer.getInt();
                if(length < 0){
                    continue;
                }
                byte status = statusB[0];
                System.out.println("STATUS: " + status + " " +  peer.GetHost() + ":" + peer.GetPort() + " " +length);
                byte[] data = new byte[length - 1];
                DifineStatus(status, data);
                inputStream.read(data);
            } catch (IOException e) {
                throw new ConnectionError("can't read data " + peer.GetHost() + ":" + peer.GetPort());
            }
        }
    }

    @Override
    public void run() {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(peer.GetHost(), peer.GetPort()), 5000);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            System.out.println("connection with " + peer.GetHost() + ":" + peer.GetPort());
            DoHandShake();
            Read();
        } catch (IOException e) {
            System.out.println("can't connection with " + peer.GetHost() + ":" + peer.GetPort());
        } catch (ConnectionError e) {
            System.out.println(e.getMessage());
        }
    }
}
