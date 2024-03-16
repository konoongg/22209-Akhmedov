package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            GameStat gameStat = new GameStat("/mapConfig.txt");
            Viewer viewer = new Viewer();
            GameContoller gameContoller = new GameContoller(gameStat, viewer);
        } catch (IOException e) {
            System.out.println("somethink error: " + e);
        }
    }
}