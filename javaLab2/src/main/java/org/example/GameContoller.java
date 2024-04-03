package org.example;

import org.example.enemy.EnemyAnimationStatus;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.CellStatus;
import org.example.viewer.Viewer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class GameContoller {
    private final int timeTosleep;
    private final double timeToSpawnEnemy;
    private double enemySpawnWait;

    private void SpawnEnemy(GameStat gameStat) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(enemySpawnWait <= 0){
            enemySpawnWait =  timeToSpawnEnemy;
            gameStat.CreateNewEnemy();
            enemySpawnWait = 1000;
        }
        else{
            enemySpawnWait -= timeTosleep;
        }
    }

    private void CheckPlayer(GameStat gameStat, Viewer viewer){
        Player player = gameStat.ReturnPlayer();
        if(!player.IsAlive()){
            viewer.CreateFinal();
        }
    }

    private void CheckEnemy(GameStat gameStat, Viewer viewer) throws IOException {
        ArrayList<IEnemy> enemyList =  new ArrayList<>(gameStat.ReturnEnemyList());
        for(IEnemy enemy : enemyList){
            enemy.ChangeSpriteTime(timeTosleep);
            if(enemy.Status() == EnemyAnimationStatus.Walk){
                Coords enemyCooeds = new Coords(enemy.CoordsX(), enemy.CoordsY());
                Cell curCell = gameStat.GetMap().GetCell(enemyCooeds);
                CellStatus curStatus = curCell.GetStatus();
                if(curStatus == CellStatus.NOT_IN_MAP){
                    Player player = gameStat.ReturnPlayer();
                    player.ChangeHp(enemy.Damage());
                    gameStat.DeleteEnemy(enemy);
                    CheckPlayer(gameStat, viewer);
                }
                else{
                    enemy.Move(curCell);
                }
            }
        }
    }
    private void StartMonitoring(GameStat gameStat, Viewer viewer, Sprite mapSprite) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        SpawnEnemy(gameStat);
        CheckEnemy(gameStat, viewer);
        viewer.CreateFieldStructur(mapSprite, gameStat.ReturnEnemyList(), gameStat.ReturnCharacterList());
    }
    public GameContoller(GameStat gameStat, Viewer viewer) throws IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        timeToSpawnEnemy = 1000;
        timeTosleep = 50;
        enemySpawnWait = 1000;
        viewer.Start(gameStat.GetMap(), gameStat.ReturnEnemyList(), gameStat.ReturnCharacterList());
        Sprite mapSprite = gameStat.GetMap().GetSprite();
        while (true){
            StartMonitoring(gameStat, viewer, mapSprite);
            Thread.sleep(timeTosleep);
        }
    }
}
