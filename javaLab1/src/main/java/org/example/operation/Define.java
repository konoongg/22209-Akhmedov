package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;
import org.example.exceptions.WrongFormatOfOperation;

public class Define implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack, WrongFormatOfOperation {
        if(arguments.length < 3){
            throw new WrongFormatOfOperation("need more arguments for define");
        }
        context.Define(arguments[1], Double.parseDouble(arguments[2]));
    }
}

