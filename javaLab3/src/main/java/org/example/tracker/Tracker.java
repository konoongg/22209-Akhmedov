package org.example.tracker;

import org.example.peers.Peer;
import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;
import org.example.torrent.Torrent;
import org.example.torrent.TorrentParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Tracker {
    private String infoHash;
    private Torrent torrent;
    private String peerId;
    private int port = 20202;

    public Tracker(Torrent torrent) throws ServerCommunicateError {
        this.torrent = torrent;
        CreateInfoHash();
        CreatePeerId();
    }

    private void CreateInfoHash() throws ServerCommunicateError {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] sha1 = digest.digest(torrent.GetInfo());
            StringBuilder sb = new StringBuilder();
            for (byte b : sha1) {
                sb.append(String.format("%%%02x", b));
            }
            infoHash = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new ServerCommunicateError("cant' create sha-1 info hash");
        }
    }

    private void CreatePeerId(){
        byte[] peerIdB = new byte[20];
        peerIdB[0] = '-';
        peerIdB[1] = 'M';
        peerIdB[2] = 'T';
        peerIdB[3] = '0';
        peerIdB[4] = '0';
        peerIdB[5] = '0';
        peerIdB[6] = '1';
        peerIdB[7] = '-';
        byte[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        for(int i = 8; i < 20; ++i){
            peerIdB[i] = validChars[random.nextInt(10)];
        }
        peerId = new String(peerIdB, StandardCharsets.UTF_8);
    }

    private URL CreateUrl() throws ServerCommunicateError {
        Map<String, String> params = new HashMap<>();
        params.put("info_hash", infoHash);
        params.put("peer_id", peerId);
        params.put("port", Integer.toString(port));
        params.put("uploaded", "0");
        params.put("downloaded", "0");
        params.put("compact", "1");
        params.put("event", "started");
        params.put("left", Integer.toString(torrent.GetDownloadSize()));
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (query.length() > 0) {
                query.append("&");
            }
            try {
                if(entry.getKey() != "info_hash"){
                    query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    query.append("=");
                    query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                else{
                    query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    query.append("=");
                    query.append(entry.getValue());
                }
            } catch (UnsupportedEncodingException e) {
                throw new ServerCommunicateError("can't create url params for trecker");
            }
        }
        URL url = null;
        try {
            url = new URL(torrent.GetAnnounce() + "?" + query.toString());
        } catch (MalformedURLException e) {
            throw new ServerCommunicateError("can't created url with params");
        }
        return url;
    }

    private byte[] ReadTreckerAnswer(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toByteArray();
    }

    private byte[] AnswerParsing(HttpURLConnection connection) throws WrongTorrentFileFormat, IOException {
        byte[] responseBytes = ReadTreckerAnswer(connection.getInputStream());
        responseBytes = Arrays.copyOfRange(responseBytes, 1, responseBytes.length - 1);
        Map<String, byte[]>  answerDict = new HashMap<>();
        TorrentParser parser = new TorrentParser();
        parser.ParseText(answerDict, responseBytes);
        return answerDict.get("peers");
    }

    private void PeersParsing(ArrayList<Peer> ipPort, byte[] peers) throws ServerCommunicateError {
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
            Peer peer = new Peer(host, port);
            ipPort.add(peer);
        }
    }

    public ArrayList<Peer> GetPeers() throws ServerCommunicateError, IOException, WrongTorrentFileFormat {
        URL url = CreateUrl();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000); // 15 seconds timeout
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new ServerCommunicateError("can't conneckt with  trecker" + torrent.GetAnnounce() + "HTTP error code: " + responseCode);
        }
        byte[] peers = AnswerParsing(connection);
        ArrayList<Peer> ipPort = new ArrayList<>();
        PeersParsing(ipPort, peers);
        return ipPort;
    }
}
