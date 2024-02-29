import org.example.*;
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

    @Test(expected = ErrorCreateOperation.class)
    public void ErrorCreateOperation() throws ErrorCreateOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String[] args = new String[1];
        args[0] = "";
        try (InputOperation reader = new InputOperation(args);) {
            Interpreter interpreter = new Interpreter(reader);
        }
        catch(ErrorCreateOperation e){
            throw e;
        }
        catch(Exception e){
            calcLogger.LogError("error " + e);
            System.out.println("error " + e);
        }
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void WrongFormatOfOperation() throws WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String[] args = new String[1];
        args[0] = "/home/konoongglts/work/22209-Akhmedov/javaLab1/src/test/resources/empty.txt";
        try (InputOperation reader = new InputOperation(args);) {
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
        String[] args = new String[1];
        args[0] = "/home/konoongglts/work/22209-Akhmedov/javaLab1/src/test/resources/uncorDefine.txt";
        try (InputOperation reader = new InputOperation(args);) {
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
        String[] args = new String[1];
        args[0] = "/home/konoongglts/work/22209-Akhmedov/javaLab1/src/test/resources/uncorPush.txt";
        try (InputOperation reader = new InputOperation(args);) {
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
