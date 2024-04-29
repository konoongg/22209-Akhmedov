package org.example.torrent;

import org.example.exceptions.WrongTorrentFileFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;


public class TorrentParser {
    private int curIndex = 0;
    private int startIndex = 0;
    private int endIndex = 0;
    private String fieldName;
    private int sizeStr;

    private int ParseInt(byte[] text) throws WrongTorrentFileFormat {
        int result = 0;
        curIndex++;
        while(text[curIndex] != 'e'){
            if(curIndex >= text.length){
                throw new WrongTorrentFileFormat("num don't close");
            }
            if (text[curIndex] >= '0' && text[curIndex] <= '9') {
                result *= 10;
                result += text[curIndex] - '0';
            }
            else{
                throw new WrongTorrentFileFormat("wrong format of num");
            }
            curIndex++;
        }
        curIndex++;
        return result;
    }

    private String ParseString(byte[] text){
        sizeStr = 0;
        int countSym = 0;
        while(text[curIndex] >= '0' && text[curIndex] <= '9'){
            sizeStr++;
            countSym *= 10;
            countSym += text[curIndex] - '0';
            curIndex++;
        }
        curIndex++; // пропускаем :
        byte[] name = Arrays.copyOfRange(text, curIndex, curIndex + countSym);
        curIndex += countSym;
        return new String(name);
    }

    private void PardeDict(byte[] text) throws WrongTorrentFileFormat {
        curIndex++;
        while(text[curIndex] != 'e'){
            if(curIndex >= text.length){
                throw new WrongTorrentFileFormat("dict don't close");
            }
            ParseString(text);
            if(text[curIndex] == 'i'){
                ParseInt(text);
            }
            else if(text[curIndex] == 'd'){
                PardeDict(text);
            }
            else if(text[curIndex] == 'l'){
                ParseList(text);
            }
            else{
                ParseString(text);
            }
        }
        curIndex++;
    }

    private void ParseList(byte[] text) throws WrongTorrentFileFormat {
        curIndex++;
        while(text[curIndex] != 'e'){
            if(curIndex >= text.length){
                throw new WrongTorrentFileFormat("list don't close");
            }
            if(text[curIndex] == 'i'){
                ParseInt(text);
            }
            else if(text[curIndex] == 'd'){
                PardeDict(text);
            }
            else if(text[curIndex] == 'l'){
                ParseList(text);
            }
            else{
                ParseString(text);
            }
        }
        curIndex++;
    }

    private void ParseFild(byte[] text) throws WrongTorrentFileFormat {
        fieldName = ParseString(text);
        startIndex = curIndex;
        if(text[curIndex] == 'i'){
            startIndex++; // skip i
            ParseInt(text);
            endIndex = curIndex - 1;
        }
        else if(text[curIndex] == 'd'){
            startIndex++;//skip d
            PardeDict(text);
            endIndex = curIndex - 1;
        }
        else if(text[curIndex] == 'l'){
            startIndex++; // skip l
            ParseList(text);
            endIndex = curIndex - 1;
        }
        else if(text[curIndex] >= '0' && text[curIndex] <= '9'){
            ParseString(text);
            startIndex += sizeStr + 1; // размер и двоеточик
            endIndex = curIndex;
        }
        else{
            throw new WrongTorrentFileFormat("can't define data type");
        }
    }

    public void ParseText(Map<String, byte[]> dict, byte[] text) throws WrongTorrentFileFormat {
        curIndex = 0;
        while(curIndex < text.length){
            if(curIndex >= text.length){
                throw new WrongTorrentFileFormat("dict don't close");
            }
            ParseFild(text);
            byte[] field = Arrays.copyOfRange(text, startIndex, endIndex);
            System.out.println(startIndex + " " + endIndex + " " + fieldName);
            dict.put(fieldName, field);
        }
    }

    public TorrentParser(Map<String, byte[]>  mainDict, String path) throws WrongTorrentFileFormat {
        byte[] text = null;
        curIndex = 1;
        try (FileInputStream fis = new FileInputStream(path)) {
            text = fis.readAllBytes();
            text = Arrays.copyOfRange(text, 1, text.length - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ParseText(mainDict, text);
    }
}
