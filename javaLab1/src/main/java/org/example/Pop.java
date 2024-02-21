package org.example;

public class Pop implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double pop = context.Pop();
        if(arguments.length > 1){
            context.Define(arguments[1], pop);
        }
    }
}
