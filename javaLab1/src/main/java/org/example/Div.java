package org.example;

public class Div implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double num1 = context.Pop();
        double num2 = context.Pop();
        double div = num1 / num2;
        context.Push(Double.toString(div));
    }
}
