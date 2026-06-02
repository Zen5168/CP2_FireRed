package com.game.entity;

import com.game.main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC extends Entity {
    
    protected GamePanel gp;
    protected String name;
    protected String npcType; // "NURSE", "CLERK", etc.
    protected BufferedImage sprite;
    
    // INTERACTION RANGE
    protected int interactionRange = 2; // TILES RANGE
    
    public NPC(GamePanel gp, String name, String npcType, int worldX, int worldY) {
        this.gp = gp;
        this.name = name;
        this.npcType = npcType;
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = "down";
        
        // DEFAULT COLLISION BOX FOR NPCS
        solidArea = new Rectangle(8, 16, 32, 32);
    }
    
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
    
    public boolean isPlayerNearby() {
        int playerTileX = gp.player.worldX / gp.tileSize;
        int playerTileY = gp.player.worldY / gp.tileSize;
        int npcTileX = worldX / gp.tileSize;
        int npcTileY = worldY / gp.tileSize;
        
        int distance = Math.abs(playerTileX - npcTileX) + Math.abs(playerTileY - npcTileY);
        return distance <= interactionRange;
    }
    
    public boolean isPlayerFacingNPC() {
        int playerTileX = gp.player.worldX / gp.tileSize;
        int playerTileY = gp.player.worldY / gp.tileSize;
        int npcTileX = worldX / gp.tileSize;
        int npcTileY = worldY / gp.tileSize;
        
        String playerDir = gp.player.direction;
        
        // CHECK IF PLAYER IS FACING THE NPC WITHIN INTERACTION RANGE
        // ALLOWS INTERACTION ACROSS COUNTERS (UP TO 2 TILES AWAY)
        switch (playerDir) {
            case "up":
                return playerTileX == npcTileX && 
                       playerTileY > npcTileY && 
                       (playerTileY - npcTileY) <= interactionRange;
            case "down":
                return playerTileX == npcTileX && 
                       playerTileY < npcTileY && 
                       (npcTileY - playerTileY) <= interactionRange;
            case "left":
                return playerTileY == npcTileY && 
                       playerTileX > npcTileX && 
                       (playerTileX - npcTileX) <= interactionRange;
            case "right":
                return playerTileY == npcTileY && 
                       playerTileX < npcTileX && 
                       (npcTileX - playerTileX) <= interactionRange;
        }
        return false;
    }
    
    public void interact() {
    }
    
    public void draw(Graphics2D g2) {
        if (sprite != null) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            // ONLY DRAW IF ON SCREEN
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                g2.drawImage(sprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
    
     //==================
     // GETTERS
     //==================
    public String getName() {
        return name;
    }
    
    public String getNpcType() {
        return npcType;
    }
}
