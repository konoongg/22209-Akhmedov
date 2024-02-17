package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class Interpreter {
    private Fabric fabric;
    private Context context;

    private  void InputReader(InputStream inputStream){
        try( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){
            String line;
            while ((line = reader.readLine()) != null) {
                fabric.
            }
        }
        catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
    }
    public Interpreter(InputStream inputStream){
        context = new Context();
        fabric = new Fabric();
        InputReader(inputStream);
    }

}
