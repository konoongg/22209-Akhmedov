package org.example;

import org.example.viewer.Viewer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class GameContoller {
    private final int timeTosleep;
    private final double timeToSpawnEnemy;
    private double enemySpawnWait;
    private void StartMonitoring(GameStat gameStat, Viewer viewer, Sprite mapSprite) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(enemySpawnWait <= 0){
            enemySpawnWait =  timeToSpawnEnemy;
            gameStat.CreateNewEnemy();
        }
        else{
            enemySpawnWait -= timeTosleep;
        }
        viewer.CreateMap(mapSprite, gameStat.ReturnEnemyList(), gameStat.ReturnCharacterList());
    }
    public GameContoller(GameStat gameStat, Viewer viewer) throws IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        timeToSpawnEnemy = 1000;
        timeTosleep = 100;
        enemySpawnWait = 1000;
        viewer.start(gameStat.GetMap(), gameStat.ReturnEnemyList(), gameStat.ReturnCharacterList());
        Sprite mapSprite = gameStat.GetMap().GetSprite();
        while (true){
            StartMonitoring(gameStat, viewer, mapSprite);
            Thread.sleep(timeTosleep);
        }
    }
}
