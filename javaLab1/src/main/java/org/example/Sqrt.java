package org.example;

public class Sqrt implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double num1 = context.Pop();
        double sqrt = Math.sqrt(num1);;
        context.Push(Double.toString(sqrt));
    }
}
