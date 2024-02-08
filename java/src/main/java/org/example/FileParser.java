package org.example;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
public class FileParser {
    private String fileName;
    private int countAllWorld;
    private Map<String, Integer> countWord = new LinkedHashMap<>();
    public FileParser(String fileName){
        this.fileName = fileName;
        ReadString();
    }

    private void ReadString(){
        try{
            Map<String, Integer> noSortCountWord = new LinkedHashMap<>();
            Scanner scanner = new Scanner(new java.io.File(fileName));
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                line = RemoveWrongChar(line);
                DefineWorld(line, noSortCountWord);
            }
            scanner.close();
            SortMapByValue(noSortCountWord);
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    private String RemoveWrongChar(String line){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < line.length(); ++i) {
            if (Character.isDigit(line.charAt(i)) || Character.isLetter(line.charAt(i)) || line.charAt(i) == ' ') {
                sb.append(line.charAt(i));
            }
        }
        return sb.toString();
    }

    private void DefineWorld(String line, Map<String, Integer> noSortCountWord) {
        String[] words = line.split(" ");
        countAllWorld += words.length;
        for(String word: words){
            if(noSortCountWord.containsKey(word)){
                int count = noSortCountWord.get(word);
                noSortCountWord.put(word, count + 1);
            }
            else{
                noSortCountWord.put(word, 1);
            }
        }
    }

    private void SortMapByValue(Map<String, Integer> noSortCountWord){
        List<Map.Entry<String, Integer>> list = new LinkedList<>(noSortCountWord.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<String, Integer> entry : list) {
            countWord.put(entry.getKey(), entry.getValue());
        }
    }

    public int ReturnCounAllWorld(){
        return countAllWorld;
    }

    public Map<String, Integer> ReturnCountWord(){
        return countWord;
    }
}
