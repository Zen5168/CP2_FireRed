package com.game.main;

import com.game.entity.*;
import com.game.world.Building;
import java.awt.Rectangle;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {

        this.gp = gp;
    }

    //=====================================
    // CHECK TILE COLLISION
    //=====================================    
    public void checkTile(Entity entity) {

        // If inside a building, use interior collision detection
        if (gp.buildingManager.isInBuilding()) {
            checkInteriorCollision(entity);
            return;
        }

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }

        // CHECK BUILDING COLLISION (ONLY IN OVERWORLD, NOT INSIDE BUILDINGS)
        if (!gp.buildingManager.isInBuilding()) {
            checkBuildingCollision(entity);
        }
    }

    //=====================================
    // CHECK INTERIOR COLLISION
    //=====================================
    private void checkInteriorCollision(Entity entity) {
        // CALCULATE ENTITY'S NEXT POSITION BASED ON DIRECTION
        int nextWorldX = entity.worldX;
        int nextWorldY = entity.worldY;

        switch (entity.direction) {
            case "up":
                nextWorldY -= entity.speed;
                break;
            case "down":
                nextWorldY += entity.speed;
                break;
            case "left":
                nextWorldX -= entity.speed;
                break;
            case "right":
                nextWorldX += entity.speed;
                break;
        }

        // CHECK IF THE NEXT POSITION WOULD COLLIDE WITH INTERIOR WALLS
        if (gp.buildingManager.checkInteriorCollision(entity.solidArea, nextWorldX, nextWorldY)) {
            entity.collisionOn = true;
        }
    }

    //=====================================
    // CHECK BUILDING COLLISION
    //=====================================
    public void checkBuildingCollision(Entity entity) {
        for (Building building : gp.buildingManager.getBuildings()) {
            // Calculate entity's next position based on direction
            int nextWorldX = entity.worldX;
            int nextWorldY = entity.worldY;

            switch (entity.direction) {
                case "up":
                    nextWorldY -= entity.speed;
                    break;
                case "down":
                    nextWorldY += entity.speed;
                    break;
                case "left":
                    nextWorldX -= entity.speed;
                    break;
                case "right":
                    nextWorldX += entity.speed;
                    break;
            }

            // CREATE RECTANGLE FOR ENTITY
            Rectangle entityRect = new Rectangle(
                    nextWorldX + entity.solidArea.x,
                    nextWorldY + entity.solidArea.y,
                    entity.solidArea.width,
                    entity.solidArea.height
            );

            // BUILDING COLLISION (ENTIRE BUILDING INCLUDING DOOR)
            int buildingWorldX = building.getWorldX();
            int buildingWorldY = building.getWorldY();
            int buildingWidth = building.getWidth() * gp.tileSize;
            int buildingHeight = building.getHeight() * gp.tileSize;

            Rectangle buildingRect = new Rectangle(
                    buildingWorldX,
                    buildingWorldY,
                    buildingWidth,
                    buildingHeight
            );

            // CHECK IF ENTITY COLLIDES WITH BUILDING
            if (entityRect.intersects(buildingRect)) {
                entity.collisionOn = true;
                break;
            }
        }
    }
}
