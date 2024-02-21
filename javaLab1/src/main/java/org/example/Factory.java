package org.example;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.ClassNotFoundException;
public class Factory{
    private Map<String, String> pathToClass;
    private void ReadConfig(){
        InputStream inputStream = Factory.class.getResourceAsStream("config.txt");
        if(inputStream != null){
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] pathAndName = line.split(" ");
                pathToClass.put(pathAndName[0], pathAndName[1]);
            }
        }
        else{
            System.out.println("error");
        }
        if(pathToClass.isEmpty()){
            System.out.println("error");
        }
    }
    public Factory(){
        ReadConfig();
    }

    public IOperation CreateOperation(String operationName){
        String className = pathToClass.get(operationName);
        try{
            IOperation operation = (IOperation) Class.forName(className).newInstance();
            return operation;
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
            return null;
        }
    }
}
