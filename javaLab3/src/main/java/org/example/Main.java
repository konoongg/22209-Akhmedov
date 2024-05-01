package org.example;

public class Main {
    public static void main(String[] args)  {
        String torrentPath = args[0];
        String folderPath = args[1];
        try{
            TorrentController torrentController = new TorrentController(torrentPath, folderPath);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}