package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;

public class InputOperation implements AutoCloseable {
    private BufferedReader reader;
    public InputOperation(String[] args){
        CalcLogger calcLogger = CalcLogger.getInstance();
        if(args.length >= 1){
            try{
                calcLogger.LogInfo("read form file:" + args[0]);
                reader = new BufferedReader(new FileReader(args[0]));
            }
            catch (IOException e){
                calcLogger.LogError("Took start arguments. Error reading file: "+ e.getMessage());
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
        else{
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }
    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
