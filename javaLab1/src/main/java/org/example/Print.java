package org.example;

import org.example.exceptions.EmptyStack;

public class Print  implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double pop = 0;
        try{
            pop = context.Pop();
        }
        catch(EmptyStack e){
            throw e;
        }
        System.out.println(pop);
        context.Push(Double.toString(pop));
    }
}
