package com.game.tile;

import com.game.gui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[20];
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
                new Rectangle(1, 52, 16, 16), // com.game.tile[0] - GRASS PATH LIGHT GREEN
                new Rectangle(103, 1, 16, 16), // com.game.tile[1] - GRASS PATH VIBRANT GREEN
                new Rectangle(120, 1, 16, 16), // com.game.tile[2] - TALL GRASS
                new Rectangle(239, 290, 16, 16), // com.game.tile[3] - TREE UPPER LEFT
                new Rectangle(256, 290, 16, 16), // com.game.tile[4] - TREE UPPER RIGHT
                new Rectangle(239, 307, 16, 16), // com.game.tile[5] - TREE MID LEFT
                new Rectangle(256, 307, 16, 16), // com.game.tile[6] - TREE MID RIGHT
                new Rectangle(239, 324, 16, 16), // com.game.tile[7] - TREE LOWER LEFT
                new Rectangle(256, 324, 16, 16), // com.game.tile[8] - TREE LOWER RIGHT
                new Rectangle(273, 290, 16, 16), // com.game.tile[9] - WALL OF TREE UPPER LEFT
                new Rectangle(290, 290, 16, 16), // com.game.tile[10] - WALL OF TREE UPPER RIGHT
                new Rectangle(273, 307, 16, 16), // com.game.tile[11] - WALL OF TREE MID LEFT
                new Rectangle(290, 307, 16, 16), // com.game.tile[12] - WALL OF TREE MID RIGHT
                new Rectangle(273, 324, 16, 16), // com.game.tile[13] - WALL OF TREE LOWER LEFT
                new Rectangle(290, 324, 16, 16), // com.game.tile[14] - WALL OF TREE LOWER RIGHT
                new Rectangle(290, 1, 16, 16), // com.game.tile[15] - BUSH
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
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
