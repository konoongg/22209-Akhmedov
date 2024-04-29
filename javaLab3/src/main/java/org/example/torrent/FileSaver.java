package org.example.torrent;

import org.example.exceptions.CantcreateFile;

import java.io.*;
import java.io.File;
import java.util.ArrayList;

public class FileSaver {
    private ArrayList<FileT> files;
    public FileSaver(ArrayList<FileT> files, String path, String name) throws IOException, CantcreateFile {
        this.files = files;
        CreateEmptyFiles(path, name);
    }

    private String DefineFullPath(String path, String name){
        String fullPath = path;
        if(path.charAt(path.length() - 1) == '/'){
            fullPath += name;
        }
        else{
            fullPath += "/" + name;
        }
        return fullPath;
    }

    private void CreateEmptyFiles(String path, String name) throws IOException, CantcreateFile {
        if(files.size() == 1){
            String fullPath = DefineFullPath(path, name);
            try {
                FileOutputStream fos = new FileOutputStream(fullPath);
                int length = files.get(0).GetLength();
                byte[] zeros = new byte[length];
                fos.write(zeros);
                fos.close();
            } catch (IOException e) {
                throw new CantcreateFile("can't create file: " + fullPath);
            }
        }
    }
}
