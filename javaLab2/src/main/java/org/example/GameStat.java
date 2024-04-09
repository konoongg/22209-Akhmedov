package org.example;

import org.example.animation.AnimationReader;
import org.example.characters.CharacterFactory;
import org.example.characters.CharactersParams;
import org.example.characters.ICharacter;
import org.example.enemy.EnemyFactory;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.GameMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameStat {
    private final GameMap gameMap;
    private final Player player;
    private final EnemyFactory enemyFactory;
    private final CharacterFactory characterFactory;
    private final ArrayList<IEnemy> enemyList;
    private final ArrayList<ICharacter> characterList;
    private final ArrayList<CharactersParams> unicCharacters;
    public GameStat (String config) throws IOException {
        player = new Player(100, 200);
        gameMap = new GameMap(config);
        enemyFactory = new EnemyFactory();
        characterFactory = new CharacterFactory();
        unicCharacters = new ArrayList<>();
        ReadCharacters();
        enemyList = new ArrayList<>();
        characterList = new ArrayList<>();
    }

    private void ReadCharacters() throws IOException {
        String config = "/charactersStats.txt";
        try(InputStream inputStream = GameStat.class.getResourceAsStream(config)){
            if(inputStream == null){
                throw new IOException("inputString it is null config: " + config);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null){
                String[] params = line.split(" ");
                String name = params[0];
                int coast = Integer.parseInt(params[1]);
                int damage = Integer.parseInt(params[2]);
                String pathToIcon = params[3];
                int delay = Integer.parseInt(params[4]);
                CharactersParams charParams = new CharactersParams(name, pathToIcon, damage, coast, delay);
                unicCharacters.add(charParams);
            }
        }
        catch(IOException e){
            System.out.println("cant read config");
            throw e;
        }
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


    public void CreateNewCharacter(Coords characterCoords, String name) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ICharacter character = characterFactory.CreateCharacter(name, characterCoords);
        characterList.add(character);
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
    public ArrayList<CharactersParams> ReturnUnicCharacters(){
        return unicCharacters;
    }
}
