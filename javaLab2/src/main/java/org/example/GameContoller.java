package org.example;

import java.io.IOException;

public class GameContoller {
    public GameContoller(GameStat gameStat, Viewer viewer) throws IOException {
        viewer.start(gameStat.GetMap());
    }
}
