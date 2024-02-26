package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;

public class Div implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double num1 = context.Pop();
        double num2 = context.Pop();
        double div = num1 / num2;
        context.Push(div);
    }
}
