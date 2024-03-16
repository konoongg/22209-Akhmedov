package org.example.map;

import org.example.Coords;

import java.util.ArrayList;

public class Cell {
    Coords start;
    Coords end;
    CellStatus status;
    ArrayList<CellEffect> effects;

    public Cell(int numCellX, int numCellY, int sizeCell, CellStatus status){
        start = new Coords(sizeCell * numCellX, sizeCell * numCellY);
        end = new Coords(sizeCell * (numCellX + 1), sizeCell * (numCellY + 1));
        this.status = status;
        effects = new ArrayList<>();
    }
    public void ChangeStatus(CellStatus newStatus){
        status = newStatus;
    }
    public CellStatus GetStatus(){
        return status;
    }
    public void AddEffect(CellEffect effect){
        effects.add(effect);
    }
    public void DeleteEffect(CellEffect effect){
        effects.remove(effect);
    }
}