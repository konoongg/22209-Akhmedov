package org.example;

public class Sprite {
    private final String spritePath;
    private final int spriteSizeX;
    private final int spriteSizey;
    public Sprite(String spritePath, int spriteSizeX, int spriteSizey){
        this.spritePath = spritePath;
        this.spriteSizeX = spriteSizeX;
        this.spriteSizey = spriteSizey;
    }

    public int SizeX(){
        return spriteSizeX;
    }

    public int SizeY(){
        return spriteSizey;
    }

    public String Path(){
        return spritePath;
    }
}
