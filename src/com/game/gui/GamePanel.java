package com.game.gui;

import javax.swing.*;
import java.awt.*;
import com.game.logic.*;
import com.game.entity.*;
import com.game.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    //========================================
    // SCREEN SETTINGS
    //========================================
    final int originalTileSize = 16; // 16x16 Tile
    final int scale = 3; // TILE SCALING 

    public final int tileSize = originalTileSize * scale; // MAKES THE TILE 48x48
    public final int maxScreenCol = 16; // 16 TILES HORIZONTALLY
    public final int maxScreenRow = 12; // 12 TILES VERTICALLY
    public int screenWidth = tileSize * maxScreenCol; // 768 PIXELS
    public int screenHeight = tileSize * maxScreenRow; // 576 PIXELS

    //========================================
    // WORLD SETTINGS
    //========================================
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol; 
    public final int worldHeight = tileSize * maxWorldRow; 
    
    //========================================
    // FPS
    //========================================
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyH);

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

        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        player.draw(g2);

        g2.dispose(); // DISPOSES GRAPHICS CONTEXT
    }
}
