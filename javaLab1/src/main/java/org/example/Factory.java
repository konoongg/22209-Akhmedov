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

import org.example.exceptions.*;

import java.util.Properties;

import org.example.operation.*;
public class Factory{
    private CalcLogger calcLogger;
    private Properties pathToClass;
    private void ReadConfig(InputStream inputStream ) throws CantFindConfig, WrongFormatOfConfig{
        if(inputStream != null){
            try {
                pathToClass.load(inputStream);
            }
            catch (IOException e) {
                throw new WrongFormatOfConfig("cant' read config");
            }
            calcLogger.LogInfo("successeful reading config");
        }
        else{
            throw new CantFindConfig("cant find config /config");
        }
        if(pathToClass.isEmpty()){
            throw new  WrongFormatOfConfig("config /config is empty");
        }
    }


    public Factory(InputStream  config) throws CantFindConfig, WrongFormatOfConfig {
        calcLogger = CalcLogger.getInstance();
        calcLogger.LogInfo("START FACRORY");
        pathToClass = new Properties();
        ReadConfig(config);
    }

    public IOperation CreateOperation(String operationName) throws WrongFormatOfConfig, UndefindedCommand {
        calcLogger.LogInfo("start create opearation " + operationName);
        String className = pathToClass.getProperty(operationName);
        if(className == null){
            throw new UndefindedCommand("can't find name command: " + operationName) ;
        }
        try{
            IOperation operation = (IOperation) Class.forName(className).getDeclaredConstructor().newInstance();
            calcLogger.LogInfo("create operation: " + className);
            calcLogger.LogInfo("successful opearation " + operationName);
            return operation;
        }
        catch(ClassNotFoundException | NullPointerException  e){
            throw new WrongFormatOfConfig("can't find name command: " + operationName) ;
        }
        catch(NoSuchMethodException e){
            throw new WrongFormatOfConfig("can't find method eception: " + operationName) ;
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new WrongFormatOfConfig("can't instance: " + operationName) ;
        }
    }
}
