package org.example;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        InputStream inputStream;
        if(args.length >= 1){
            try{
                inputStream =  new FileInputStream(args[0]);
            }
            catch (IOException e){
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }
        }
        else {
            inputStream = System.in;
        }
        Interpreter interpreter = new Interpreter(inputStream);
    }
}