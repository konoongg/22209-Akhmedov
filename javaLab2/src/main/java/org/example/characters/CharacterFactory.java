package org.example.characters;

import org.example.Coords;
import org.example.map.Cell;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class CharacterFactory {
    private final Properties pathToClass;

    private void ReadConfig(){
        try(InputStream inputStream = CharacterFactory.class.getResourceAsStream("/charactersConfig.txt")){
            pathToClass.load(inputStream);
        }
        catch(IOException e){
            System.out.println("error");
        }
    }
    public CharacterFactory(){
        pathToClass = new Properties();
        ReadConfig();
    }
    public ICharacter CreateCharacter(String characterName, Cell startCell, CharactersParams params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        String className = pathToClass.getProperty(characterName);
        ICharacter character= (ICharacter) Class.forName(className).getDeclaredConstructor().newInstance();
        character.Create(startCell, params);
        return character;
    }
}
