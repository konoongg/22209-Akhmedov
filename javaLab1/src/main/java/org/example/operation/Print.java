package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;

public class Print  implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double pop = context.Pop();
        System.out.println(pop);
        context.Push(pop);
    }
}
