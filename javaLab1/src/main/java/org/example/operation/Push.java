package org.example.operation;

import org.example.Context;
import org.example.exceptions.UndefinedVariable;
import org.example.exceptions.WrongFormatOfOperation;

public class Push implements IOperation {
    @Override
    public void Do(Context context, String[] arguments) throws UndefinedVariable, WrongFormatOfOperation {
        if(arguments.length == 1){
            throw new WrongFormatOfOperation("need more arguments for push ");
        }
        try {
            Double.parseDouble(arguments[1]);
            context.Push(Double.parseDouble(arguments[1]));
        } catch (NumberFormatException e) {
            context.PushDefined(arguments[1]);
        }
    }
}
