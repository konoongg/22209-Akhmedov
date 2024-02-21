package org.example;

public class Minus implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double num1 = context.Pop();
        double num2 = context.Pop();
        double minus = num1 - num2;
        context.Push(Double.toString(minus));
    }
}
