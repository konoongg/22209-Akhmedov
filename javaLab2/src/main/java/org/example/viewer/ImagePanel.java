package org.example.viewer;

import org.example.Sprite;
import org.example.characters.ICharacter;
import org.example.enemy.IEnemy;

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
    private final Sprite mapSprite;
    private final ArrayList<IEnemy> enemyList;
    private ArrayList<ICharacter> characterList;
    public ImagePanel(Sprite mapSprite, ArrayList<IEnemy> enemyList,  ArrayList<ICharacter> characterList) {
        try {
            URL mapURL = Viewer.class.getResource(mapSprite.Path());
            if(mapURL == null){
                throw new RuntimeException(new IOException());
            }
            map = ImageIO.read(mapURL);
            this.characterList = characterList;
            this.mapSprite = mapSprite;
            this.enemyList = enemyList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void PrintMap(Graphics g){
        if (map != null) {
            g.drawImage(map, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void PrintEnemys(Graphics g) throws IOException {
        if(!enemyList.isEmpty()){
            for (IEnemy enemy : enemyList){
                double enemyX = enemy.CoordsX();
                double enemyY = enemy.CoordsY();
                Sprite sprite = enemy.Sprite();
                double startX = enemyX / mapSprite.SizeX() * getWidth();
                double startY = enemyY / mapSprite.SizeY() * getHeight();
                double endX = (enemyX + sprite.SizeX()) / mapSprite.SizeX() * getWidth();
                double endY = (enemyY + sprite.SizeY()) / mapSprite.SizeY() * getHeight();
                URL enemyURL = Viewer.class.getResource(sprite.Path());
                if(enemyURL == null){
                    throw new RuntimeException(new IOException());
                }
                BufferedImage enemyImage = ImageIO.read(enemyURL);
                g.drawImage(enemyImage, (int)startX, (int)startY, (int)endX, (int)endY, this);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        PrintMap(g);
        try {
            PrintEnemys(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}