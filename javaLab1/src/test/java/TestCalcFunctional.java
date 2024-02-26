import org.example.CalcLogger;
import org.example.Context;
import org.example.exceptions.EmptyStack;
import org.example.exceptions.UndefinedVariable;
import org.example.exceptions.WrongFormatOfOperation;
import org.example.operation.IOperation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

public class TestCalcFunctional {
    @Test
    public void TestSumWithOutDefined()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        Context contextCor = new Context();
        try{
            IOperation plus = (IOperation) Class.forName("org.example.operation.Plus").getDeclaredConstructor().newInstance();
            context.Push(1.0);
            context.Push(2.0);
            String[] arguments = new String[1];
            arguments[0] = "+";
            plus.Do(context, arguments);
            contextCor.Push(3.0);
            assertEquals(context, contextCor);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("SUM ERROR " + e.getMessage());
        }
    }
}