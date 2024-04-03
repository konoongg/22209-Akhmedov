package org.example.animation;

import org.example.Sprite;
import org.example.characters.ICharacter;
import org.example.enemy.Artist;
import org.example.viewer.Viewer;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Animation {
     private final int animationTime;
     private final LinkedList<Sprite> animationFiles;
     private ListIterator<Sprite> iterator;
     private final boolean rec;

     private void ReadAnimation(String pathToFolder, int spriteSizeX, int spriteSizeY) throws IOException {
        URL animationURL = Artist.class.getResource(pathToFolder);
        if(animationURL == null){
            throw new IOException(pathToFolder + " can't open");
        }
        File folder = new File(animationURL.getPath());
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    return file1.getName().compareTo(file2.getName());
                }
            });
            if(files.length == 0){
                throw new IOException(pathToFolder + "is empty");
            }
            for(File file : files){
               String fileName = file.getName();
               Sprite sprite = new Sprite(pathToFolder + '/' + fileName, spriteSizeX, spriteSizeY);
               animationFiles.add(sprite);
            }
        }
        else{
            throw new IOException(pathToFolder + "is not a folder");
        }
     }
     public Animation(int animationTime, String pathToFolder, int spriteSizeX, int spriteSizeY, boolean rec ) throws IOException {
         this.animationTime = animationTime;
         animationFiles = new LinkedList<>();
         this.rec = rec;
         ReadAnimation(pathToFolder, spriteSizeX,  spriteSizeY);
         iterator = animationFiles.listIterator();
     }

     public int GetTime(){
         return animationTime;
     }

     public Sprite GetNextSprite(){
         if (iterator.hasNext()) {
             return iterator.next();
         }
         else if(rec){
             iterator = animationFiles.listIterator();
             return iterator.next();
         }
         else{
             return null;
         }
     }
}
