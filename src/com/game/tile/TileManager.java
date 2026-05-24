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

    //=====================================
    // TILE SPRITE
    //=====================================
    private void getTileImage() {

        try {
            BufferedImage tileSheet1 = ImageIO.read(getClass().getResourceAsStream("/res/image/Tileset.png"));
            BufferedImage tileSheet2 = ImageIO.read(getClass().getResourceAsStream("/res/image/Tileset2.png")); // (PLACEHOLDER)

            // MAP EACH TILE INDEX TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            Rectangle[] tileSet1 = {
                
                new Rectangle(1, 52, 16, 16), // Tile[0] - GRASS PATH LIGHT GREEN
                new Rectangle(103, 1, 16, 16), // Tile[1] - GRASS PATH VIBRANT GREEN
                new Rectangle(120, 1, 16, 16), // Tile[2] - TALL GRASS
                new Rectangle(290, 1, 16, 16), // Tile[3] - BUSH
            };

            Rectangle[] tileSet2 = {
                
                
            };

            for (int i = 0; i < tileSet1.length; i++) {
                tile[i] = new Tile();
                Rectangle r = tileSet1[i];
                tile[i].image = tileSheet1.getSubimage(r.x, r.y, r.width, r.height);
            }

            // CONTINUES THE INDEX COUNTING
            int startIndex = tileSet1.length;
            for (int i = 0; i < tileSet2.length; i++) {
                int tileIndex = startIndex + i;
                tile[tileIndex] = new Tile();
                Rectangle r = tileSet2[i];
                tile[tileIndex].image = tileSheet2.getSubimage(r.x, r.y, r.width, r.height);
            }
            
            // TILES WITH COLLISION
            tile[3].collision = true;
            
            // TILES WITH WILD ENCOUNTERS
            tile[2].hasWildEncounter = true; // TALL GRASS
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //=====================================
    // MAP LOADER
    //=====================================
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

            //=====================================
            // OPTIMIZED RENDERING
            //=====================================
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
