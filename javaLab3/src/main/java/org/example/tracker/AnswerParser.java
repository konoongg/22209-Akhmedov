package org.example.tracker;

import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;
import org.example.connection.peer.Peer;
import org.example.torrent.TorrentParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnswerParser {

    private byte[] ReadTreckerAnswer(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toByteArray();
    }

    private byte[] AnswerParsing(HttpURLConnection connection) throws WrongTorrentFileFormat, ServerCommunicateError {
        byte[] responseBytes = null;
        try {
            responseBytes = ReadTreckerAnswer(connection.getInputStream());
        } catch (IOException e) {
            throw new ServerCommunicateError("can't parse server answer: " + e.getMessage());
        }
        responseBytes = Arrays.copyOfRange(responseBytes, 1, responseBytes.length - 1);
        Map<String, byte[]> answerDict = new HashMap<>();
        TorrentParser parser = new TorrentParser();
        parser.ParseText(answerDict, responseBytes);
        return answerDict.get("peers");
    }

    public ArrayList<Peer> PeersParsing(HttpURLConnection connection, int countParts) throws ServerCommunicateError, WrongTorrentFileFormat{
        ArrayList<Peer> ipPort = new ArrayList<>();
        byte[] peers = AnswerParsing(connection);
        int index = 0;
        while(index < peers.length){
            InetAddress host;
            int port;
            byte[] ipBytes = Arrays.copyOfRange(peers, index, index +4);
            try {
                host = InetAddress.getByAddress(ipBytes);
            } catch (UnknownHostException e) {
                throw new ServerCommunicateError("can'r get ip address peers");
            }
            index += 4;
            byte[] portBytes = Arrays.copyOfRange(peers, index, index + 2);
            port =  (portBytes[0] & 0xFF) << 8 | (portBytes[1] & 0xFF);;
            index += 2;
            Peer peer = new Peer(host, port, countParts);
            ipPort.add(peer);
        }
        return ipPort;
    }
}

