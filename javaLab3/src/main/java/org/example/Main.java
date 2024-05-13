package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)  {
        log.trace("start program");
        log.error("start program");
        String torrentPath = args[0];
        String folderPath = args[1];
        try{
            TorrentController torrentController = new TorrentController(torrentPath, folderPath);
        }
        catch (Exception e){
            log.error(e + e.getMessage());
        }
        log.trace("end program");
    }
}