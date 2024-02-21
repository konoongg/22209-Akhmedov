package org.example;

public class Define implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        context.Define(arguments[1], Double.parseDouble(arguments[2]));
    }
}

