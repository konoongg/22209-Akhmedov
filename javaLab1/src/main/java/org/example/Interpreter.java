package org.example;

import org.example.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.example.exceptions.EmptyStack;
import org.example.operation.*;

public class Interpreter {
    private CalcLogger calcLogger;
    private Factory factory;
    private Context context;
    private  void InputReader(InputOperation reader) throws EmptyStack, UndefinedVariable,  WrongFormatOfOperation, ErrorCreateOperation, WrongFormatOfConfig {
        try{
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arguments = line.split(" ");
                if(arguments.length == 0){
                    throw new WrongFormatOfOperation("empty operation ");
                }
                if(arguments[0].charAt(0) == '#'){
                    calcLogger.LogInfo("comment");
                    continue;
                }
                IOperation operation = factory.CreateOperation(arguments[0]);
                operation.Do(context, arguments);
                calcLogger.LogInfo("done operation: " + line);
            }
        }
        catch (IOException e){
            throw new ErrorCreateOperation("Error create operation: " + e.getMessage());
        }
    }
    public Interpreter(InputOperation reader ) throws EmptyStack, UndefinedVariable, ErrorCreateOperation, WrongFormatOfOperation,  WrongFormatOfConfig, CantFindConfig {
        calcLogger = CalcLogger.getInstance();
        context = new Context();
        factory = new Factory();
        InputReader(reader);
    }
}
