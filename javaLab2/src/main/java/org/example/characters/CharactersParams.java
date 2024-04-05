package org.example.characters;

public class CharactersParams {
    private String name;
    private String pathToIcon;
    private int coast;
    private int damage;
    private int delay;
    public CharactersParams(String name, String pathToIcon, int damage, int coast, int delay){
        this.name = name;
        this.coast = coast;
        this.damage = damage;
        this.pathToIcon = pathToIcon;
        this.delay = delay;
    }
}
