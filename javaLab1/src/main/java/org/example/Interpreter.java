package org.example;

import org.example.exceptions.CantFindConfig;
import org.example.exceptions.EmptyConfig;
import org.example.exceptions.EmptyStack;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.example.exceptions.EmptyStack;
public class Interpreter {
    private  Logger logger;
    private Factory factory;
    private Context context;
    private  void InputReader(InputStream inputStream) throws EmptyStack, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arguments = line.split(" ");
                if(arguments[0].charAt(0) == '#'){
                    logger.log(Level.INFO, "comment");
                    continue;
                }
                try{

                    IOperation operation = factory.CreateOperation(arguments[0]);
                    operation.Do(context, arguments);
                }
                catch(EmptyStack | ClassNotFoundException | InvocationTargetException |  NoSuchMethodException
                      |  InstantiationException| IllegalAccessException e){
                    throw e;
                }
                logger.log(Level.INFO, "done operation: " + line);
            }
        }
        catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
               InstantiationException | IllegalAccessException e){
            throw e;
        }
    }
    public Interpreter(InputStream inputStream, Logger logger) throws EmptyStack, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, EmptyConfig, CantFindConfig {
        this.logger = logger;
        context = new Context(logger);
        try {

            factory = new Factory(logger);
            InputReader(inputStream);
        }
        catch(EmptyStack | IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
              InstantiationException | IllegalAccessException | CantFindConfig | EmptyConfig e){
            throw e;
        }

    }
}
