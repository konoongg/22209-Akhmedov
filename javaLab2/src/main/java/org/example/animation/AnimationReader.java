package org.example.animation;

import org.example.map.GameMap;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AnimationReader {
    public  AnimationReader(String path, Map<String, Animation> animations) throws IOException {
        URL animationURL = AnimationReader.class.getResource(path);
        if(animationURL == null){
            throw new IOException(path + " is null ");
        }
        File folder = new File(animationURL.getPath());
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files == null || files.length == 0){
                throw new IOException(path + "is empty");
            }
            Map<String, String> configure = new HashMap<>();
            ReadAnimationCfg(configure, path);
            for(File file : files){
                if(file.isDirectory() == false){
                    continue;
                }
                String name = file.getName();
                String animationCfg = configure.get(name);
                String[] cfg = animationCfg.split(" ");
                int animatonTime = Integer.parseInt(cfg[1]);
                int spriteSizex = Integer.parseInt(cfg[2]);
                int spriteSizeY = Integer.parseInt(cfg[3]);
                boolean rec = Boolean.parseBoolean(cfg[4]);
                Animation animation = new Animation(animatonTime,path + "/" + name, spriteSizex, spriteSizeY, rec);
                animations.put(name, animation);
            }
        }
        else{
            throw new IOException(path + " is not a folder");
        }
    }

    private void ReadAnimationCfg(Map<String, String> configure, String path) throws IOException {
        String config = path + "/" + "animationConfig.txt";
        try(InputStream inputStream = GameMap.class.getResourceAsStream(config)){
            if(inputStream == null){
                throw new IOException("inputString it is null config: " + config);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cfg = line.split(" ");
                String name = cfg[0];
                configure.put(name, line);
            }
        }
        catch(IOException e){
            System.out.println("cant read config");
            throw e;
        }
    }
}
