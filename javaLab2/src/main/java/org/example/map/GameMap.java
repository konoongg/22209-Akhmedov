package org.example.map;

import org.example.Coords;
import org.example.Sprite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameMap {
    private int sizeX;
    private int sizeY;
    private String nameMap;
    private Sprite sprite;
    private final int cellSize = 50;
    private Cell[] cells;
    private ArrayList<Coords> enemySpawn;

    private void DefineName(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null){
            System.out.println("empty config");
            throw new IOException("can't read name");
        }
        nameMap = line;
    }

    private String DefineSpritePath(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null){
            System.out.println("empty config");
            throw new IOException("can't read sprite");
        }
        return  line;
    }

    private void DefineSprite(BufferedReader reader) throws IOException {
        String path = DefineSpritePath(reader);
        String[] coords = DefineSize(reader);
        sizeX = Integer.parseInt(coords[0]);
        sizeY = Integer.parseInt(coords[1]);
        sprite = new Sprite(path, sizeX, sizeY);
    }

    private String[] DefineSize(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null){
            System.out.println("cant' read size");
            throw new IOException("cant't read size");
        }
        String[] coords = line.split(" ");
        return coords;
    }

    //тут надо првоерять, что если в строке бред
    private void DefineCells(BufferedReader reader) throws IOException {
        int countCellX = sizeX / cellSize;
        int countCellY = sizeY / cellSize;
        int countCell = countCellX * countCellY;
        String line;
        cells = new Cell[countCell];
        for(int i = 0; i < countCell; ++i){
            line = reader.readLine();
            String[] cell = line.split(" ");
            int coordX = Integer.parseInt(cell[0]);
            int coordY = Integer.parseInt(cell[1]);
            String status = cell[2];
            cells[coordY * countCellY  + coordX ] = new Cell(coordX, coordY, cellSize, CellStatus.valueOf(status));
        }
    }

    private void DefineEnemySpawn(BufferedReader reader) throws IOException {
        String line = reader.readLine();;
        int countSpawns = Integer.parseInt(line);
        for(int i = 0; i < countSpawns; ++i){
            line = reader.readLine();
            String[] cell = line.split(" ");
            int coordX = Integer.parseInt(cell[0]);
            int coordY = Integer.parseInt(cell[1]);
            Coords coordsSpawn = new Coords(coordX * cellSize, coordY * cellSize);
            enemySpawn.add(coordsSpawn);
        }
    }
    private void CreateField(String config) throws IOException {
        enemySpawn = new ArrayList<>();
        try(InputStream inputStream = GameMap.class.getResourceAsStream(config)){
            if(inputStream == null){
                throw new IOException("inputString it is null config: " + config);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            DefineName(reader);
            DefineSprite(reader);
            DefineCells(reader);
            DefineEnemySpawn(reader);
        }
        catch(IOException e){
            System.out.println("cant read config");
            throw e;
        }
    }

    public GameMap(String config) throws IOException {
        CreateField(config);
    }

    public void Restart(){
        for(Cell cell : cells){
            cell.Restart();
        }
    }

    public String GetName(){
        return nameMap;
    }

    public Sprite GetSprite(){
        return sprite;
    }

    public int GetCellSize(){
        return cellSize;
    }
    public ArrayList<Coords> GetEnemySpawn(){
        return enemySpawn;
    }

    public Cell GetCell(Coords coords){
        int Y = (int)coords.Y() / cellSize;
        int X = (int)coords.X() / cellSize;
        int countCellY = sizeY / cellSize;
        int countCellX = sizeX / cellSize;
        int cellIndex = Y *  countCellY + X;
        if(X > countCellX - 1 || Y > countCellY - 1 || X < 0 || Y < 0 ){
            return  new Cell(0,0,0, CellStatus.NOT_IN_MAP);
        }
        return cells[cellIndex];
    }
     public Cell[] GetAllCell(){
        return cells;
     }

    public int GetSizeX() {
        return sizeX;
    }

    public int GetSizey(){
        return sizeY;
    }
}
