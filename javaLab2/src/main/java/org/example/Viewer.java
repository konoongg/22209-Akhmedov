package org.example;
import org.example.map.GameMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Viewer {
    private JFrame gameField;
    private JPanel map;

    private JPanel charactersList;

    private void CreateMap(){
        BufferedImage image;
        map = new JPanel();
        map.setBackground(Color.BLUE);
        map.setBorder(new LineBorder(Color.BLACK));
    }

    private void CreateList(){
        charactersList = new JPanel();
        charactersList.setBackground(Color.DARK_GRAY);
        map.setBorder(new LineBorder(Color.BLACK));
    }
    private void CreateFielStructur(GameMap gameMap) throws IOException {
        gameField.setLayout(new GridBagLayout());
        CreateMap();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 0.7; // 70% ширины
        gbc.fill = GridBagConstraints.BOTH;
        gameField.add(map, gbc);
        CreateList();
        gbc.gridx = 1;
        gbc.weighty = 1;
        gbc.weightx = 0.3;
        gameField.add(charactersList, gbc);
    }

    public void start(GameMap gameMap) throws IOException {
        gameField = new JFrame(gameMap.GetName());
        gameField.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameField.setExtendedState(JFrame.MAXIMIZED_BOTH);
        CreateFielStructur(gameMap);
        //CreateCharactersList();
        gameField.setVisible(true);
    }
}
