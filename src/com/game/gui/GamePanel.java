package com.game.gui;

import javax.swing.*;
import java.awt.*;
import com.game.logic.*;

public class GamePanel extends JPanel implements Runnable {

    //========================================
    // SCREEN SETTINGS
    //========================================
    final int originalTileSize = 16; // 16x16 Tile
    final int scale = 3; // TILE SCALING 

    final int tileSize = originalTileSize * scale; // MAKES THE TILE 48x48
    final int maxScreenCol = 16; // 16 TILES HORIZONTALLY
    final int maxScreenRow = 12; // 16 TILES VERTICALLY
    final int screenWidth = tileSize * maxScreenCol; // 768 PIXELS
    final int screenHeight = tileSize * maxScreenRow; // 576 PIXELS

    //========================================
    // FPS
    //========================================
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    //========================================
    // SET PLAYER'S DEFAULT LOCATION
    //========================================
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // PREVENTS FLICKERING (BETTER RENDERING)
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    //========================================
    // FPS LIMITER / GAME LOOP (DELTA METHOD)
    //========================================
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount); // FPS DISPLAY
                drawCount = 0;
                timer = 0;
            }
        }

    }

    public void update() {

        if (keyH.upPressed == true) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed == true) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed == true) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed == true) {
            playerX += playerSpeed;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);

        g2.fillRect(playerX, playerY, tileSize, tileSize);

        g2.dispose(); // DISPOSES GRAPHICS CONTEXT
    }
}
