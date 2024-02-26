package org.example;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.example.exceptions.CantFindConfig;
import java.util.Properties;

import org.example.exceptions.ErrorCreateOperation;
import org.example.exceptions.WrongFormatOfConfig;
import org.example.operation.*;
public class Factory{
    private CalcLogger calcLogger;
    private Map<String, String> pathToClass;
    private void ReadConfig() throws CantFindConfig, WrongFormatOfConfig {
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        if(inputStream != null){
            Properties properties = new Properties();
            try{
                properties.load(inputStream);
                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    pathToClass.put(key, value);
                }
            }
            catch(IOException e){
                throw new WrongFormatOfConfig("cant close config");
            }
            calcLogger.LogInfo("successeful reading config");
            try{
                inputStream.close();
            }
            catch(IOException e){
                throw new WrongFormatOfConfig("cant close config");
            }
        }
        else{
            throw new CantFindConfig("cant find config /config");
        }
        if(pathToClass.isEmpty()){
           throw new  WrongFormatOfConfig("config /config is empty");
        }
    }
    public Factory() throws CantFindConfig, NullPointerException, WrongFormatOfConfig, ErrorCreateOperation {
        calcLogger = CalcLogger.getInstance();
        pathToClass = new HashMap<>();
        ReadConfig();
    }

    public IOperation CreateOperation(String operationName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = pathToClass.get(operationName);
        IOperation operation = (IOperation) Class.forName(className).getDeclaredConstructor().newInstance();
        calcLogger.LogInfo("create operation: " + className);
        return operation;
    }
}
