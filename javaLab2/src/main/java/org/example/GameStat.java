package org.example;

import org.example.animation.AnimationReader;
import org.example.characters.*;
import org.example.enemy.EnemyFactory;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.CellStatus;
import org.example.map.GameMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class GameStat {
    private final GameMap gameMap;
    private final Player player;
    private final EnemyFactory enemyFactory;
    private final CharacterFactory characterFactory;
    private ArrayList<IEnemy> enemyList;
    private ArrayList<ICharacter> characterList;
    private final Map<String, CharactersParams> unicCharacters;

    public GameStat (String config) throws IOException {
        player = new Player(1000, 200);
        gameMap = new GameMap(config);
        enemyFactory = new EnemyFactory();
        characterFactory = new CharacterFactory();
        unicCharacters = new HashMap<>();
        ReadCharacters();
        enemyList = new ArrayList<>();
        characterList = new ArrayList<>();
    }

    public void Restart(){
        enemyList = new ArrayList<>();
        characterList = new ArrayList<>();
        player.Restart(1000, 200);
        gameMap.Restart();
    }

    private void ReadCharacters() throws IOException {
        String config = "/charactersStats.txt";
        CharactersReader charactersReader = new CharactersReader(config, unicCharacters);
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
        IEnemy enemy = enemyFactory.CreateRandomEnemy(enemyCords);
        enemyList.add(enemy);
    }

    public void DeleteEnemy(IEnemy enemy){
        enemyList.remove(enemy);
    }

    private  boolean ISItSuitableCell(Cell startCell, String name, CharactersParams params){
        int sizeCellX = params.GetSizeCellX();
        int sizeCellY = params.GetSizeCellX();
        int cellSize = gameMap.GetCellSize();
        Coords choosedCellCoords = startCell.GetStartCoords();
        double choosedCellX = choosedCellCoords.X();
        double choosedCellY = choosedCellCoords.Y();
        for(int i = 0; i < sizeCellY; ++i){
            for(int j = 0; j < sizeCellX; ++j){
                double cellX = choosedCellX + j * cellSize;
                double cellY = choosedCellY + i * cellSize;
                Cell cell = gameMap.GetCell(new Coords(cellX, cellY));
                if(cell.GetStatus() != CellStatus.FREE){
                    return false;
                }
            }
        }
        return true;
    }
    private void BorrowCells(Cell startCell, CharactersParams params){
        int sizeCellX = params.GetSizeCellX();
        int sizeCellY = params.GetSizeCellX();
        int cellSize = gameMap.GetCellSize();
        Coords choosedCellCoords = startCell.GetStartCoords();
        double choosedCellX = choosedCellCoords.X();
        double choosedCellY = choosedCellCoords.Y();
        for(int i = 0; i < sizeCellY; ++i){
            for(int j = 0; j < sizeCellX; ++j){
                double cellX = choosedCellX + j * cellSize;
                double cellY = choosedCellY + i *cellSize;
                Cell cell = gameMap.GetCell(new Coords(cellX, cellY));
                cell.ChangeStatus(CellStatus.BORROW);
            }
        }
    }
    public CreatingCharStatus CreateNewCharacter(Cell startCell, String name) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        CharactersParams params = unicCharacters.get(name);
        if(!ISItSuitableCell(startCell, name, params)){
           return CreatingCharStatus.NOT_CHAR_SPAWN;
        }
        if(params.GetCoast() > player.GetMoney()){

            return CreatingCharStatus.NO_MONEY;
        }
        BorrowCells(startCell, params);
        ICharacter character = characterFactory.CreateCharacter(name, startCell, params);
        characterList.add(character);
        player.ChangeMoney(-params.GetCoast());
        return CreatingCharStatus.CHAR_SPAWN;
    }

    public ArrayList<ICharacter> ReturnCharacterList(){
        return characterList;
    }

    public ArrayList<IEnemy> ReturnEnemyList(){
        return enemyList;
    }

    public Player ReturnPlayer(){
        return player;
    }

    public GameMap ReturnMap(){
        return gameMap;
    }

    public Map<String, CharactersParams> ReturnUnicCharacters(){
        return unicCharacters;
    }
}
