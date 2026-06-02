package com.game.world;

import com.game.main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class BuildingManager {

    private GamePanel gp;
    private ArrayList<Building> buildings;
    private BufferedImage buildingSheet;

    // BUILDING STATE
    private boolean isInBuilding = false;
    private String currentBuildingType = null; // "POKECENTER" OR "POKEMART"
    private Point previousPlayerPosition = null;

    // INTERIOR MANAGER FOR ROOM-BASED INTERIORS
    private InteriorManager interiorManager;

    // TRANSITION MANAGER FOR FADE EFFECTS
    private TransitionManager transitionManager;

    public BuildingManager(GamePanel gp) {
        this.gp = gp;
        this.buildings = new ArrayList<>();
        this.interiorManager = new InteriorManager(gp);
        this.transitionManager = new TransitionManager();
        loadBuildingSprites();
        setupBuildings();
    }

    private void loadBuildingSprites() {
        try {
            buildingSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/buildings_sprites.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupBuildings() {
        // POKEMON CENTER AT WORLD TILE POSITION (20, 15)
        // SPRITE LOCATION: X=30, Y=768, SIZE=64X64 (4X4 TILES AT 16X16 EACH)
        BufferedImage pokeCenterSprite = buildingSheet.getSubimage(30, 768, 64, 64);
        Building pokeCenter = new Building(
                "Pokemon Center",
                20 * gp.tileSize, // WORLD X
                15 * gp.tileSize, // WORLD Y
                4, // WIDTH IN TILES
                4, // HEIGHT IN TILES
                pokeCenterSprite
        );
        buildings.add(pokeCenter);

        // POKEMART AT WORLD TILE POSITION (26, 15)
        // SPRITE LOCATION: X=31, Y=880, SIZE=64X64
        BufferedImage pokeMartSprite = buildingSheet.getSubimage(31, 880, 64, 64);
        Building pokeMart = new Building(
                "PokeMart",
                26 * gp.tileSize, // WORLD X
                15 * gp.tileSize, // WORLD Y
                4, // WIDTH IN TILES
                4, // HEIGHT IN TILES
                pokeMartSprite
        );
        buildings.add(pokeMart);
    }

    //=============================
    // DRAW ALL BUILDINGS
    //=============================
    public void draw(Graphics2D g2) {
        for (Building building : buildings) {
            int worldX = building.getWorldX();
            int worldY = building.getWorldY();

            // CALCULATE SCREEN POSITION RELATIVE TO PLAYER
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // ONLY DRAW IF BUILDING IS ON SCREEN (OPTIMIZED RENDERING)
            int buildingPixelWidth = building.getWidth() * gp.tileSize;
            int buildingPixelHeight = building.getHeight() * gp.tileSize;

            if (worldX + buildingPixelWidth > gp.player.worldX - gp.player.screenX
                    && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + buildingPixelHeight > gp.player.worldY - gp.player.screenY
                    && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(building.getSprite(), screenX, screenY,
                        buildingPixelWidth, buildingPixelHeight, null);
            }
        }
    }

    // CHECK IF PLAYER IS TRYING TO ENTER A BUILDING
    public void checkBuildingEntry(int playerTileX, int playerTileY) {
        for (Building building : buildings) {
            int buildingTileX = building.getWorldX() / gp.tileSize;
            int buildingTileY = building.getWorldY() / gp.tileSize;

            // DOOR IS AT COLUMN 1, ROW 3 OF THE 4X4 BUILDING
            int doorTileX = buildingTileX + 1; // COLUMN 1 (LEFT-CENTER DOOR TILE)
            int doorTileY = buildingTileY + 3;  // ROW 3 (BOTTOM ROW)

            // PLAYER CAN ENTER FROM THE DOOR TILE OR ONE TILE BELOW IT
            boolean atDoor = (playerTileX == doorTileX) && (playerTileY == doorTileY);
            boolean belowDoor = (playerTileX == doorTileX) && (playerTileY == doorTileY + 1);
            
            // PLAYER MUST BE FACING UP TO ENTER
            boolean facingUp = gp.player.direction.equals("up");
            
            if ((atDoor || belowDoor) && facingUp) {
                // DETERMINE BUILDING TYPE
                String buildingType = building.getName().contains("Center") ? "POKECENTER" : "POKEMART";
                enterBuilding(buildingType, building);
                return;
            }
        }
    }

    // CHECK IF PLAYER IS TRYING TO EXIT A BUILDING
    public void checkBuildingExit(int playerTileX, int playerTileY) {
        if (isInBuilding && interiorManager.checkExit(playerTileX, playerTileY)) {
            exitBuilding();
        }
    }

    private void enterBuilding(String buildingType, Building building) {
        // START FADE OUT TRANSITION
        transitionManager.startFadeOutIn(() -> {

            // SAVE CURRENT PLAYER POSITION
            previousPlayerPosition = new Point(
                    gp.player.worldX / gp.tileSize,
                    gp.player.worldY / gp.tileSize
            );

            // ENTER THE INTERIOR ROOM
            interiorManager.enterInterior(buildingType);

            // EXECUTE BUILDING-SPECIFIC ACTIONS
            if (buildingType.equals("POKECENTER")) {
                Buildings.healPokemon(gp.playerTrainer);
            } else if (buildingType.equals("POKEMART")) {
                Buildings.openShop();
            }
            
            // UPDATE STATE
            isInBuilding = true;
            currentBuildingType = buildingType;
        });
    }

    private void exitBuilding() {
        if (currentBuildingType != null) {
            // START FADE OUT TRANSITION
            transitionManager.startFadeOutIn(() -> {

                // EXIT THE INTERIOR
                interiorManager.exitInterior();

                // MOVE PLAYER BACK TO WHERE THEY ENTERED
                if (previousPlayerPosition != null) {
                    gp.player.worldX = previousPlayerPosition.x * gp.tileSize;
                    gp.player.worldY = previousPlayerPosition.y * gp.tileSize;
                }

                // UPDATE STATE
                isInBuilding = false;
                currentBuildingType = null;
            });
        }
    }

    // DRAW INTERIOR IF INSIDE A BUILDING
    public void drawInterior(Graphics2D g2) {
        if (isInBuilding) {
            interiorManager.draw(g2);
        }
    }

    // UPDATE TRANSITIONS
    public void update() {
        transitionManager.update();
    }

    // DRAW TRANSITION OVERLAY 
    public void drawTransition(Graphics2D g2) {
        transitionManager.draw(g2, gp.screenWidth, gp.screenHeight);
    }

    // CHECK INTERIOR COLLISION
    public boolean checkInteriorCollision(Rectangle playerBounds, int playerWorldX, int playerWorldY) {
        return interiorManager.checkInteriorCollision(playerBounds, playerWorldX, playerWorldY);
    }

    //=============================
    // GETTERS
    //=============================
    public boolean isInBuilding() {
        return isInBuilding;
    }

    public String getCurrentBuildingType() {
        return currentBuildingType;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public InteriorManager getInteriorManager() {
        return interiorManager;
    }

    public TransitionManager getTransitionManager() {
        return transitionManager;
    }
}
