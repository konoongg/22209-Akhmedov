package org.example;

import org.example.exceptions.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static BufferedReader createReader(String[] args) throws IOException
    {
        BufferedReader reader;
        if(args.length >= 1){
            try{
                reader = new BufferedReader(new FileReader(args[0]));
            }
            catch (IOException e){
                System.out.println("Error reading file: " + e.getMessage());
                throw e;
            }
        }
        else{
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        return reader;
    }
    public static void main(String[] args) {
        String config = "/config.txt";
        CalcLogger calcLogger = CalcLogger.getInstance();
        calcLogger.LogInfo("start the programm");
        try (BufferedReader reader = createReader(args)) {
            Interpreter interpreter = new Interpreter(reader, config);
        }
        catch(Exception e){
            calcLogger.LogError("error " + e.getMessage());
            System.out.println("error " + e.getMessage());
        }
        calcLogger.LogInfo("end the programm");
    }
}