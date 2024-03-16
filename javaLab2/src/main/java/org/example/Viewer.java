package org.example;
import org.example.map.GameMap;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Viewer {
    private JFrame gameField;

    private void ViewField(GameMap gameMap) throws IOException {
        GridBagLayout layout = new GridBagLayout();
        gameField.setLayout(layout);
        JPanel map = new JPanel(new GridBagLayout());
        URL imageUrl = Viewer.class.getResource("/Images/map.png");
        if(imageUrl == null){
            throw new IOException("cant open map sprite");
        }
        ImageIcon icon = new ImageIcon(imageUrl);
        JLabel label = new JLabel(icon);
        //именить параметры

        GridBagConstraints params = new GridBagConstraints();
        params.gridx = 0; // установите x-координату в 0
        params.gridy = 0; // установите y-координату в 0
        params.anchor = GridBagConstraints.SOUTHWEST; // зафиксируйте компонент в юго-западном углу
        params.fill = GridBagConstraints.BOTH; // растягивайте компонент на всю доступную область
        params.weightx = 1.0; // установите вес по x-оси в 1.0
        params.weighty = 1.0;
        map.add(label);
        gameField.add(map, params);
    }
    public void start(GameMap gameMap) throws IOException {
        gameField = new JFrame(gameMap.GetName());
        gameField.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameField.setSize(gameMap.GetSizeX() + 200, gameMap.GetSizeY() + 100);
        ViewField(gameMap);
        gameField.setVisible(true);
    }
}
