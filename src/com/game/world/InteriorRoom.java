package com.game.world;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class InteriorRoom {

    private String name;
    private BufferedImage roomSprite; // THE COMPLETE INTERIOR ROOM IMAGE
    private int spriteX, spriteY; // POSITION IN THE SPRITE SHEET
    private int spriteWidth, spriteHeight; // SIZE IN PIXELS ON SPRITE SHEET
    private int roomWidthTiles, roomHeightTiles; // SIZE IN GAME TILES
    private ArrayList<Point> exitDoorTiles; // MULTIPLE EXIT DOOR TILES
    private Point playerSpawnTile; // WHERE PLAYER SPAWNS WHEN ENTERING
    private int[][] collisionMap; // 2D ARRAY: 0 = WALKABLE, 1 = COLLISION

    //=============================
    // CONSTRUCTOR 
    //=============================
    public InteriorRoom(String name, int spriteX, int spriteY,
            int spriteWidth, int spriteHeight,
            int roomWidthTiles, int roomHeightTiles,
            Point[] exitDoorTiles, Point playerSpawnTile) {
        this.name = name;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.roomWidthTiles = roomWidthTiles;
        this.roomHeightTiles = roomHeightTiles;
        this.exitDoorTiles = new ArrayList<>(Arrays.asList(exitDoorTiles));
        this.playerSpawnTile = playerSpawnTile;
    }

    // CHECK IF A TILE HAS COLLISION
    public boolean hasTileCollision(int tileX, int tileY) {
        if (collisionMap == null) {
            return false; // NO COLLISION MAP LOADED
        }

        // CHECK BOUNDS - OUT OF BOUNDS = COLLISION
        if (tileX < 0 || tileX >= roomWidthTiles || tileY < 0 || tileY >= roomHeightTiles) {
            return true;
        }

        // RETURN TRUE IF COLLISION VALUE IS 1
        return collisionMap[tileY][tileX] == 1;
    }

    // CHECK IF PLAYER IS AT ANY EXIT DOOR
    public boolean isPlayerAtExit(int playerTileX, int playerTileY) {
        for (Point exitTile : exitDoorTiles) {
            if (playerTileX == exitTile.x && playerTileY == exitTile.y) {
                return true;
            }
        }
        return false;
    }

    //=============================
    // GETTERS AND SETTERS
    //=============================
    public void setRoomSprite(BufferedImage sprite) {
        this.roomSprite = sprite;
    }

    public void setCollisionMap(int[][] collisionMap) {
        this.collisionMap = collisionMap;
    }
    
    public String getName() {
        return name;
    }

    public BufferedImage getRoomSprite() {
        return roomSprite;
    }

    public int getSpriteX() {
        return spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getRoomWidthTiles() {
        return roomWidthTiles;
    }

    public int getRoomHeightTiles() {
        return roomHeightTiles;
    }

    public Point getExitDoorTile() {
        return exitDoorTiles.get(0);
    } // RETURN FIRST EXIT FOR COMPATIBILITY

    public ArrayList<Point> getExitDoorTiles() {
        return exitDoorTiles;
    } // RETURN ALL EXITS

    public Point getPlayerSpawnTile() {
        return playerSpawnTile;
    }

    public int[][] getCollisionMap() {
        return collisionMap;
    }
}
