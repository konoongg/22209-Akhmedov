package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;
import org.example.exceptions.UndefinedVariable;
import org.example.exceptions.WrongFormatOfOperation;

public interface IOperation {
    void Do(Context context, String[] arguments) throws EmptyStack, UndefinedVariable, WrongFormatOfOperation;
}
