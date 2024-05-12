package org.example.characters;

import org.example.GameStat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class CharactersReader {
    public CharactersReader(String config, Map<String, CharactersParams> unicCharacters) throws IOException {
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
                int sizeCellX = Integer.parseInt(params[5]);
                int sizeCellY = Integer.parseInt(params[6]);
                CharactersParams charParams = new CharactersParams(name, pathToIcon, damage, coast, delay, sizeCellX, sizeCellY);
                unicCharacters.put(name, charParams);
            }
        }
        catch(IOException e){
            System.out.println("cant read config");
            throw e;
        }
    }
}
