package org.example;

public class Plus implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double num1 = context.Pop();
        double num2 = context.Pop();
        double sum = num1 + num2;
        context.Push(Double.toString(sum));
    }
}
