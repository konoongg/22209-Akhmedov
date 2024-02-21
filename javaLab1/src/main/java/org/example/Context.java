package org.example;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private Stack<Double> stack;
    private Map<String, Double> defineMap;
    public Context(){
        stack = new Stack<>();
        defineMap = new HashMap<>();
    }
    public double Pop(){
        return stack.pop();
    }
    public void Push(String elem){
        try{
            double number = Double.parseDouble(elem);
            stack.push(number);
        }
        catch (NumberFormatException e) {
            double number = defineMap.get(elem);
            stack.push(number);
        }
    }
    public void Define(String name, double definition){
        defineMap.put(name, definition);
    }

}
