package org.example;

import org.example.characters.ICharacter;
import org.example.enemy.EnemyFactory;
import org.example.enemy.IEnemy;
import org.example.map.GameMap;

import java.io.IOException;
import java.util.ArrayList;

public class GameStat {
    private GameMap gameMap;
    private EnemyFactory enemyFactory;
    private ArrayList<IEnemy> enemyList;

    private ArrayList<ICharacter> characterList;
    public GameStat (String config) throws IOException {
        gameMap = new GameMap(config);
        enemyFactory = new EnemyFactory();
        enemyList = new ArrayList<IEnemy>();
        characterList = new ArrayList<ICharacter>();
        ReadEnemyConfig();
    }

    private 

    public void CreateNewEnemy(){
        IEnemy enemy = enemyFactory.CreateEnemy();
        enemyList.add(enemy);
    }

    public void CreateNewCharacter(ICharacter character){
        characterList.add(character);
    }

    public ArrayList<ICharacter> CharacterList(){
        return characterList;
    }

    public ArrayList<IEnemy> EnemyList(){
        return enemyList;
    }
    public GameMap GetMap(){
        return gameMap;
    }
}
