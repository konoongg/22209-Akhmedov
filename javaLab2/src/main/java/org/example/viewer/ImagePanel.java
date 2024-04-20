package org.example.viewer;

import org.example.Coords;
import org.example.Sprite;
import org.example.characters.CharactersParams;
import org.example.characters.ICharacter;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.CellEffect;
import org.example.map.CellStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private final BufferedImage map;
    private Sprite mapSprite;
    private ArrayList<IEnemy> enemyList;
    private ArrayList<ICharacter> characterList;
    private boolean visibleNet;
    private double sizeNet;

    private Cell[] cells;
    public ImagePanel(Sprite mapSprite, ArrayList<IEnemy> enemyList, Cell[] cells, ArrayList<ICharacter> characterList, boolean visibleNet, double sizeNet ) {
        try {
            URL mapURL = Viewer.class.getResource(mapSprite.Path());
            if(mapURL == null){
                throw new RuntimeException(new IOException());
            }
            map = ImageIO.read(mapURL);
            this.characterList = new ArrayList<>(characterList);
            this.mapSprite = mapSprite;
            this.cells = cells;
            this.enemyList = new ArrayList<>(enemyList);
            this.visibleNet = visibleNet;
            this.sizeNet = sizeNet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Repaint(ArrayList<IEnemy> enemyList, Cell[] cells, ArrayList<ICharacter> characterList, boolean visibleNet, double sizeNet ){
        this.characterList = new ArrayList<>(characterList);
        this.mapSprite = mapSprite;
        this.cells = cells;
        this.enemyList = new ArrayList<>(enemyList);
        this.visibleNet = visibleNet;
        this.sizeNet = sizeNet;
    }

    private void PrintMap(Graphics g){
        if (map != null) {
            g.drawImage(map, 0, 0, getWidth(), getHeight(), this);
        }
    }


    private void PrintCharacter(Graphics g) throws IOException {
        if(!characterList.isEmpty()){
            try{
                for(ICharacter character : characterList){
                    Coords startCoords = character.GetStartCoords();
                    Coords endCoords = character.GetEndCoords();
                    double startX = startCoords.X() / mapSprite.SizeX() * getWidth();
                    double startY = startCoords.Y() / mapSprite.SizeY() * getHeight();
                    double endX = endCoords.X() / mapSprite.SizeX() * getWidth();
                    double endY = endCoords.Y() / mapSprite.SizeY() * getHeight();
                    double weight = endX - startX;
                    double height = endY - startY;
                    Sprite sprite = character.Sprite();
                    URL characterURL = Viewer.class.getResource(sprite.Path());
                    if(characterURL == null){
                        throw new RuntimeException(new IOException());
                    }
                    BufferedImage enemyImage = ImageIO.read(characterURL);
                    g.drawImage(enemyImage, (int)startX, (int)startY, (int)weight, (int)height, this);
                }
            }
            catch(Exception e){
                System.out.println("ERROR " + e);
                throw new IOException(e);
            }
        }
    }

    private void PrintEnemys(Graphics g) throws IOException {
        if(!enemyList.isEmpty()){
            try{
                for (IEnemy enemy : enemyList){
                    double enemyX = enemy.CoordsX();
                    double enemyY = enemy.CoordsY();
                    Sprite sprite = enemy.Sprite();
                    double startX = enemyX / mapSprite.SizeX() * getWidth();
                    double startY = enemyY / mapSprite.SizeY() * getHeight();
                    double endX = (enemyX + sprite.SizeX()) / mapSprite.SizeX() * getWidth();
                    double endY = (enemyY + sprite.SizeY()) / mapSprite.SizeY() * getHeight();
                    double weight = endX- startX;
                    double height = endY - startY;
                    URL enemyURL = Viewer.class.getResource(sprite.Path());
                    if(enemyURL == null){
                        throw new RuntimeException(new IOException());
                    }
                    BufferedImage enemyImage = ImageIO.read(enemyURL);
                    g.drawImage(enemyImage, (int)startX, (int)startY, (int)weight, (int)height, this);
                }
            }
            catch(Exception e){
                System.out.println("ERROR " + e);
                throw new IOException(e);
            }

        }
    }

    private Color CalculateCellColor(CellStatus status){
        if(status == CellStatus.FREE){
            return new Color(0, 255, 0, 128);
        }
        else if(status == CellStatus.BORROW){
            return new Color(255, 50, 230, 64);
        }
        else{
            return new Color(255, 0, 0, 64);
        }
    }

    private void PrintNet(Graphics g){
        for (Cell cell : cells){
            double cofMonX = 1 / (double)mapSprite.SizeX() * getWidth();
            double cofMonY = 1 / (double)mapSprite.SizeY() * getHeight();
            Coords start = cell.GetStartCoords();
            double startX = start.X() * cofMonX;
            double startY = start.Y() * cofMonY;
            Coords end = cell.GetEndCoords();
            double endX = end.X() * cofMonX;
            double endY = end.Y() * cofMonY;
            double weight = endX - startX;
            double height = endY - startY;
            Color cellColor = CalculateCellColor(cell.GetStatus());
            g.setColor(cellColor);
            g.fillRect((int)startX, (int)startY, (int)weight, (int)height);
            g.drawRect((int)startX, (int)startY, (int)weight, (int)height);
        }
    }

    private void PrintMapEffects(Graphics g)  {
        for (Cell cell : cells){
            ArrayList<CellEffect> effects = cell.GetCellEffects();
            if(effects.isEmpty()){
                continue;
            }
            double cofMonX = 1 / (double)mapSprite.SizeX() * getWidth();
            double cofMonY = 1 / (double)mapSprite.SizeY() * getHeight();
            Coords start = cell.GetStartCoords();
            double startX = start.X() * cofMonX;
            double startY = start.Y() * cofMonY;
            Coords end = cell.GetEndCoords();
            double endX = end.X() * cofMonX;
            double endY = end.Y() * cofMonY;
            double weight = endX - startX;
            double height = endY - startY;
            for(CellEffect effect : effects){
                String path = "/Images/Effects/" + String.valueOf(effect) + ".png";
                URL effectURL = Viewer.class.getResource(path);
                if(effectURL == null){
                    throw new RuntimeException(new IOException());
                }
                BufferedImage effectImage;
                try {
                    effectImage = ImageIO.read(effectURL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(effectImage, (int)startX, (int)startY, (int)weight, (int)height, this);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        PrintMap(g);
        PrintMapEffects(g);
        try {
            PrintEnemys(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(visibleNet){
            PrintNet(g);
        }
        else{
            try {
                PrintCharacter(g);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}