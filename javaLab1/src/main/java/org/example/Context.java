package org.example;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import org.example.exceptions.EmptyStack;

public class Context {
    private Logger logger;
    private Stack<Double> stack;
    private Map<String, Double> defineMap;
    public Context(Logger logger){
        this.logger = logger;
        stack = new Stack<>();
        defineMap = new HashMap<>();
    }
    public double Pop() throws EmptyStack {
        if(stack.size() == 0){
            throw new EmptyStack("stack is empty");
        }
        logger.log(Level.INFO, "pop");
        return stack.pop();
    }
    public void Push(String elem){
        try{
            double number = Double.parseDouble(elem);
            stack.push(number);
            logger.log(Level.INFO, "push concreat num: " + elem);
        }
        catch (NumberFormatException e) {
            double number = defineMap.get(elem);
            stack.push(number);
            logger.log(Level.INFO, "push defined num: " + elem);
        }
    }
    public void Define(String name, double definition){
        logger.log(Level.INFO, "defined " + name+ " " + Double.toString(definition) );
        defineMap.put(name, definition);
    }

}
