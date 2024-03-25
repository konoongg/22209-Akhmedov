import org.example.CalcLogger;
import org.example.Context;
import org.example.Factory;
import org.example.Interpreter;
import org.example.exceptions.*;
import org.example.operation.IOperation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

public class TestCalcFunctional {
    @Test
    public void TestPush() throws ClassNotFoundException, UndefinedVariable, EmptyStack, WrongFormatOfOperation, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation plus = (IOperation) Class.forName("org.example.operation.Push").getDeclaredConstructor().newInstance();
        String[] arguments = new String[2];
        arguments[0] = "PUSH";
        arguments[1] = "1.3";
        plus.Do(context, arguments);
        double result = context.Pop();
        assertEquals(1.3,  result, 0);
    }

    @Test
    public void TestDefine() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
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
    @Test
    public void TestPop() throws UndefinedVariable, EmptyStack, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation pop = (IOperation) Class.forName("org.example.operation.Pop").getDeclaredConstructor().newInstance();
        String[] arguments = new String[1];
        context.Push(2.6);
        context.Push(1.3);
        arguments[0] = "POP";
        pop.Do(context, arguments);
        double result = context.Pop();
        assertEquals(2.6,  result, 0);
    }

    @Test
    public void TestPopOnDefine() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
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

    @Test
    public void TestPrint() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation print= (IOperation) Class.forName("org.example.operation.Print").getDeclaredConstructor().newInstance();
        String[] arguments = new String[1];
        context.Push(2.6);
        arguments[0] = "PRINT";
        print.Do(context, arguments);
        double result = context.Pop();
        assertEquals(2.6,  result, 0);
    }


    @Test
    public void TestPushDefine() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation plus = (IOperation) Class.forName("org.example.operation.Define").getDeclaredConstructor().newInstance();
        String[] argumentsDefine = new String[3];
        argumentsDefine[0] = "DEFINE";
        argumentsDefine[1] = "name";
        argumentsDefine[2] = "1.3";
        plus.Do(context, argumentsDefine);
        IOperation push = (IOperation) Class.forName("org.example.operation.Push").getDeclaredConstructor().newInstance();
        String[] argumentsPush = new String[2];
        argumentsPush[0] = "PUSH";
        argumentsPush[1] = "name";
        push.Do(context, argumentsPush);
        double result = context.Pop();
        assertEquals(1.3,  result, 0);
    }
    @Test
    public void TestSum() throws UndefinedVariable, EmptyStack, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation plus = (IOperation) Class.forName("org.example.operation.Plus").getDeclaredConstructor().newInstance();
        context.Push(1.0);
        context.Push(2.0);
        String[] arguments = new String[1];
        arguments[0] = "+";
        plus.Do(context, arguments);
        double result = context.Pop();
        assertEquals(3.0,  result, 0);
    }
    @Test
    public void TestMinus() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation minus = (IOperation) Class.forName("org.example.operation.Minus").getDeclaredConstructor().newInstance();
        context.Push(1.0);
        context.Push(2.0);
        String[] arguments = new String[1];
        arguments[0] = "-";
        minus.Do(context, arguments);
        double result = context.Pop();
        assertEquals(-1.0,  result, 0);
    }

    @Test
    public void TestMult() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation mult = (IOperation) Class.forName("org.example.operation.Mult").getDeclaredConstructor().newInstance();
        context.Push(3.0);
        context.Push(2.0);
        String[] arguments = new String[1];
        arguments[0] = "*";
        mult.Do(context, arguments);
        double result = context.Pop();
        assertEquals(6.0,  result, 0);
    }

    @Test
    public void TestDiv() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation div = (IOperation) Class.forName("org.example.operation.Div").getDeclaredConstructor().newInstance();
        context.Push(3.0);
        context.Push(2.0);
        String[] arguments = new String[1];
        arguments[0] = "/";
        div.Do(context, arguments);
        double result = context.Pop();
        assertEquals(1.5,  result, 0);
    }

    @Test
    public void TestSqrt() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        IOperation sqrt = (IOperation) Class.forName("org.example.operation.Sqrt").getDeclaredConstructor().newInstance();
        context.Push(4.0);
        String[] arguments = new String[1];
        arguments[0] = "SQRT";
        sqrt.Do(context, arguments);
        double result = context.Pop();
        assertEquals(2.0,  result, 0);
    }
    @Test
    public void TestInvarMD() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
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

    @Test
    public void TestInvarDM() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
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

    @Test
    public void TestInvarPlMin() throws EmptyStack, UndefinedVariable, WrongFormatOfOperation, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
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

    @Test
    public void TestInvarMinPl() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
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

    @Test
    public void Exit() throws WrongFormatOfConfig, UndefinedVariable, EmptyStack, WrongFormatOfOperation, CantFindConfig, UndefindedCommand, IOException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "EXIT";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(reader, inputStream);
    }

    @Test
    public void Comment() throws WrongFormatOfConfig, UndefinedVariable, EmptyStack, WrongFormatOfOperation, CantFindConfig, UndefindedCommand, IOException {
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "#test";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(reader, inputStream);
    }
}

