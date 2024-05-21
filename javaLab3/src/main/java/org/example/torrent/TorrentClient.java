package org.example.torrent;

import org.example.exceptions.ServerCommunicateError;
import org.example.exceptions.WrongTorrentFileFormat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TorrentClient {
    private Map<String, String> params;
    private int port;
    private String peerId;
    private String infoHash;
    private final TorrentFile torrentFile;
    private byte[] infoHashB;
    private byte[] peerIdB;

    public TorrentClient(String torrentPath, String folderPath) throws ServerCommunicateError, WrongTorrentFileFormat {
        torrentFile = new TorrentFile(torrentPath, folderPath);
        CreatePeerId();
        CreateInfoHash(torrentFile);
        CreatePort();
        CreateParams(torrentFile);
    }

    private void CreatePort(){
        port = 20202;
    }

    private void CreatePeerId(){
        peerIdB = new byte[20];
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

    private void CreateParams(TorrentFile torrentFile){
        params = new HashMap<>();
        params.put("info_hash", infoHash);
        params.put("peer_id", peerId);
        params.put("port", Integer.toString(port));
        params.put("uploaded", "0");
        params.put("downloaded", "0");
        params.put("compact", "1");
        params.put("event", "started");
        params.put("left", Integer.toString(torrentFile.GetDownloadSize()));
    }

    private void CreateInfoHash(TorrentFile torrentFile) throws ServerCommunicateError {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] sha1 = digest.digest(torrentFile.GetInfo());
            infoHashB = Arrays.copyOf(sha1, sha1.length);;
            StringBuilder sb = new StringBuilder();
            for (byte b : sha1) {
                sb.append(String.format("%%%02x", b));
            }
            infoHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServerCommunicateError("cant' create sha-1 info hash");
        }
    }

    public Map<String, String> GetParams(){
        return params;
    }

    public TorrentFile GetTorrentFile(){
        return torrentFile;
    }

    public byte[] GetInfoHashB(){
        return  infoHashB;
    }

    public byte[] GetPeerIdB(){
        return peerIdB;
    }

    public int GetPort(){
        return port;
    }
}
