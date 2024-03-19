package test;

import org.example.Context;
import org.example.exceptions.EmptyStack;
import org.example.operation.IOperation;

import java.io.IOException;

public class TestOperationExceptConst implements IOperation {

    public TestOperationExceptConst() throws IOException {
        throw new IOException("test");
    }

    @Override
    public void Do(Context context, String[] arguments) throws EmptyStack {
        double num1 = context.Pop();
        double num2 = context.Pop();
        double minus = num2 - num1;
        context.Push(minus);
    }
}
