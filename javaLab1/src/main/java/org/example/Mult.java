package org.example;

import org.example.exceptions.EmptyStack;

public class Mult implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {

        double num1 = 0;
        double num2 = 0;
        try{
           num1 = context.Pop();
           num2 = context.Pop();
        }
        catch(EmptyStack e){
            throw e;
        }
        double mult = num1 * num2;
        context.Push(Double.toString(mult));
    }
}
