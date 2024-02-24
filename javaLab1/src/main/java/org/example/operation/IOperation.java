package org.example.operation;

import org.example.Context;
import org.example.exceptions.EmptyStack;

public interface IOperation {
    void Do(Context context, String[] arguments) throws EmptyStack;
}
