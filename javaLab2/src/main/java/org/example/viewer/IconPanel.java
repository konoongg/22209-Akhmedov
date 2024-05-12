package org.example.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class IconPanel extends JPanel {
    private final BufferedImage icon;
    public IconPanel(String path) {
        try {
            URL iconURL = Viewer.class.getResource(path);
            if(iconURL == null){
                throw new RuntimeException(new IOException());
            }
            icon = ImageIO.read(iconURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void PrintIcon(Graphics g){
        if (icon != null) {
            g.drawImage(icon, 0, 0, getWidth(), getHeight(), this);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        PrintIcon(g);
    }
}