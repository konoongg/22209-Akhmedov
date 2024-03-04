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
import org.example.exceptions.WrongFormatOfOperation;
import org.example.operation.*;
public class Factory{
    private CalcLogger calcLogger;
    private Map<String, String> pathToClass;
    private void ReadConfig() throws CantFindConfig, WrongFormatOfConfig {
        try(InputStream inputStream = Factory.class.getResourceAsStream("/config.txt")){
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
            }
            else{
                throw new CantFindConfig("cant find config /config");
            }
            if(pathToClass.isEmpty()){
                throw new  WrongFormatOfConfig("config /config is empty");
            }
        } catch (IOException e) {
            throw new WrongFormatOfConfig("cant close config");
        }


    }
    public Factory() throws CantFindConfig,  WrongFormatOfConfig{
        calcLogger = CalcLogger.getInstance();
        calcLogger.LogInfo("START FACRORY");
        pathToClass = new HashMap<>();
        ReadConfig();
    }

    public IOperation CreateOperation(String operationName) throws  WrongFormatOfConfig {
        calcLogger.LogInfo("start create opearation " + operationName);
        String className = pathToClass.get(operationName);
        try{
            IOperation operation = (IOperation) Class.forName(className).getDeclaredConstructor().newInstance();
            calcLogger.LogInfo("create operation: " + className);
            calcLogger.LogInfo("successful opearation " + operationName);
            return operation;
        }
        catch(ClassNotFoundException | NullPointerException  e){
            throw new WrongFormatOfConfig("can't find name class: " + operationName + "\n" + e) ;
        }
        catch(NoSuchMethodException e){
            throw new WrongFormatOfConfig("can't find method eception: " + operationName + "\n" + e) ;
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new WrongFormatOfConfig("can't instance: " + operationName + "\n" + e) ;
        }
    }
}
