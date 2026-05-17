package com.game.entity;

import java.awt.image.*;

public class Entity {
    
    public int worldX, worldY;
    public int speed;
    
    public BufferedImage upN, up1, up2, downN, down1, down2, leftN, left1, left2, rightN, right1, right2;
    public String direction;
    
    public int spriteCounter = 0;
    public int spriteNum = 0;
}
