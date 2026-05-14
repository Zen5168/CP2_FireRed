package com.game.entity;

import com.game.gui.*;
import com.game.logic.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    private BufferedImage[][] walkingSprites; // [DIRECTION][FRAME]

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        try {
            BufferedImage playerSheet = ImageIO.read(getClass().getResourceAsStream("/image/FMC_sprite_sheet.png"));
            int bgRGB = playerSheet.getRGB(0, 0);

            walkingSprites = new BufferedImage[4][3];

            // 12 FRAME MAPPINGS: [DIRECTION][FRAME] -> {NEUTRAL, LEFT FOOT, RIGHT FOOT}
            Rectangle[][] map = {
                // DOWN
                {new Rectangle(1, 1, 19, 19), new Rectangle(22, 1, 19, 19), new Rectangle(43, 1, 19, 19)},
                // UP
                {new Rectangle(1, 22, 19, 19), new Rectangle(22, 22, 19, 19), new Rectangle(43, 22, 19, 19)},
                // LEFT
                {new Rectangle(1, 43, 19, 19), new Rectangle(22, 43, 19, 19), new Rectangle(43, 43, 19, 19)},
                // RIGHT
                {new Rectangle(1, 64, 19, 19), new Rectangle(22, 64, 19, 19), new Rectangle(43, 64, 19, 19)}
            };

            for (int dir = 0; dir < 4; dir++) {
                for (int frame = 0; frame < 3; frame++) {
                    Rectangle r = map[dir][frame];

                    walkingSprites[dir][frame] = playerSheet.getSubimage(r.x, r.y, r.width, r.height);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=====================================
    // WALKING ANIMATION
    //=====================================
    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
                y -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                y += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                x -= speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                x += speed;
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 0) ? 2 : 0; // LEFT FOOT OR RIGHT FOOT
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // STANDING OR NETURAL
        }
    }

    //=====================================
    // IMAGE OUTPUT
    //=====================================    
    public void draw(Graphics2D g2) {
        int dirIndex = switch (direction) {
            case "down" ->
                0;
            case "up" ->
                1;
            case "left" ->
                2;
            case "right" ->
                3;
            default ->
                0;
        };

        BufferedImage image = walkingSprites[dirIndex][spriteNum];
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
