package org.example.tracker;

import org.example.exceptions.ServerCommunicateError;
import org.example.torrent.Torrent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
            infoHash = new String(digest.digest(torrent.GetInfo()), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
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
                query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                query.append("=");
                query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
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
        System.out.println(url);
        return url;
    }

    public void GetPeers() throws ServerCommunicateError, IOException {
        URL url = CreateUrl();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000); // 15 seconds timeout
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new ServerCommunicateError("can't conneckt with  trecker" + torrent.GetAnnounce() + "HTTP error code: " + responseCode);
        }
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }
}
