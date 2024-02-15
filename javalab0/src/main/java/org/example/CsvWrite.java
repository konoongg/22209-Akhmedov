package org.example;

import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class CsvWrite {
     public CsvWrite(Map<String, Integer> countWords, int countAllWords){
         CreateCsv(countWords, countAllWords);
     }

     void  CreateCsv(Map<String, Integer> countWords, int countAllWords){
         try{
             BufferedWriter bw = new BufferedWriter(new FileWriter("result.csv"));
             bw.write("word" + "," + "count" +  "," + "percent" + "\n");
             for(Map.Entry<String, Integer> entry : countWords.entrySet()) {
                 if(entry.getKey().length() == 0 ){
                     continue;
                 }
                 bw.write(entry.getKey() + "," + entry.getValue() + "," + (double)entry.getValue() / countAllWords + "\n");

             }
             bw.close();
         }
         catch (IOException e) {
             System.out.println(e.getMessage());
         }
     }
}
