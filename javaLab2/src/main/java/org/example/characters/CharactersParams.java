package org.example.characters;

public class CharactersParams {
    private String name;
    private String pathToIcon;
    private int coast;
    private int damage;
    private int delay;
    private int sizeCellX;
    private int sizeCellY;
    public CharactersParams(String name, String pathToIcon, int damage, int coast, int delay, int sizeCellX, int sizeCellY){
        this.name = name;
        this.coast = coast;
        this.damage = damage;
        this.pathToIcon = pathToIcon;
        this.delay = delay;
        this.sizeCellX = sizeCellX;
        this.sizeCellY = sizeCellY;
    }

    public String GetPathFolder(){
        return pathToIcon;
    }

    public int GetDamage(){
        return damage;
    }

    public int GetDelay(){
        return delay;
    }

    public int GetCoast(){
        return coast;
    }

    public String GetName(){
        return name;
    }

    public int GetSizeCellX(){return sizeCellX;}

    public int GetSizeCellY(){return sizeCellY;}
}
