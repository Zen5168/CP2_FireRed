package com.game.world;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Building {
    
    private String name;
    private int worldX, worldY; // POSITION IN THE OVERWORLD
    private int width, height; // SIZE IN TILES
    private BufferedImage sprite;
    private Rectangle doorArea; // AREA WHERE PLAYER CAN ENTER
    private String interiorMapPath; // PATH TO INTERIOR MAP FILE
    private Point interiorSpawnPoint; // WHERE PLAYER SPAWNS INSIDE
    private Point exitSpawnPoint; // WHERE PLAYER SPAWNS WHEN EXITING
    
    public Building(String name, int worldX, int worldY, int width, int height, 
                   BufferedImage sprite, Rectangle doorArea, String interiorMapPath,
                   Point interiorSpawnPoint, Point exitSpawnPoint) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.doorArea = doorArea;
        this.interiorMapPath = interiorMapPath;
        this.interiorSpawnPoint = interiorSpawnPoint;
        this.exitSpawnPoint = exitSpawnPoint;
    }
    
    // CHECK IF PLAYER IS AT THE DOOR
    public boolean isPlayerAtDoor(int playerTileX, int playerTileY) {
        int buildingTileX = worldX / 48; 
        int buildingTileY = worldY / 48;
        
        int doorTileX = buildingTileX + (doorArea.x / 48);
        int doorTileY = buildingTileY + (doorArea.y / 48);
        
        return playerTileX == doorTileX && playerTileY == doorTileY;
    }
    
    //=============================
    // GETTERS
    //=============================
    public String getName() { return name; }
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public BufferedImage getSprite() { return sprite; }
    public Rectangle getDoorArea() { return doorArea; }
    public String getInteriorMapPath() { return interiorMapPath; }
    public Point getInteriorSpawnPoint() { return interiorSpawnPoint; }
    public Point getExitSpawnPoint() { return exitSpawnPoint; }
}
