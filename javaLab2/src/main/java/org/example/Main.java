package org.example;

import org.example.viewer.Viewer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            GameStat gameStat = new GameStat("/mapConfig.txt");
            Viewer viewer = new Viewer();
            GameController gameContoller = new GameController(gameStat, viewer);
        } catch (IOException | InterruptedException e) {
            System.out.println("somethink error: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}