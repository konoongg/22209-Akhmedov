import org.example.*;
import org.example.exceptions.*;
import org.example.operation.IOperation;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

public class TestException
{
    @Test(expected = EmptyStack.class)
    public void TestStackIsEmpty() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation pop = (IOperation) Class.forName("org.example.operation.Pop").getDeclaredConstructor().newInstance();
        String[] arguments = new String[1];
        arguments[0] = "POP";
        pop.Do(context, arguments);
    }

    @Test(expected = UndefindedCommand.class)
    public void TestWrongFormatOfConfig() throws UndefindedCommand, WrongFormatOfConfig, CantFindConfig {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        Factory factory = new Factory();
        IOperation operation = factory.CreateOperation("PP");
    }

    @Test(expected = UndefinedVariable.class)
    public void UndefinedVariable() throws UndefinedVariable  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        context.PushDefined("name");
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void WrongFormatOfOperation() throws WrongFormatOfOperation, WrongFormatOfConfig, UndefinedVariable, ErrorCreateOperation, EmptyStack, CantFindConfig {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PUSH 3 \n \n PUSH 2";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        Interpreter interpreter = new Interpreter(reader);
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void UncorDefine() throws WrongFormatOfOperation, WrongFormatOfConfig, UndefinedVariable, ErrorCreateOperation, EmptyStack, CantFindConfig {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "DEFINE A";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        Interpreter interpreter = new Interpreter(reader);
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void UncorPush() throws WrongFormatOfOperation, WrongFormatOfConfig, UndefinedVariable, ErrorCreateOperation, EmptyStack, CantFindConfig {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PUSH";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        Interpreter interpreter = new Interpreter(reader);
    }
}
