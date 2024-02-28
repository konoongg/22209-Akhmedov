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
        try (InputOperation reader = new InputOperation(args);) {
            Interpreter interpreter = new Interpreter(reader);
        }
        catch(Exception e){
            calcLogger.LogError("error " + e.getMessage());
            System.out.println("error " + e.getMessage());
        }
        calcLogger.LogInfo("end the programm");
    }
}