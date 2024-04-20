package org.example;

import org.example.GameController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ViewerListener {
    private GameController gameCrontoller;
    private boolean stop;

    public ViewerListener(GameController gameCrontoller){
        this.gameCrontoller = gameCrontoller;
        stop = false;
    }

    public String HearCreateChar(String name, Coords confs){
        try {
            return gameCrontoller.CharPanelClick(name, confs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void Restart() throws IOException, ClassNotFoundException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        stop = true;
        gameCrontoller.Restart();
    }

    public boolean Stop(){
        return stop;
    }

    public void ChangeStop(){
        stop = false;
    }
}
