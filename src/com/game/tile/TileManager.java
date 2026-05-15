package com.game.tile;

import com.game.gui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum [][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap("/res/maps/map01.txt");
    }

    //=====================================
    // TILE SPRITE
    //=====================================
    private void getTileImage() {

        try {
            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/Tileset.png"));

            // MAP EACH TILE INDEX TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            Rectangle[] map = {
                    new Rectangle(1,  52,  16, 16),   // com.game.tile[0] - GRASS PATH LIGHT GREEN
                    new Rectangle(103,  1,  16, 16),   // com.game.tile[1] - GRASS PATH VIBRANT GREEN
                    new Rectangle(120,  1,  16, 16),   // com.game.tile[2] - TALL GRASS
            };

            for (int i = 0; i < map.length; i++) {
                tile[i] = new Tile();
                Rectangle r = map[i];
                tile[i].image = tileSheet.getSubimage(r.x, r.y, r.width, r.height);
            }

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

            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                String line = br.readLine();

                while (col < gp.maxScreenCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxScreenCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image,x, y, gp.tileSize, gp.tileSize,null);
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}