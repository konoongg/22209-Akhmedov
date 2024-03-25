package org.example;

import org.example.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.example.exceptions.EmptyStack;
import org.example.operation.*;

public class Interpreter {
    private final CalcLogger calcLogger;
    private final Factory factory;
    private final Context context;
    private  void InputReader(BufferedReader reader) throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, WrongFormatOfConfig, IOException, UndefindedCommand {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] arguments = line.split(" ");
            if(arguments.length == 0 || Objects.equals(arguments[0], "")){
                throw new WrongFormatOfOperation("empty operation ");
            }
            if(arguments[0].charAt(0) == '#'){
                calcLogger.LogInfo("comment");
                continue;
            }
            if(arguments[0].equals("EXIT")){
                break;
            }
            IOperation operation = factory.CreateOperation(arguments[0]);
            operation.Do(context, arguments);
            calcLogger.LogInfo("done operation: " + line);
        }
    }

    public Interpreter(BufferedReader reader, InputStream config ) throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, WrongFormatOfConfig, CantFindConfig, UndefindedCommand {
        calcLogger = CalcLogger.getInstance();
        context = new Context();
        factory = new Factory(config);
        try{
            InputReader(reader);
        }
        catch (UndefindedCommand e){
            throw e;
        }
        catch(IOException e){
            throw new WrongFormatOfOperation("can't  read operation " + e.getMessage());
        }
    }
}
