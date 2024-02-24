package org.example;

import org.example.exceptions.EmptyStack;

public class Define implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        context.Define(arguments[1], Double.parseDouble(arguments[2]));
    }
}

