package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel(URL imagePath) {
        try {
            image = ImageIO.read(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Рисование изображения внутри панели
        }
    }
}