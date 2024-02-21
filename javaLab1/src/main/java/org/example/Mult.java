package org.example;

public class Mult implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double num1 = context.Pop();
        double num2 = context.Pop();
        double mult = num1 * num2;
        context.Push(Double.toString(mult));
    }
}
