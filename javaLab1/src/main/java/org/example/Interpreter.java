package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class Interpreter {
    private Factory factory;
    private Context context;

    private  void InputReader(InputStream inputStream){
        try( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arguments = line.split(" ");
                IOperation operation = factory.CreateOperation(arguments[0]);
                operation.Do(context, arguments);
            }
        }
        catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
    }
    public Interpreter(InputStream inputStream){
        context = new Context();
        factory = new Factory();
        InputReader(inputStream);
    }

}
