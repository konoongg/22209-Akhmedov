package org.example.enemy;

import org.example.Coords;
import org.example.map.Cell;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Collection;
import java.util.Random;

public class EnemyFactory {

    private final Properties pathToClass;

    private void ReadConfig(){
        try(InputStream inputStream = EnemyFactory.class.getResourceAsStream("/enemyConfig.txt");){
            pathToClass.load(inputStream);
        }
        catch(IOException e){
            System.out.println("error");
        }
    }
    public EnemyFactory(){
        pathToClass = new Properties();
        ReadConfig();
    }

    public IEnemy CreateEnemy(String enemyName, Coords enemyStart, Cell startCell) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        String className = pathToClass.getProperty(enemyName);
        IEnemy enemy = (IEnemy) Class.forName(className).getDeclaredConstructor().newInstance();
        enemy.Create(enemyStart, startCell);
        return enemy;
    }

    public IEnemy CreateRandomEnemy(Coords enemyStart, Cell startCell) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Random random = new Random();
        Collection<Object> valuesCollection = pathToClass.keySet();
        String[] valuesArray = valuesCollection.toArray(new String[0]);
        int randomIndex = random.nextInt(valuesArray.length);
        String className = pathToClass.getProperty(valuesArray[randomIndex]);
        IEnemy enemy = (IEnemy) Class.forName(className).getDeclaredConstructor().newInstance();
        enemy.Create(enemyStart, startCell);
        return enemy;
    }
}
