package org.example;

import org.example.characters.CharacterFactory;
import org.example.characters.ICharacter;
import org.example.enemy.EnemyFactory;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.GameMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class GameStat {
    private final GameMap gameMap;
    private final EnemyFactory enemyFactory;
    private final CharacterFactory characterFactory;
    private final ArrayList<IEnemy> enemyList;
    private final ArrayList<ICharacter> characterList;
    public GameStat (String config) throws IOException {
        gameMap = new GameMap(config);
        enemyFactory = new EnemyFactory();
        characterFactory = new CharacterFactory();
        enemyList = new ArrayList<>();
        characterList = new ArrayList<>();
    }

    private Coords GetRandomCoords(){
        Random random = new Random();
        ArrayList<Coords> enemySpawn = gameMap.GetEnemySpawn();
        int randomIndex = random.nextInt(enemySpawn.size());
        return enemySpawn.get(randomIndex);
    }

    public void CreateNewEnemy() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Coords enemyCords = GetRandomCoords();
        int cellSize = gameMap.GetCellSize();
        Cell startCell = new Cell(enemyCords.X() / cellSize, enemyCords.Y() / cellSize, cellSize, );
        IEnemy enemy = enemyFactory.CreateRandomEnemy(enemyCords);
        enemyList.add(enemy);
    }

    public void CreateNewCharacter(Coords characterCoords, String name) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ICharacter character = characterFactory.CreateCharacter(name, characterCoords);
        characterList.add(character);
    }
    public void CreateNewCharacter(ICharacter character){
        characterList.add(character);
    }

    public ArrayList<ICharacter> ReturnCharacterList(){
        return characterList;
    }

    public ArrayList<IEnemy> ReturnEnemyList(){
        return enemyList;
    }
    public GameMap GetMap(){
        return gameMap;
    }
}
