package org.example;

import org.example.map.GameMap;

import java.io.IOException;

public class GameStat {
    GameMap gameMap;

    public GameStat (String config) throws IOException {
        gameMap = new GameMap(config);
    }
}
