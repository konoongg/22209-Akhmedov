package org.example.connection;

import org.example.connection.states.HandShackeStatusE;
import org.example.connection.states.MessageIdE;
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


    private byte[] CreateReqMes(){
        byte[] mes = new byte[13];
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

    //java.neo socket chanel

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


    private void SendReq(int segment, int offset) throws ConnectionError {
        int sizeBlock = 16 * 1024;
        byte[] reqv = new byte[17];
        reqv[0] = 0;
        reqv[1] = 0;
        reqv[2] = 0;
        reqv[3] = 13;
        //reqv[4] = MessageIdE.REQUEST.getValue();
        int curIndex = 5;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()){
            DataOutputStream out = new DataOutputStream(byteStream);
            out.writeInt(segment);
            out.writeInt(offset);
            out.writeInt(sizeBlock);
            byte[] bytes = byteStream.toByteArray();
            for(int i = 0; i < 12; ++i){
                reqv[curIndex] = bytes[i];
                curIndex++;
            }
            outputStream.write(reqv);
        } catch (IOException e) {
            throw new ConnectionError("can't send interested: " + e.getMessage());
        }
    }

    private void DifineId(byte id, byte[] data) throws ConnectionError {
        if(id == MessageIdE.BITFIELD.getValue()){
            SendInterested();
            //peer.SetParts(data);
        }
        else if(id == MessageIdE.UNCHOKE.getValue()){
            peer.Unchoke();
        }
        else if(id == MessageIdE.CHOKE.getValue()){
            peer.Choke();
        }
        else{
            //throw new ConnectionError("id message is uncorrected:  " + id);
        }
    }

    private byte ListenPeer() throws ConnectionError {
        byte[] lenghtB = new byte[4];
        byte[] idB = new byte[1];
        int readStatus;
        try {
            readStatus = inputStream.readNBytes(lenghtB, 0, 4);
            if(readStatus < 4){
                return -1;
            }
            readStatus =  inputStream.readNBytes(idB, 0, 1);
            if(readStatus < 1 ){
                return -1;
            }
            ByteBuffer buffer = ByteBuffer.wrap(lenghtB);
            buffer.order(ByteOrder.BIG_ENDIAN);
            int length = buffer.getInt();
            byte id = idB[0];
            System.out.println("ID: " + id + " " +  peer.GetHost() + ":" + peer.GetPort() + " " +length);
            if(length <= 0){
                return -1;
            }
            byte[] data = new byte[length - 1];
            readStatus = inputStream.readNBytes(data, 0, length - 1);
            if(readStatus < length - 1){
                return -1;
            }
            DifineId(id, data);
            return id;
        }
        catch (IOException e) {
            throw new ConnectionError("can't read data " + peer.GetHost() + ":" + peer.GetPort());
        }
    }

    private void WaitUnchoke() throws ConnectionError {
        boolean flag = false;
        while(true){
            byte id = ListenPeer();
            if(id == MessageIdE.UNCHOKE.getValue() || flag){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                SendReq(0,0);
                flag = true;
                //return;
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
            WaitUnchoke();
        } catch (IOException e) {
            System.out.println("can't connection with " + peer.GetHost() + ":" + peer.GetPort());
        } catch (ConnectionError e) {
            System.out.println(e.getMessage());
        }
    }
}
