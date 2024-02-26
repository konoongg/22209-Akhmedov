package org.example;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import org.example.exceptions.EmptyStack;
import org.example.exceptions.UndefinedVariable;

public class Context {
    private CalcLogger calcLogger;
    private Stack<Double> stack;
    private Map<String, Double> defineMap;
    public Context(){
        calcLogger = CalcLogger.getInstance();
        stack = new Stack<>();
        defineMap = new HashMap<>();
    }
    public double Pop() throws EmptyStack {
        if(stack.size() == 0){
            throw new EmptyStack("stack is empty");
        }
        calcLogger.LogInfo("pop");
        return stack.pop();
    }
    public void Push(double number){
       stack.push(number);
       calcLogger.LogInfo("push concreat num: " + number);
    }

    public void PushDefined(String name) throws UndefinedVariable {
        double number = defineMap.get(name);
        if(Objects.isNull(number)){
            throw new UndefinedVariable("undefined variable " + name);
        }
        stack.push(number);
        calcLogger.LogInfo("push defined num: " + name);

    }
    public void Define(String name, double definition){
        calcLogger.LogInfo("defined " + name+ " " + Double.toString(definition) );
        defineMap.put(name, definition);
    }
}
