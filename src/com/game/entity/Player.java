package com.game.entity;

import com.game.main.GamePanel;
import com.game.logic.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    private BufferedImage[][] walkingSprites; // [DIRECTION][FRAME]

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        // CAMERA FOLLOWS THE PLAYER
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2); // MAKES THE PLAYER ALWAYS AT THE CENTER OF THE SCREEN
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2); // MAKES THE PLAYER ALWAYS AT THE CENTER OF THE SCREEN

        solidArea = new Rectangle(10, 26, 22, 16); // HITBOX

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }

    public void getPlayerImage() {

        try {
            BufferedImage playerSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/FMC_sprite_sheet.png"));
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

        if (keyH.ctrlPressed) {
            speed = 4; // RUN SPEED
        } else {
            speed = 2; // WALK SPEED
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false) {

                switch (direction) {

                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 0) ? 2 : 0; // LEFT FOOT OR RIGHT FOOT
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // STANDING OR NEUTRAL
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
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
