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
    public void TestPush()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation plus = (IOperation) Class.forName("org.example.operation.Push").getDeclaredConstructor().newInstance();
            String[] arguments = new String[2];
            arguments[0] = "PUSH";
            arguments[1] = "1.3";
            plus.Do(context, arguments);
            double result = context.Pop();
            assertEquals(1.3,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("PUSH ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestDefine()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation plus = (IOperation) Class.forName("org.example.operation.Define").getDeclaredConstructor().newInstance();
            String[] arguments = new String[3];
            arguments[0] = "DEFINE";
            arguments[1] = "name";
            arguments[2] = "1.3";
            plus.Do(context, arguments);
            context.PushDefined("name");
            double result = context.Pop();
            assertEquals(1.3,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("Define ERROR " + e.getMessage());
        }
    }
    @Test
    public void TestPop()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation pop = (IOperation) Class.forName("org.example.operation.Pop").getDeclaredConstructor().newInstance();
            String[] arguments = new String[1];
            context.Push(2.6);
            context.Push(1.3);
            arguments[0] = "POP";
            pop.Do(context, arguments);
            double result = context.Pop();
            assertEquals(2.6,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("Define ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestPopOnDefine()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation pop = (IOperation) Class.forName("org.example.operation.Pop").getDeclaredConstructor().newInstance();
            String[] arguments = new String[2];
            context.Push(2.6);
            context.Push(1.3);
            arguments[0] = "POP";
            arguments[1] = "name";
            pop.Do(context, arguments);
            context.PushDefined("name");
            double result = context.Pop();
            assertEquals(1.3,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("Define ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestPrint()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation print= (IOperation) Class.forName("org.example.operation.Print").getDeclaredConstructor().newInstance();
            String[] arguments = new String[1];
            context.Push(2.6);
            arguments[0] = "PRINT";
            print.Do(context, arguments);
            double result = context.Pop();
            assertEquals(2.6,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("Define ERROR " + e.getMessage());
        }
    }


    @Test
    public void TestPushDefine()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation plus = (IOperation) Class.forName("org.example.operation.Define").getDeclaredConstructor().newInstance();
            String[] argumentsDefine = new String[3];
            argumentsDefine[0] = "DEFINE";
            argumentsDefine[1] = "name";
            argumentsDefine[2] = "1.3";
            plus.Do(context, argumentsDefine);
            IOperation push = (IOperation) Class.forName("org.example.operation.Push").getDeclaredConstructor().newInstance();
            String[] argumentsPush = new String[1];
            argumentsPush[0] = "name";
            push.Do(context, argumentsPush);
            double result = context.Pop();
            assertEquals(1.3,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("Define ERROR " + e.getMessage());
        }
    }
    @Test
    public void TestSum()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation plus = (IOperation) Class.forName("org.example.operation.Plus").getDeclaredConstructor().newInstance();
            context.Push(1.0);
            context.Push(2.0);
            String[] arguments = new String[1];
            arguments[0] = "+";
            plus.Do(context, arguments);
            double result = context.Pop();
            assertEquals(3.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("SUM ERROR " + e.getMessage());
        }
    }
    @Test
    public void TestMinus()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation minus = (IOperation) Class.forName("org.example.operation.Minus").getDeclaredConstructor().newInstance();
            context.Push(1.0);
            context.Push(2.0);
            String[] arguments = new String[1];
            arguments[0] = "-";
            minus.Do(context, arguments);
            double result = context.Pop();
            assertEquals(-1.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("MINUS ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestMult()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation mult = (IOperation) Class.forName("org.example.operation.Mult").getDeclaredConstructor().newInstance();
            context.Push(3.0);
            context.Push(2.0);
            String[] arguments = new String[1];
            arguments[0] = "*";
            mult.Do(context, arguments);
            double result = context.Pop();
            assertEquals(6.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("MULT ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestDiv()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation div = (IOperation) Class.forName("org.example.operation.Div").getDeclaredConstructor().newInstance();
            context.Push(3.0);
            context.Push(2.0);
            String[] arguments = new String[1];
            arguments[0] = "/";
            div.Do(context, arguments);
            double result = context.Pop();
            assertEquals(1.5,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("DIV ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestSqrt()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation sqrt = (IOperation) Class.forName("org.example.operation.Sqrt").getDeclaredConstructor().newInstance();
            context.Push(4.0);
            String[] arguments = new String[1];
            arguments[0] = "SQRT";
            sqrt.Do(context, arguments);
            double result = context.Pop();
            assertEquals(2.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("SQRT ERROR " + e.getMessage());
        }
    }
    @Test
    public void TestInvarMD()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation mult = (IOperation) Class.forName("org.example.operation.Mult").getDeclaredConstructor().newInstance();
            context.Push(4.0);
            context.Push(2.0);
            String[] argumentsMult = new String[1];
            argumentsMult[0] = "*";
            mult.Do(context, argumentsMult);
            IOperation invar2 = (IOperation) Class.forName("org.example.operation.Div").getDeclaredConstructor().newInstance();
            context.Push(2.0);
            String[] argumentsDiv = new String[1];
            argumentsDiv[0] = "/";
            invar2.Do(context, argumentsDiv);
            double result = context.Pop();
            assertEquals(4.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("invariant mult/div ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestInvarDM()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation div = (IOperation) Class.forName("org.example.operation.Div").getDeclaredConstructor().newInstance();
            context.Push(4.0);
            context.Push(2.0);
            String[] argumentsDiv = new String[1];
            argumentsDiv[0] = "/";
            div.Do(context, argumentsDiv);
            IOperation invar2 = (IOperation) Class.forName("org.example.operation.Mult").getDeclaredConstructor().newInstance();
            context.Push(2.0);
            String[] argumentsMult = new String[1];
            argumentsMult[0] = "*";
            invar2.Do(context, argumentsMult);
            double result = context.Pop();
            assertEquals(4.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("invariant div/mult ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestInvarPlMin()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation plus = (IOperation) Class.forName("org.example.operation.Plus").getDeclaredConstructor().newInstance();
            context.Push(4.0);
            context.Push(2.0);
            String[] argumentsDiv = new String[1];
            argumentsDiv[0] = "+";
            plus.Do(context, argumentsDiv);
            IOperation invar2 = (IOperation) Class.forName("org.example.operation.Minus").getDeclaredConstructor().newInstance();
            context.Push(2.0);
            String[] argumentsMinus = new String[1];
            argumentsMinus[0] = "-";
            invar2.Do(context, argumentsMinus);
            double result = context.Pop();
            assertEquals(4.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("invariant plus/minus ERROR " + e.getMessage());
        }
    }

    @Test
    public void TestInvarMinPl()  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        try{
            IOperation minus = (IOperation) Class.forName("org.example.operation.Minus").getDeclaredConstructor().newInstance();
            context.Push(4.0);
            context.Push(2.0);
            String[] argumentsDiv = new String[1];
            argumentsDiv[0] = "-";
            minus.Do(context, argumentsDiv);
            IOperation invar2 = (IOperation) Class.forName("org.example.operation.Plus").getDeclaredConstructor().newInstance();
            context.Push(2.0);
            String[] argumentsPlus = new String[1];
            argumentsPlus[0] = "+";
            invar2.Do(context, argumentsPlus);
            double result = context.Pop();
            assertEquals(4.0,  result, 0);
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
              InvocationTargetException | EmptyStack | UndefinedVariable | WrongFormatOfOperation e){
            System.out.println("invariant minus/plus ERROR " + e.getMessage());
        }
    }

}