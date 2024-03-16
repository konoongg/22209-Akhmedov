package org.example.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameMap {
    private int sizeX;
    private int sizeY;
    private String nameMap;
    private String sprite;
    private final int cellSize = 50;
    private Cell[] cells;

    private void DefineName(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null){
            System.out.println("empty config");
            throw new IOException("can't read name");
        }
        nameMap = line;
    }

    private void DefineSprite(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null){
            System.out.println("empty config");
            throw new IOException("can't read sprite");
        }
        sprite = line;
    }

    private void DefineSize(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null){
            System.out.println("cant' read size");
            throw new IOException("cant't read size");
        }
        String[] coords = line.split(" ");
        sizeX = Integer.parseInt(coords[0]);
        sizeY = Integer.parseInt(coords[1]);
    }

    //тут надо првоерять, что если в строке бред
    private void DefineCells(BufferedReader reader) throws IOException {
        int cellX = sizeX / cellSize;
        int cellY = sizeY / cellSize;
        int countCell = cellX * cellY;
        String line;
        cells = new Cell[countCell];
        for(int i = 0; i < countCell; ++i){
            line = reader.readLine();
            String[] cell = line.split(" ");
            int coordX = Integer.parseInt(cell[0]);
            int coordY = Integer.parseInt(cell[1]);
            String status = cell[2];
            cells[coordY * cellY  + coordX ] = new Cell(coordX, coordY, cellSize, CellStatus.valueOf(status));
        }
    }

    private void CreateField(String config) throws IOException {
        try(InputStream inputStream = GameMap.class.getResourceAsStream(config)){
            if(inputStream == null){
                throw new IOException("inputString it is null config: " + config);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            DefineName(reader);
            DefineSprite(reader);
            DefineSize(reader);
            DefineCells(reader);
        }
        catch(IOException e){
            System.out.println("cant read config");
            throw e;
        }
    }
    public GameMap(String config) throws IOException {
        CreateField(config);
    }

    public String GetName(){
        return nameMap;
    }

    public String GetSprite(){
        return sprite;
    }

    public int GetSizeX(){
        return sizeX;
    }

    public int GetSizeY(){
        return sizeY;
    }
}
