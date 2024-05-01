package org.example.tracker;

import org.example.torrent.TorrentClient;
import org.example.connection.Peer;
import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;
import org.example.torrent.TorrentFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

public class Tracker {
    private TorrentFile torrent;

    private URL CreateUrl(TorrentClient torrent) throws ServerCommunicateError {
        Map<String, String> params = torrent.GetParams();
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
            url = new URL(torrent.GetTorrentFile().GetAnnounce() + "?" + query.toString());
        } catch (MalformedURLException e) {
            throw new ServerCommunicateError("can't created url with params");
        }
        return url;
    }

    public ArrayList<Peer> GetPeers(TorrentClient torrent) throws ServerCommunicateError, IOException, WrongTorrentFileFormat {
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

        return peers;
    }
}
