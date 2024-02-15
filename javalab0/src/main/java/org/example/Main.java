package org.example;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0){
          System.out.println("file undefinded");
          return;
        }
        String fileName = args[0];
        FileParser parser = new FileParser(fileName);
        CsvWrite writer = new CsvWrite(parser.ReturnCountWord(), parser.ReturnCounAllWorld());

        System.out.println("check result.csv");
    }
}