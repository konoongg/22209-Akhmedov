package org.example;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private Stack<Double> stack;
    private Map<String, Double> define;
    public Context(){
        stack = new Stack<>();
        define = new HashMap<>();
    }
    public Stack<Double> ReturnStack(){
        return stack;
    }

    public Map<String, Double> ReturnDefine(){
        return define;
    }

}
