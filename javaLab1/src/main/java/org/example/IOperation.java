package org.example;

import org.example.exceptions.EmptyStack;

public interface IOperation {
    void Do(Context context, String[] arguments) throws EmptyStack;
}
