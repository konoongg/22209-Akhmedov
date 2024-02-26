package org.example;

import org.example.exceptions.*;

import java.lang.reflect.InvocationTargetException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CalcLogger calcLogger = CalcLogger.getInstance();
        calcLogger.LogInfo("start the programm");
        InputStream inputStream;
        if(args.length >= 1){
            try{
                calcLogger.LogInfo("read form file:" + args[0]);
                inputStream =  new FileInputStream(args[0]);
            }
            catch (IOException e){
                calcLogger.LogError("Took start arguments. Error reading file: "+ e.getMessage());
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }
        }
        else {
            calcLogger.LogInfo("read from console");
            inputStream = System.in;
        }
        try{
            Interpreter interpreter = new Interpreter(inputStream);
        }
        catch(EmptyStack | UndefinedVariable | ErrorCreateOperation | ClassNotFoundException | WrongFormatOfOperation |
              CantFindConfig | WrongFormatOfConfig e){

            calcLogger.LogError("error " + e.getMessage());
            System.out.println("error " + e.getMessage());
        }
        calcLogger.LogInfo("end the programm");
    }
}