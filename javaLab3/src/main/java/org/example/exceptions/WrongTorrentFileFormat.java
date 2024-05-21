package org.example.exceptions;

public class WrongTorrentFileFormat extends Exception{
    public WrongTorrentFileFormat(String message) {
        super(message);
    }
}
