package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;
public class Pop implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack{
        double pop =  context.Pop();
        if(arguments.length > 1){
            context.Define(arguments[1], pop);
        }
    }
}
