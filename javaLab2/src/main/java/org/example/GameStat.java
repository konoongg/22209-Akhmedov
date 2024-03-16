package org.example;

import org.example.map.GameMap;

import java.io.IOException;

public class GameStat {
    private GameMap gameMap;
    public GameStat (String config) throws IOException {
        gameMap = new GameMap(config);
    }
    public GameMap GetMap(){
        return gameMap;
    }
}
