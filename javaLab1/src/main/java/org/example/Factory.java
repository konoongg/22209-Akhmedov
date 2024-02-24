package org.example;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.example.exceptions.CantFindConfig;
import org.example.exceptions.EmptyConfig;
import org.example.operation.*;
public class Factory{
    private Logger logger;
    private Map<String, String> pathToClass;
    private void ReadConfig() throws NullPointerException, CantFindConfig, EmptyConfig {
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        System.out.println(inputStream);
        if(inputStream != null){
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
                String[] pathAndName = line.split(" ");
                try{
                    pathToClass.put(pathAndName[0], pathAndName[1]);
                }
                catch(java.lang.NullPointerException e){
                    throw e;
                }
            }
            logger.log(Level.INFO, " successeful reading config" );
        }
        else{
            throw new CantFindConfig("cant find config /config");
        }
        if(pathToClass.isEmpty()){
           throw new EmptyConfig("config /config is empty");
        }
    }
    public Factory(Logger logger) throws CantFindConfig, NullPointerException, EmptyConfig {
        this.logger = logger;
        pathToClass = new HashMap<>();
        try{
            ReadConfig();
        }
        catch(NullPointerException | CantFindConfig | EmptyConfig e){
            throw e;
        }
    }

    public IOperation CreateOperation(String operationName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = pathToClass.get(operationName);
        try{
            IOperation operation = (IOperation) Class.forName(className).getDeclaredConstructor().newInstance();

            logger.log(Level.INFO, "create operation: " + className);
            return operation;
        }
        catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException |
              IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
              throw e;
        }
    }
}
