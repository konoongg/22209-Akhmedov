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

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class GameController {
    private final int timeTosleep;
    private final double timeToSpawnEnemy;
    private double enemySpawnWait;
    GameStat gameStat;
    Viewer viewer;

    public String CharPanelClick(String name, Coords conf) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        GameMap map = gameStat.ReturnMap();
        double x = conf.X() * map.GetSizeX();
        double y = conf.Y() * map.GetSizey();
        Cell startCell = map.GetCell(new Coords(x,y));
        CreatingCharStatus  status = gameStat.CreateNewCharacter(startCell, name);
        if(status == CreatingCharStatus.NOT_CHAR_SPAWN){
            return "can't create on this plase";
        }
        else{
            return "created" + name;
        }
    }

    private void SpawnEnemy() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(enemySpawnWait <= 0){
            enemySpawnWait =  timeToSpawnEnemy;
            gameStat.CreateNewEnemy();
            enemySpawnWait = 5000;
        }
        else{
            enemySpawnWait -= timeTosleep;
        }
    }

    private void CheckPlayer(){
        Player player = gameStat.ReturnPlayer();
        if(!player.IsAlive()){
            viewer.CreateFinal();
        }
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
                character.UseSkill(enemyList);
            }
        }
    }

    private void CheckEnemy() throws IOException {
        ArrayList<IEnemy> enemyList =  new ArrayList<>(gameStat.ReturnEnemyList());
        for(IEnemy enemy : enemyList){
            enemy.ChangeSpriteTime(timeTosleep);
            if(enemy.Status() == EnemyAnimationStatus.Walk){
                Coords enemyCooeds = new Coords(enemy.CoordsX(), enemy.CoordsY());
                Cell curCell = gameStat.ReturnMap().GetCell(enemyCooeds);
                CellStatus curStatus = curCell.GetStatus();
                if(curStatus == CellStatus.NOT_IN_MAP){
                    Player player = gameStat.ReturnPlayer();
                    player.ChangeHp(enemy.Damage());
                    gameStat.DeleteEnemy(enemy);
                    CheckPlayer();
                }
                else{
                    enemy.Move(curCell);
                }
            }
            else if(enemy.Status() == EnemyAnimationStatus.Died){
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
    public GameController(GameStat gameStat, Viewer viewer) throws IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        timeToSpawnEnemy = 5000;
        timeTosleep = 50;
        enemySpawnWait = 5000;
        this.viewer = viewer;
        this.gameStat = gameStat;
        ViewerListener listener = new ViewerListener(this);
        viewer.Start(gameStat, listener);
        Sprite mapSprite = gameStat.ReturnMap().GetSprite();
        while (true){
            StartMonitoring();
            Thread.sleep(timeTosleep);
        }
    }
}
