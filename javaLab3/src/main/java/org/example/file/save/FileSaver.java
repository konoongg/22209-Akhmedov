package org.example.file.save;

import org.example.exceptions.CantcreateFile;
import org.example.exceptions.SaveDataException;
import org.example.file.FileT;

import java.io.*;
import java.util.ArrayList;

public class FileSaver {
    private final ArrayList<FileT> files;
    private String fullPath;

    public FileSaver(ArrayList<FileT> files, String folderPath, String name) throws CantcreateFile {
        this.files = files;
        DefineFullPath(name, folderPath);
        CreateEmptyFiles();
    }

    private void DefineFullPath(String name, String folderPath){
        String fullPath = folderPath;
        if(folderPath.charAt(folderPath.length() - 1) == '/'){
            fullPath += name;
        }
        else{
            fullPath += "/" + name;
        }
        this.fullPath = fullPath;
    }

    private void CreateEmptyFiles() throws CantcreateFile {
        if(files.size() == 1){
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

    public void Write(byte[] data, int startPosition) throws SaveDataException {
        try (RandomAccessFile raf = new RandomAccessFile(fullPath, "rw")) {
            raf.seek(startPosition);
            raf.write(data);
        } catch (FileNotFoundException e) {
            throw new SaveDataException("can't find file: " + fullPath);
        } catch (IOException e) {
            throw new SaveDataException("can't write data: " + e.getMessage());
        }
    }
}
