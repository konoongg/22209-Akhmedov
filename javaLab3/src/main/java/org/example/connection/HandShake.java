package org.example.connection;

import org.example.exceptions.ConnectionError;
import org.example.torrent.TorrentClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class HandShake {
    TorrentClient torrent;


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


    public HandShackeStatusE CheckHandShacke(byte[] buffer){
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

    public void DoHandShake(SocketChannel channel, SelectionKey key) throws ConnectionError {
        try{
            byte[] hsMes = CreateHandShakeMes();
            ByteBuffer buffer = ByteBuffer.wrap(hsMes);
            while(!key.isWritable()){
                try{
                    System.out.println("Sleep");
                    Thread.sleep(100);
                }
                catch(InterruptedException e){
                    throw new ConnectionError("can't write: " + e.getMessage());
                }
            }
            channel.write(buffer);
        }
        catch (IOException e) {
            throw new ConnectionError("can't handShake: " +  e.getMessage());
        }
    }

    public HandShake(TorrentClient torrent){
        this.torrent = torrent;
    }
}
