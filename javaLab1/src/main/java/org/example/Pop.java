package org.example;

import org.example.exceptions.EmptyStack;
public class Pop implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack{
        double pop = 0;
        try{
            context.Pop();
        }
        catch(EmptyStack e){
            throw e;
        }
        if(arguments.length > 1){
            context.Define(arguments[1], pop);
        }
    }
}
