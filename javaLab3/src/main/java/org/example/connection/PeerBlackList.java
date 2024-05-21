package org.example.connection;

import java.util.HashMap;
import java.util.Map;

public class PeerBlackList {
    private final Map<String, Integer> blackList= new HashMap<>();
    private final int maxCountDisconnect = 10;

    public void Disconnect(String key){
        if(blackList.containsKey(key)){
            int count = blackList.get(key);
            blackList.put(key, count + 1);
        }
        else{
            blackList.put(key, 1);
        }
    }

    public boolean IsBlock(String key){
        if(blackList.containsKey(key)){
            int count = blackList.get(key);
            return count >= maxCountDisconnect;
        }
        return false;
    }
}
