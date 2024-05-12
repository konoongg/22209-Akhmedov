package org.example;

import org.example.characters.CharacterAnimationStatus;
import org.example.characters.CreatingCharStatus;
import org.example.characters.ICharacter;
import org.example.enemy.EnemyAnimationStatus;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.CellStatus;
import org.example.map.GameMap;
import org.example.viewer.Viewer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class GameController {
    private int timeTosleep;
    private double timeToSpawnEnemy;
    private double enemySpawnWait;
    private GameStat gameStat;
    private Viewer viewer;
    private Timer timer;
    private int countSec;
    private ViewerListener listener;
    private boolean finalCrated;

    public void Restart() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        gameStat.Restart();
        countSec = 0;
        timeToSpawnEnemy = 1500;
        enemySpawnWait = timeToSpawnEnemy;
        viewer.Restart(gameStat);
        listener.ChangeStop();
        finalCrated = false;
    }

    public String CharPanelClick(String name, Coords conf) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        GameMap map = gameStat.ReturnMap();
        double x = conf.X() * map.GetSizeX();
        double y = conf.Y() * map.GetSizey();
        Cell startCell = map.GetCell(new Coords(x,y));
        CreatingCharStatus  status = gameStat.CreateNewCharacter(startCell, name);
        if(status == CreatingCharStatus.NOT_CHAR_SPAWN){
            return "can't create on this place";
        }
        else if(status == CreatingCharStatus.NO_MONEY){
            return "don't have money";
        }
        else if(status == CreatingCharStatus.CHAR_SPAWN){
            viewer.ChangeMoney(gameStat.ReturnPlayer().GetMoney());
            return "created " + name;
        }
        return "error";
    }

    private void SpawnEnemy() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(enemySpawnWait <= 0){
            gameStat.CreateNewEnemy();
            enemySpawnWait = timeToSpawnEnemy;
        }
        else{
            enemySpawnWait -= timeTosleep;
        }
    }

    private PlayerStatus CheckPlayer(){
        Player player = gameStat.ReturnPlayer();
        if(!player.IsAlive()){
            return PlayerStatus.DIE;
        }
        return PlayerStatus.OK;
    }

    private void CheckCahracters() throws IOException {
        ArrayList<ICharacter> characterList = new ArrayList<>(gameStat.ReturnCharacterList());
        ArrayList<IEnemy> enemyList =  new ArrayList<>(gameStat.ReturnEnemyList());
        for(ICharacter character: characterList){
            character.ChangeSpriteTime(timeTosleep);
            if(character.Status() == CharacterAnimationStatus.Wait){
                character.Wait(timeTosleep);
            }
            else if(character.Status() == CharacterAnimationStatus.Atack){
                character.UseSkill(enemyList, gameStat.ReturnMap().GetAllCell());
            }
        }
    }

    private void CheckEnemy() throws IOException {
        ArrayList<IEnemy> enemyList =  new ArrayList<>(gameStat.ReturnEnemyList());
        for(IEnemy enemy : enemyList){
            enemy.ChangeSpriteTime(timeTosleep);
            if(enemy.Status() == EnemyAnimationStatus.Walk){
                enemy.TimeProgress(countSec / 60);
                Coords enemyCooeds = new Coords(enemy.CoordsX(), enemy.CoordsY());
                Cell curCell = gameStat.ReturnMap().GetCell(enemyCooeds);
                CellStatus curStatus = curCell.GetStatus();
                if(curStatus == CellStatus.NOT_IN_MAP){
                    Player player = gameStat.ReturnPlayer();
                    player.ChangeHp(enemy.Damage());
                    gameStat.DeleteEnemy(enemy);
                    viewer.ChangeHp(player.GetHp());
                }
                else{
                    enemy.Move(curCell);
                }
            }
            else if(enemy.Status() == EnemyAnimationStatus.Died){
                Player player = gameStat.ReturnPlayer();
                player.ChangeMoney(enemy.Prize());
                viewer.ChangeMoney(player.GetMoney());
                gameStat.DeleteEnemy(enemy);
            }
        }
    }
    private void StartMonitoring() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        SpawnEnemy();
        CheckCahracters();
        CheckEnemy();
        viewer.RepaintMap(gameStat);
    }

    private void DefineSrr(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();
        int refreshRate = dm.getRefreshRate();
        timeTosleep = 1000 / refreshRate;
    }

    public GameController(GameStat gameStat, Viewer viewer) throws IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        countSec = 0;
        timeToSpawnEnemy = 1500;
        timer = new Timer();
        DefineSrr();
        enemySpawnWait = timeToSpawnEnemy;
        this.viewer = viewer;
        this.gameStat = gameStat;
        listener = new ViewerListener(this);
        viewer.Start(gameStat, listener);
        TimerTask TimeController = new TimerTask() {
            @Override
            public void run() {
                countSec++;
                viewer.ChangeTime(countSec / 3600, (countSec % 3600) / 60, countSec % 60);
                if(countSec % 60 == 0){
                    if(timeToSpawnEnemy > 500){
                        timeToSpawnEnemy -= 100;
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(TimeController, 1000, 1000);
        Sprite mapSprite = gameStat.ReturnMap().GetSprite();
        finalCrated = false;
        while (true){
            if(!listener.Stop()){
                StartMonitoring();
            }
            if(CheckPlayer() == PlayerStatus.DIE){
                if(!finalCrated){
                    viewer.CreateFinal();
                    finalCrated = true;
                }
            }
            Thread.sleep(timeTosleep);
        }
    }
}
