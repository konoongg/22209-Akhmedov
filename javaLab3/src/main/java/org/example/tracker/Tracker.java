package org.example.tracker;

import org.example.torrent.TorrentClient;
import org.example.connection.peer.Peer;
import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tracker {
    private URL CreateUrl(TorrentClient torrent) throws ServerCommunicateError {
        Map<String, String> params = torrent.GetParams();
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (query.length() > 0) {
                query.append("&");
            }
            if(!Objects.equals(entry.getKey(), "info_hash")){
                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                query.append("=");
                query.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
            else{
                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                query.append("=");
                query.append(entry.getValue());
            }
        }
        URL url = null;
        try {
            url = new URL(torrent.GetTorrentFile().GetAnnounce() + "?" + query.toString());
        } catch (MalformedURLException e) {
            throw new ServerCommunicateError("can't created url with params");
        }
        return url;
    }

    private String DefineFullPath(String path, String name){
        String fullPath = path;
        if(path.charAt(path.length() - 1) == '/'){
            fullPath += name;
        }
        else{
            fullPath += "/" + name;
        }
        return fullPath;
    }

    private void  SetPeerFile(ArrayList<Peer> peers, String path, String name ){
        String fullPath = DefineFullPath(path, name);
        for(Peer peer : peers){
            peer.SetTask(fullPath);
        }
    }

    public ArrayList<Peer> GetPeers(TorrentClient torrent, String folderPath) throws ServerCommunicateError, IOException, WrongTorrentFileFormat {
        URL url = CreateUrl(torrent);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000); // 15 seconds timeout
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new ServerCommunicateError("can't connect with  tracker" + torrent.GetTorrentFile().GetAnnounce() + "HTTP error code: " + responseCode);
        }
        AnswerParser trackerAnswerParser = new AnswerParser();
        ArrayList<Peer> peers =  trackerAnswerParser.PeersParsing(connection, torrent.GetTorrentFile().GetCountParts());
        SetPeerFile(peers, folderPath, torrent.GetTorrentFile().GetName());
        return peers;
    }
}
