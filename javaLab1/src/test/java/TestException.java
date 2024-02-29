import org.example.CalcLogger;
import org.example.Context;
import org.example.Factory;
import org.example.exceptions.*;
import org.example.operation.IOperation;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

public class TestException
{
    @Test(expected = EmptyStack.class)
    public void TestStackIsEmpty() throws  EmptyStack {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation pop = (IOperation) Class.forName("org.example.operation.Pop").getDeclaredConstructor().newInstance();
            String[] arguments = new String[1];
            arguments[0] = "POP";
            pop.Do(context, arguments);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException |  UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("ERROR " + e.getMessage());
        }
    }

    @Test(expected = WrongFormatOfConfig.class)
    public void TestWrongFormatOfConfig() throws WrongFormatOfConfig  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            Factory factory = new Factory();
            IOperation operation = factory.CreateOperation("PP");
        }
        catch (CantFindConfig e) {
            System.out.println("ERROR " + e);
        }
    }
}
