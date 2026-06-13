package com.game.world;

import java.awt.image.BufferedImage;

public class Building {
    
    private String name;
    private int worldX, worldY; // POSITION IN THE OVERWORLD
    private int width, height; // SIZE IN TILES
    private BufferedImage sprite;
    
    public Building(String name, int worldX, int worldY, int width, int height, 
                   BufferedImage sprite) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }
    
    //=============================
    // GETTERS
    //=============================
    public String getName() { 
        return name; 
    }
    public int getWorldX() { 
        return worldX; 
    }
    public int getWorldY() { 
        return worldY; 
    }
    public int getWidth() { 
        return width; 
    }
    public int getHeight() { 
        return height; 
    }
    public BufferedImage getSprite() { 
        return sprite; 
    }
}
