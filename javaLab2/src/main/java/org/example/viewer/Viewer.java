package org.example.viewer;
import org.example.Sprite;
import org.example.characters.ICharacter;
import org.example.enemy.IEnemy;
import org.example.map.GameMap;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Viewer {
    private JFrame gameField;
    private JPanel map;
    private JPanel buyList;
    private  URL mapUrl;
   private void CreateList(){
        buyList = new JPanel();
        buyList.setBackground(Color.DARK_GRAY);
        map.setBorder(new LineBorder(Color.BLACK));
    }
    public void CreateFieldStructur(Sprite mapSprite, ArrayList<IEnemy> enemyList,  ArrayList<ICharacter> characterList) throws IOException {
        gameField.getContentPane().removeAll();
        gameField.setLayout(new GridBagLayout());
        CreateMap(mapSprite, enemyList, characterList);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gameField.add(map, gbc);
        CreateList();
        gbc.gridx = 1;
        gbc.weighty = 1;
        gbc.weightx = 0.3;
        gameField.add(buyList, gbc);
        gameField.revalidate();
        gameField.repaint();
    }

    private void CreateMap(Sprite mapSprite, ArrayList<IEnemy> enemyList,  ArrayList<ICharacter> characterList){
        map = new ImagePanel(mapSprite, enemyList, characterList);
        map.setBackground(Color.BLUE);
        map.setBorder(new LineBorder(Color.BLACK));
    }

    public void CreateFinal(){
        gameField.getContentPane().removeAll();

        gameField.revalidate();
        gameField.repaint();
    }

    public void Start(GameMap gameMap, ArrayList<IEnemy> enemyList,  ArrayList<ICharacter> characterList) throws IOException {
        gameField = new JFrame(gameMap.GetName());
        gameField.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameField.setExtendedState(JFrame.MAXIMIZED_BOTH);
        CreateFieldStructur(gameMap.GetSprite(), enemyList, characterList);
        gameField.setVisible(true);
    }
}
