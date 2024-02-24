package org.example;

import org.example.exceptions.EmptyStack;

public class Sqrt implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double num1 = 0;
        try{
            num1 = context.Pop();
        }
        catch(EmptyStack e){
            throw e;
        }
        double sqrt = Math.sqrt(num1);;
        context.Push(Double.toString(sqrt));
    }
}
