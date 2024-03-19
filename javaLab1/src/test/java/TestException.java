import org.example.*;
import org.example.exceptions.*;
import org.example.operation.IOperation;
import org.junit.Test;

import java.io.*;
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
    public void TestWrongFormatOfConfig() throws UndefindedCommand, WrongFormatOfConfig, CantFindConfig, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        /*CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        Factory factory = new Factory("/config.txt");
        IOperation operation = factory.CreateOperation("PP");*/

        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PP";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(reader, inputStream);
    }

    @Test(expected = UndefinedVariable.class)
    public void UndefinedVariable() throws UndefinedVariable  {
        CalcLogger calcLogger = CalcLogger.getInstance();
        Context context = new Context();
        context.PushDefined("name");
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void WrongFormatOfOperation() throws WrongFormatOfOperation, WrongFormatOfConfig, UndefinedVariable, EmptyStack, CantFindConfig, UndefindedCommand{
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PUSH 3 \n \n PUSH 2";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(reader, inputStream);
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void UncorDefine() throws WrongFormatOfOperation, WrongFormatOfConfig, UndefinedVariable, EmptyStack, CantFindConfig, UndefindedCommand{
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "DEFINE A";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(reader, inputStream);
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void UncorPush() throws WrongFormatOfOperation, WrongFormatOfConfig, UndefinedVariable, EmptyStack, CantFindConfig, UndefindedCommand{
        CalcLogger calcLogger = CalcLogger.getInstance();
        String exampleFile = "PUSH";
        BufferedReader reader = new BufferedReader(new StringReader(exampleFile));
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(reader, inputStream);
    }

    @Test(expected = CantFindConfig.class)
    public void NotExistenceConfig() throws WrongFormatOfConfig, CantFindConfig {
        InputStream inputStream = Factory.class.getResourceAsStream("/test.txt");
        Factory fac = new Factory(inputStream);
    }

    @Test(expected = WrongFormatOfConfig.class)
    public void EmptyConfig() throws WrongFormatOfConfig, CantFindConfig {
        String exampleFile = "";
        InputStream inputStream = new ByteArrayInputStream(exampleFile.getBytes());
        Factory fac = new Factory(inputStream);
    }
    @Test(expected = WrongFormatOfConfig.class)
    public void ClassNotFounded() throws WrongFormatOfConfig, CantFindConfig, UndefindedCommand {
        String exampleFile = "TEST org.exmaple.testClass";
        InputStream inputStream = new ByteArrayInputStream(exampleFile.getBytes());
        Factory fac = new Factory(inputStream);
        fac.CreateOperation("TEST");
    }

    @Test(expected = WrongFormatOfConfig.class)
    public void CantCreatPropirties() throws WrongFormatOfConfig, CantFindConfig, UndefindedCommand {
        InputStream fakeInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("IOException");
            }
        };
        Factory fac = new Factory(fakeInputStream);
        fac.CreateOperation("TEST");
    }

    @Test(expected = WrongFormatOfOperation.class)
    public void FakeReader() throws WrongFormatOfConfig, CantFindConfig, UndefindedCommand, UndefinedVariable, EmptyStack, WrongFormatOfOperation {
        BufferedReader fakeReader = new BufferedReader(new StringReader("TEST")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Fake IOException");
            }
        };
        InputStream inputStream = Factory.class.getResourceAsStream("/config.txt");
        Interpreter interpreter = new Interpreter(fakeReader, inputStream);
    }
}
