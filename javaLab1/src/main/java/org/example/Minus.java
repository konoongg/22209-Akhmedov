package org.example;

import org.example.exceptions.EmptyStack;

public class Minus implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double num1 = 0;
        double num2 = 0;
        try{
            num1 = context.Pop();
            num2 = context.Pop();
        }
        catch(EmptyStack e){
            throw e;
        }
        double minus = num2 - num1;
        context.Push(Double.toString(minus));
    }
}
