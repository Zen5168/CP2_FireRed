package com.game.tile;

import com.game.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[100];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/res/maps/testMap.txt");
    }

    // =====================================
    // TILE SPRITE
    // =====================================
    private void getTileImage() {

        try {
            BufferedImage tileSheet1 = ImageIO.read(getClass().getResourceAsStream("/res/image/Tileset.png"));
            BufferedImage tileSheet2 = ImageIO.read(getClass().getResourceAsStream("/res/image/Tileset2.png")); // (PLACEHOLDER)
            BufferedImage buildingSheet = ImageIO
                    .read(getClass().getResourceAsStream("/res/image/buildings_sprites.png"));

            // MAP EACH TILE INDEX TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            Rectangle[] tileSet1 = {
                new Rectangle(1, 52, 16, 16), // Tile[0] - GRASS PATH LIGHT GREEN
                new Rectangle(103, 1, 16, 16), // Tile[1] - GRASS PATH VIBRANT GREEN
                new Rectangle(120, 1, 16, 16), // Tile[2] - TALL GRASS
                new Rectangle(290, 1, 16, 16), // Tile[3] - BUSH
            };

            // BUILDING TILES (16x16 sprites from buildings_sprites.png)
            // Pokemon Center (4x4 tiles = 16 tiles total, index 4-19)
            Rectangle[] pokeCenterTiles = {
                // Row 1 (Top)
                new Rectangle(30, 768, 16, 16), // Tile[4] - Top-left
                new Rectangle(46, 768, 16, 16), // Tile[5] - Top-center-left
                new Rectangle(62, 768, 16, 16), // Tile[6] - Top-center-right
                new Rectangle(78, 768, 16, 16), // Tile[7] - Top-right
                // Row 2
                new Rectangle(30, 784, 16, 16), // Tile[8] - Mid-left
                new Rectangle(46, 784, 16, 16), // Tile[9] - Mid-center-left
                new Rectangle(62, 784, 16, 16), // Tile[10] - Mid-center-right
                new Rectangle(78, 784, 16, 16), // Tile[11] - Mid-right
                // Row 3
                new Rectangle(30, 800, 16, 16), // Tile[12] - Lower-left
                new Rectangle(46, 800, 16, 16), // Tile[13] - Lower-center-left
                new Rectangle(62, 800, 16, 16), // Tile[14] - Lower-center-right
                new Rectangle(78, 800, 16, 16), // Tile[15] - Lower-right
                // Row 4 (Bottom with door)
                new Rectangle(30, 816, 16, 16), // Tile[16] - Bottom-left
                new Rectangle(46, 816, 16, 16), // Tile[17] - Bottom-center-left (DOOR)
                new Rectangle(62, 816, 16, 16), // Tile[18] - Bottom-center-right
                new Rectangle(78, 816, 16, 16), // Tile[19] - Bottom-right
            };

            // PokeMart (4x4 tiles = 16 tiles total, index 20-35)
            Rectangle[] pokeMartTiles = {
                // Row 1 (Top)
                new Rectangle(31, 880, 16, 16), // Tile[20] - Top-left
                new Rectangle(47, 880, 16, 16), // Tile[21] - Top-center-left
                new Rectangle(63, 880, 16, 16), // Tile[22] - Top-center-right
                new Rectangle(79, 880, 16, 16), // Tile[23] - Top-right
                // Row 2
                new Rectangle(31, 896, 16, 16), // Tile[24] - Mid-left
                new Rectangle(47, 896, 16, 16), // Tile[25] - Mid-center-left
                new Rectangle(63, 896, 16, 16), // Tile[26] - Mid-center-right
                new Rectangle(79, 896, 16, 16), // Tile[27] - Mid-right
                // Row 3
                new Rectangle(31, 912, 16, 16), // Tile[28] - Lower-left
                new Rectangle(47, 912, 16, 16), // Tile[29] - Lower-center-left
                new Rectangle(63, 912, 16, 16), // Tile[30] - Lower-center-right
                new Rectangle(79, 912, 16, 16), // Tile[31] - Lower-right
                // Row 4 (Bottom with door)
                new Rectangle(31, 928, 16, 16), // Tile[32] - Bottom-left
                new Rectangle(47, 928, 16, 16), // Tile[33] - Bottom-center-left (DOOR)
                new Rectangle(63, 928, 16, 16), // Tile[34] - Bottom-center-right
                new Rectangle(79, 928, 16, 16), // Tile[35] - Bottom-right
            };

            for (int i = 0; i < tileSet1.length; i++) {
                tile[i] = new Tile();
                Rectangle r = tileSet1[i];
                tile[i].image = tileSheet1.getSubimage(r.x, r.y, r.width, r.height);
            }

            // FUTURE USE
            // // CONTINUES THE INDEX COUNTING
            // int startIndex = tileSet1.length;
            // for (int i = 0; i < tileSet2.length; i++) {
            // int tileIndex = startIndex + i;
            // tile[tileIndex] = new Tile();
            // Rectangle r = tileSet2[i];
            // tile[tileIndex].image = tileSheet2.getSubimage(r.x, r.y, r.width, r.height);
            // }
            // LOAD POKEMON CENTER TILES (INDEX 4-19)
            for (int i = 0; i < pokeCenterTiles.length; i++) {
                int tileIndex = 4 + i;
                tile[tileIndex] = new Tile();
                Rectangle r = pokeCenterTiles[i];
                tile[tileIndex].image = buildingSheet.getSubimage(r.x, r.y, r.width, r.height);
                tile[tileIndex].collision = true; // Buildings have collision
            }

            // LOAD POKEMART TILES (INDEX 20-35)
            for (int i = 0; i < pokeMartTiles.length; i++) {
                int tileIndex = 20 + i;
                tile[tileIndex] = new Tile();
                Rectangle r = pokeMartTiles[i];
                tile[tileIndex].image = buildingSheet.getSubimage(r.x, r.y, r.width, r.height);
                tile[tileIndex].collision = true; // Buildings have collision
            }

            // TILES WITH COLLISION
            tile[3].collision = true;

            // TILES WITH WILD ENCOUNTERS
            tile[2].hasWildEncounter = true; // TALL GRASS

            // BUILDING TILES 
            for (int i = 4; i <= 35; i++) {
                tile[i].collision = true;
            }

            tile[18].isPokeCenterDoor = true;

            tile[33].isMartDoor = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =====================================
    // MAP LOADER
    // =====================================
    public void loadMap(String filePath) {

        try {
            InputStream is = getClass().getResourceAsStream(filePath); // LOADS A MAP.TXT FILE
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // =====================================
            // OPTIMIZED RENDERING
            // =====================================
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                    && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                    && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
