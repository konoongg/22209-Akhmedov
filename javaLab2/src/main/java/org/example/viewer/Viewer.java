package org.example.viewer;
import org.example.GameStat;
import org.example.Sprite;
import org.example.characters.CharactersParams;
import org.example.characters.ICharacter;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.GameMap;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.max;

public class Viewer {
    private JFrame gameField;
    private JPanel map;
    private JScrollPane buyList;
    private boolean visibleNet;
    private String charName;

    private String ReturnName(JPanel charPanel) {
        JPanel panel = (JPanel) charPanel.getComponent(1);
        JLabel label = (JLabel) (panel.getComponent(0));
        String[] partOfString = label.getText().split(" ");
        String name = partOfString[1];
        return name;
    }

    private void CreateIcon(JPanel charPanel,GridBagConstraints gbc,  CharactersParams params){
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel icon = new IconPanel(params.GetPath());
        icon.setBorder(new LineBorder(Color.BLACK));
        charPanel.add(icon, gbc);
    }

    private void CreateLabel( JPanel info, String text, boolean visidle){
        JLabel label = new JLabel(text);
        label.setOpaque(visidle);
        label.setBackground(Color.YELLOW);
        info.add(label);

    }
    private void CreateInfo(JPanel charPanel,GridBagConstraints gbc,  CharactersParams params){
        JPanel info = new JPanel();
        gbc.gridx = 1;
        gbc.weighty = 1;
        gbc.weightx = 0.7;
        info.setBackground(Color.GRAY);
        info.setLayout(new GridLayout(4, 1));
        info.setBorder(new LineBorder(Color.BLACK));
        CreateLabel(info, "NAME: " + params.GetName(), true);
        CreateLabel(info, "DAMAGE: " + params.GetDamage(), false);
        CreateLabel(info, "DELAY: " + params.GetDelay() / 1000, false);
        CreateLabel(info, "COAST: " + params.GetCoast(), false);
        charPanel.add(info, gbc);
    }


    private void CreateCharPanel(GameStat gameStat, JPanel buyListCont,  int blockHeight, CharactersParams params ){
        JPanel charPanel = new JPanel();
        charPanel.setPreferredSize(new Dimension(0, blockHeight)); // Установите предпочтительный размер для JPanel
        charPanel.setBackground(Color.RED);
        charPanel.setBorder(new LineBorder(Color.BLACK));
        charPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        CreateIcon(charPanel, gbc, params);
        CreateInfo(charPanel, gbc, params);
        charPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                charName = ReturnName(charPanel);
                visibleNet = true;
            }
        });
        buyListCont.add(charPanel);
    }

    private void CreateList(GameStat gameStat){
        ArrayList<CharactersParams> unicCharacters = gameStat.ReturnUnicCharacters();
        JPanel buyListCont = new JPanel();
        buyListCont.setBackground(Color.DARK_GRAY);
        int countRows = max(4, unicCharacters.size());
        buyListCont.setLayout(new GridLayout(countRows, 1));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int blockHeight = screenSize.height / 5;
        for(CharactersParams params : unicCharacters){
           CreateCharPanel(gameStat, buyListCont, blockHeight, params );
        }
        buyList = new JScrollPane(buyListCont);
        buyList.setBorder(new LineBorder(Color.BLACK));
        buyList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buyList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void RepaintMap(GameStat gameStat){
        gameField.getContentPane().remove(map);
        Sprite mapSprite = gameStat.ReturnMap().GetSprite();
        ArrayList<IEnemy> enemyList = gameStat.ReturnEnemyList();
        ArrayList<ICharacter> characterList = gameStat.ReturnCharacterList();
        Cell[] cells = gameStat.ReturnMap().GetAllCell();
        CreateMap(mapSprite, enemyList, characterList, cells);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gameField.add(map, gbc);
        gameField.revalidate();
        gameField.repaint();
    }
    private void CreateFieldStructur(GameStat gameStat) throws IOException {
        Sprite mapSprite = gameStat.ReturnMap().GetSprite();
        ArrayList<IEnemy> enemyList = gameStat.ReturnEnemyList();
        ArrayList<ICharacter> characterList = gameStat.ReturnCharacterList();
        gameField.setLayout(new GridBagLayout());
        Cell[] cells = gameStat.ReturnMap().GetAllCell();
        CreateMap(mapSprite, enemyList, characterList, cells);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gameField.add(map, gbc);
        CreateList(gameStat);
        gbc.gridx = 1;
        gbc.weighty = 1;
        gbc.weightx = 0.3;
        gameField.add(buyList, gbc);
    }

    private void CreateMap(Sprite mapSprite, ArrayList<IEnemy> enemyList,  ArrayList<ICharacter> characterList, Cell[] cells){
        map = new ImagePanel(mapSprite, enemyList, cells, characterList, visibleNet, 50);
        map.setBackground(Color.BLUE);
        map.setBorder(new LineBorder(Color.BLACK));
    }

    public void CreateFinal(){
        gameField.getContentPane().removeAll();
        gameField.revalidate();
        gameField.repaint();
    }

    private void KeyListener(){
        gameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    visibleNet = false;
                    charName = null;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public void Start(GameStat gamestat) throws IOException {
        visibleNet = false;
        charName = null;
        GameMap gameMap = gamestat.ReturnMap();
        gameField = new JFrame(gameMap.GetName());
        gameField.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameField.setExtendedState(JFrame.MAXIMIZED_BOTH);
        KeyListener();
        CreateFieldStructur(gamestat);
        gameField.setVisible(true);
    }
}
