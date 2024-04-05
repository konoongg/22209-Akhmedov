package org.example.viewer;
import org.example.GameStat;
import org.example.Sprite;
import org.example.characters.CharactersParams;
import org.example.characters.ICharacter;
import org.example.enemy.IEnemy;
import org.example.map.GameMap;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Math.max;

public class Viewer {
    private JFrame gameField;
    private JPanel map;
    private JScrollPane buyList;
    private  URL mapUrl;
    private void CreateList(ArrayList<CharactersParams> unicCharacters){
        JPanel buyListCont = new JPanel();
        buyListCont.setBackground(Color.DARK_GRAY);
        int countRows = max(5, unicCharacters.size());
        buyListCont.setLayout(new GridLayout(20, 1));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int blockHeight = screenSize.height / 5;
        for(int i = 0; i < 20; ++i){
            JPanel imagePanel = new JPanel();
            imagePanel.setPreferredSize(new Dimension(0, blockHeight)); // Установите предпочтительный размер для JPanel
            imagePanel.setBackground(Color.RED);
            imagePanel.setBorder(new LineBorder(Color.BLACK));
            buyListCont.add(imagePanel);
        }
        buyList = new JScrollPane(buyListCont);
        buyList.setBorder(new LineBorder(Color.BLACK));
        buyList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buyList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void CreateFieldStructur(GameStat gameStat) throws IOException {
        Sprite mapSprite = gameStat.ReturnMap().GetSprite();
        ArrayList<IEnemy> enemyList = gameStat.ReturnEnemyList();
        ArrayList<ICharacter> characterList = gameStat.ReturnCharacterList();
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
        CreateList(gameStat.ReturnUnicCharacters());
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

    public void Start(GameStat gamestat) throws IOException {
        GameMap gameMap = gamestat.ReturnMap();
        gameField = new JFrame(gameMap.GetName());
        gameField.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameField.setExtendedState(JFrame.MAXIMIZED_BOTH);
        CreateFieldStructur(gamestat);
        gameField.setVisible(true);
    }
}
