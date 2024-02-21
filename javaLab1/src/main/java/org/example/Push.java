package org.example;

public class Push implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        context.Push(arguments[1]);
    }
}
