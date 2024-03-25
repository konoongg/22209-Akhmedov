package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.Level;
public class CalcLogger {
    private static CalcLogger instance;
    private Logger logger;
    private CalcLogger(){
        logger =  Logger.getLogger("CalculatorLogger");
        try (InputStream ins = Main.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
    }

    public static CalcLogger getInstance() {
        if (instance == null) {
            instance = new CalcLogger();
        }
        return instance;
    }
    public void LogError(String error){
        logger.log(Level.SEVERE, error);
    }
    public void LogInfo(String info){
        logger.log(Level.INFO, info);
    }
}
