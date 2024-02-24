package org.example;

import org.example.exceptions.CantFindConfig;
import org.example.exceptions.EmptyConfig;
import org.example.exceptions.EmptyStack;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Logger logger = Logger.getLogger("CalculatorLogger");
    public static void main(String[] args) {
        try (InputStream ins = Main.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        logger.setLevel(Level.ALL);
        logger.log(Level.INFO, "start the programm");
        InputStream inputStream;
        if(args.length >= 1){
            try{
                logger.log(Level.INFO, "read form file:" + args[0]);
                inputStream =  new FileInputStream(args[0]);
            }
            catch (IOException e){
                //logger.setLevel(Level.SEVERE);
                logger.severe("Took start arguments. Error reading file: "+ e.getMessage());
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }
        }
        else {
            logger.log(Level.INFO, "read from console");
            inputStream = System.in;
        }
        try{
            Interpreter interpreter = new Interpreter(inputStream, logger);
        }
        catch(EmptyStack | IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
              InstantiationException | IllegalAccessException | EmptyConfig | CantFindConfig e){
            logger.log(Level.SEVERE, "error " + e.getMessage());
            System.out.println("error " + e.getMessage());
        }
        logger.log(Level.INFO, "end the programm");
    }
}