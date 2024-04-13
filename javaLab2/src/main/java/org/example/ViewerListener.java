package org.example;

import org.example.GameController;

import java.lang.reflect.InvocationTargetException;

public class ViewerListener {
    private GameController gameCrontoller;

    public ViewerListener(GameController gameCrontoller){
        this.gameCrontoller = gameCrontoller;
    }

    public String HearCreateChar(String name, Coords confs){
        try {
            return gameCrontoller.CharPanelClick(name, confs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
