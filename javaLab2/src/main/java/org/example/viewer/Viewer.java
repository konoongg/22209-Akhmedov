package org.example.viewer;
import org.example.*;
import org.example.characters.CharactersParams;
import org.example.characters.ICharacter;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.GameMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.max;

public class Viewer {
    private JFrame gameField;
    private ImagePanel map;
    private JScrollPane buyList;
    private boolean visibleNet;
    private String createdName;
    private ViewerListener listener;
    private JPanel gameText;

    private String ReturnCharName(JPanel charPanel) {
        JPanel panel = (JPanel) charPanel.getComponent(1);
        JPanel labelPanel = (JPanel) (panel.getComponent(0));
        JLabel label = (JLabel) (labelPanel.getComponent(0));
        String[] partOfString = label.getText().split(" ");
        String name = partOfString[1];
        return name;
    }

    private void DoVisibleNet(){
        visibleNet = true;
    }

    private void CreateLabel( JPanel info, String text, boolean visible, Color bgColor, boolean border){
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(1, 1));
        labelPanel.setPreferredSize(new Dimension(info.getWidth(), info.getHeight() / 2));
        JLabel label = new JLabel(text);
        label.setOpaque(visible);
        labelPanel.setBackground(bgColor);
        label.setBackground(bgColor);
        if (border) {
            label.setBorder(new LineBorder(Color.BLACK));
        }
        labelPanel.add(label);
        info.add(labelPanel);
    }

    private void ChangeGameText(String newText){
        JPanel labelPanel = (JPanel) (gameText.getComponent(2));
        JLabel label = (JLabel) (labelPanel.getComponent(0));
        label.setText(newText);
    }

    public void ChangeMoney(int newMoney){
        JPanel labelPanel = (JPanel) (gameText.getComponent(1));
        JLabel label = (JLabel) (labelPanel.getComponent(0));
        label.setText("money: " + newMoney);
    }

    public void ChangeTime(int h, int m, int s){
        JPanel labelPanel = (JPanel) (gameText.getComponent(3));
        JLabel label = (JLabel) (labelPanel.getComponent(0));
        label.setText("time in game:" + h +" h " + m + " m " + s + "s");
    }

    public void ChangeHp (int newHp){
        JPanel labelPanel = (JPanel) (gameText.getComponent(0));
        JLabel label = (JLabel) (labelPanel.getComponent(0));
        label.setText("Hp: " + newHp);
    }

    private void CreateInfPanel(JPanel buyListCont, int blockHeight, Player player, String startText){
        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(0, blockHeight));
        infoPanel.setBorder(new LineBorder(Color.BLACK));
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setLayout(new GridLayout(4, 1));
        infoPanel.setBorder(new LineBorder(Color.BLACK));
        CreateLabel(infoPanel, "Hp: " + player.GetHp(), false,  Color.GRAY, false);
        CreateLabel(infoPanel, "money: " + player.GetMoney(), false,  Color.GRAY, false);
        CreateLabel(infoPanel,  startText, true, Color.WHITE, true);
        CreateLabel(infoPanel,  "time in game: 0 h 0 m 0 s", true, Color.GREEN, true);
        buyListCont.add(infoPanel);
        gameText = infoPanel;
    }

    private void CreateIcon(JPanel charPanel,GridBagConstraints gbc,  CharactersParams params){
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel icon = new IconPanel(params.GetPathFolder() + "icon.png");
        icon.setBorder(new LineBorder(Color.BLACK));
        charPanel.add(icon, gbc);
    }

    private void CreateInfo(JPanel charPanel,GridBagConstraints gbc,  CharactersParams params){
        JPanel info = new JPanel();
        gbc.gridx = 1;
        gbc.weighty = 1;
        gbc.weightx = 0.7;
        info.setBackground(Color.GRAY);
        info.setLayout(new GridLayout(4, 1));
        info.setBorder(new LineBorder(Color.BLACK));
        CreateLabel(info, "NAME: " + params.GetName(), true, Color.YELLOW, false);
        CreateLabel(info, "DAMAGE: " + params.GetDamage(), false, Color.GRAY, false);
        CreateLabel(info, "DELAY: " + (float)params.GetDelay() / 1000, false,  Color.GRAY, false);
        CreateLabel(info, "COAST: " + params.GetCoast(), false,  Color.GRAY, false);
        charPanel.add(info, gbc);
    }

    private void CreateCharPanel(GameStat gameStat, JPanel buyListCont,  int blockHeight, CharactersParams params ){
        JPanel charPanel = new JPanel();
        charPanel.setPreferredSize(new Dimension(buyListCont.getWidth(), blockHeight));
        charPanel.setBackground(Color.RED);
        charPanel.setBorder(new LineBorder(Color.BLACK));
        charPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        CreateIcon(charPanel, gbc, params);
        CreateInfo(charPanel, gbc, params);
        charPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createdName = ReturnCharName(charPanel);
                DoVisibleNet();
            }
        });
        buyListCont.add(charPanel);
    }

    private void CreateList(GameStat gameStat){
        Map<String, CharactersParams> unicCharacters = gameStat.ReturnUnicCharacters();
        JPanel buyListCont = new JPanel();
        buyListCont.setBackground(Color.DARK_GRAY);
        int countRows = max(4, unicCharacters.size());
        buyListCont.setLayout(new GridLayout(countRows, 1));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int blockHeight = screenSize.height / 5;
        CreateInfPanel(buyListCont, blockHeight, gameStat.ReturnPlayer(), "StartGame");
        for(Map.Entry<String, CharactersParams> entry : unicCharacters.entrySet()){
           CreateCharPanel(gameStat, buyListCont, blockHeight, entry.getValue() );
        }
        buyList = new JScrollPane(buyListCont);
        buyList.setBorder(new LineBorder(Color.BLACK));
        buyList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buyList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void RepaintMap(GameStat gameStat){
        ArrayList<IEnemy> enemyList = gameStat.ReturnEnemyList();
        ArrayList<ICharacter> characterList = gameStat.ReturnCharacterList();
        Cell[] cells = gameStat.ReturnMap().GetAllCell();
        map.Repaint(enemyList, cells, characterList,  visibleNet, gameStat.ReturnMap().GetCellSize());
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
        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(visibleNet){
                    float x = (float)e.getX() / map.getWidth();
                    float y = (float)e.getY() / map.getHeight();
                    Coords coordsClick = new Coords(x, y);
                    String message = listener.HearCreateChar(createdName, coordsClick);
                    ChangeGameText(message);
                    visibleNet = false;
                }
            }
        });
    }

    public void CreateFinal(){
        gameField.getContentPane().removeAll();
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String path = "/Images/Bg/final.png";
                URL bgURL = Viewer.class.getResource(path);
                Image bg;
                try {
                    bg = ImageIO.read(bgURL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        JButton exit = new JButton("выход");
        JButton restart = new JButton("рестарт");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    listener.Restart();
                }
                catch (Exception err){
                    throw new RuntimeException("error restart" + err);
                }
            }
        });
        backgroundPanel.add(exit);
        backgroundPanel.add(restart);
        backgroundPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gameField.add(backgroundPanel, gbc);
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
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public void Restart(GameStat gamestat) throws IOException {
        //visibleNet = false;
        gameField.getContentPane().removeAll();
        CreateFieldStructur(gamestat);
        gameField.revalidate();
        gameField.repaint();
        gameField.setVisible(true);
    }

    public void Start(GameStat gamestat, ViewerListener listener) throws IOException {
        visibleNet = false;
        this.listener = listener;
        GameMap gameMap = gamestat.ReturnMap();
        gameField = new JFrame(gameMap.GetName());
        gameField.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameField.setExtendedState(JFrame.MAXIMIZED_BOTH);
        KeyListener();
        CreateFieldStructur(gamestat);
        gameField.setVisible(true);
    }
}
