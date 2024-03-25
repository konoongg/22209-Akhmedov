package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;

public class Sqrt implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double num1 = context.Pop();
        double sqrt = Math.sqrt(num1);;
        context.Push(sqrt);
    }
}
