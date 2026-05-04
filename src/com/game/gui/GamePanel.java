package com.game.gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //=========================
    // SCREEN SETTINGS
    //=========================
    final int originalTileSize = 16; // 16x16 Tile
    final int scale = 3; // TILE SCALING 

    final int tileSize = originalTileSize * scale; // MAKES THE TILE 48x48
    final int maxScreenCol = 16; // 16 TILES HORIZONTALLY
    final int maxScreenRow = 12; // 16 TILES VERTICALLY
    final int screenWidth = tileSize * maxScreenCol; // 768 PIXELS
    final int screenHeight = tileSize * maxScreenRow; // 576 PIXELS

    Thread gameThread;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // PREVENTS FLICKERING (BETTER RENDERING)
    }

    public void startGameThread() {
        
        gameThread = new Thread (this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        
        
    }
}
