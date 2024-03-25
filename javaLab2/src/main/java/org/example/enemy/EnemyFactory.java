package org.example.enemy;

import org.example.Coords;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

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

    public IEnemy CreateEnemy(String enemyName, Coords enemyStart) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String className = pathToClass.getProperty(enemyName);
        IEnemy enemy = (IEnemy) Class.forName(className).getDeclaredConstructor().newInstance();
        enemy.Create(enemyStart);
        return enemy;
    }
}
