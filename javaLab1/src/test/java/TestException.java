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

    @Test(expected = UndefinedVariable.class)
    public void UndefinedVariable() throws UndefinedVariable  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            context.PushDefined("name");
        }
        catch(NullPointerException e){
            System.out.println("error" + e);
        }
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void WrongFormatOfOperation() throws WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PUSH 3 \n \n PUSH 2";
        try (BufferedReader reader = new BufferedReader(new StringReader(exampleFile))) {
            Interpreter interpreter = new Interpreter(reader);
        }
        catch(WrongFormatOfOperation e){
            throw e;
        }
        catch(Exception e){
            calcLogger.LogError("error " + e);
            System.out.println("error " + e);
        }
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void UncorDefine() throws WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "DEFINE A";
        try (BufferedReader reader = new BufferedReader(new StringReader(exampleFile))) {
            Interpreter interpreter = new Interpreter(reader);
        }
        catch(WrongFormatOfOperation e){
            throw e;
        }
        catch(Exception e){
            calcLogger.LogError("error " + e);
            System.out.println("error " + e);
        }
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void UncorPush() throws WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PUSH";
        try (BufferedReader reader = new BufferedReader(new StringReader(exampleFile))) {
            Interpreter interpreter = new Interpreter(reader);
        }
        catch(WrongFormatOfOperation e){
            throw e;
        }
        catch(Exception e){
            calcLogger.LogError("error " + e);
            System.out.println("error " + e);
        }
    }
}
