package com.game.world;

import com.game.main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class InteriorManager {

    private GamePanel gp;
    private BufferedImage interiorSheet;
    private HashMap<String, InteriorRoom> interiors;

    //=============================
    // CURRENT INTERIOR STATE
    //=============================
    private InteriorRoom currentInterior = null;
    private boolean isInInterior = false;

    public InteriorManager(GamePanel gp) {
        this.gp = gp;
        this.interiors = new HashMap<>();
        loadInteriorSprites();
        setupInteriors();
    }

    //=============================
    // EXTRACT SPRITE
    //=============================
    private void loadInteriorSprites() {
        try {
            interiorSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/buildings_interior.png"));
        } catch (IllegalArgumentException e) {
            System.err.println("========================================");
            System.err.println("ERROR: File not found in resources!");
            System.err.println("  File: /res/image/buildings_interior.png");
            System.err.println("  This means the file is not in the compiled JAR/build");
            System.err.println("  Solution: Clean and rebuild the project in NetBeans");
            System.err.println("========================================");
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("ERROR loading interior sprites!");
            System.err.println("  File: /res/image/buildings_interior.png");
            System.err.println("  Error: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
        }
    }

    //=============================
    // INTERIOR SETUP
    //=============================
    private void setupInteriors() {

        // POKEMON CENTER INTERIORT
        InteriorRoom pokeCenterInterior = new InteriorRoom(
                "POKECENTER",
                656, 281, // SPRITE X, SPRITE Y (POSITION IN SPRITE SHEET)
                225, 143, // SPRITE WIDTH, SPRITE HEIGHT (ACTUAL SPRITE SIZE)
                14, 8, // ROOM WIDTH TILES, ROOM HEIGHT TILES (14 COLUMNS X 8 ROWS)
                new Point[]{ // MULTIPLE EXIT DOOR TILES
                    new Point(6, 7),
                    new Point(7, 7)
                },
                new Point(6, 7) // PLAYER SPAWN TILE (SPAWN AT X=6, Y=7)
        );

        //=============================
        // EXTRACT THE SPRITE 
        //=============================
        if (interiorSheet != null) {
            try {
                pokeCenterInterior.setRoomSprite(interiorSheet.getSubimage(
                        pokeCenterInterior.getSpriteX(),
                        pokeCenterInterior.getSpriteY(),
                        pokeCenterInterior.getSpriteWidth(),
                        pokeCenterInterior.getSpriteHeight()
                )
                );

            } catch (Exception e) {
                System.err.println("  ✗ ERROR loading Pokemon Center sprite: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("  ✗ Cannot load sprite: interiorSheet is NULL");
        }

        //=============================
        // LOAD COLLISION MAP 
        //=============================
        loadCollisionMap(pokeCenterInterior, "/res/maps/pokecenter_collision.txt");
        interiors.put("POKECENTER", pokeCenterInterior);

        // POKEMART INTERIOR
        InteriorRoom pokeMartInterior = new InteriorRoom(
                "POKEMART",
                463, 281, // SPRITE X, SPRITE Y 
                176, 128, // SPRITE WIDTH, SPRITE HEIGHT
                11, 8, // ROOM WIDTH TILES, ROOMHEIGHT TILES
                new Point[]{ // MULTIPLE EXIT DOOR TILES
                    new Point(4, 7),
                    new Point(3, 7)
                },
                new Point(3, 7) // PLAYER SPAWN TILE
        );

        if (interiorSheet != null) {
            try {

                pokeMartInterior.setRoomSprite(
                        interiorSheet.getSubimage(
                                pokeMartInterior.getSpriteX(),
                                pokeMartInterior.getSpriteY(),
                                pokeMartInterior.getSpriteWidth(),
                                pokeMartInterior.getSpriteHeight()
                        )
                );
            } catch (Exception e) {
                System.err.println("ERROR loading PokeMart sprite: " + e.getMessage());
                e.printStackTrace();
            }
        }

        //=============================
        // LOAD COLLISION MAP
        //=============================
        loadCollisionMap(pokeMartInterior, "/res/maps/pokemart_collision.txt");
        interiors.put("POKEMART", pokeMartInterior);
    }

    //=============================
    // LOAD COLLISION MAP TEXT FILE
    //=============================
    private void loadCollisionMap(InteriorRoom interior, String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);

            if (is == null) {
                System.err.println("ERROR: Could not find collision map file: " + filePath);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int[][] collisionMap = new int[interior.getRoomHeightTiles()][interior.getRoomWidthTiles()];

            for (int row = 0; row < interior.getRoomHeightTiles(); row++) {
                String line = br.readLine();
                if (line != null) {
                    String[] numbers = line.trim().split("\\s+");

                    for (int col = 0; col < interior.getRoomWidthTiles() && col < numbers.length; col++) {
                        collisionMap[row][col] = Integer.parseInt(numbers[col]);
                    }
                } else {
                    System.err.println("WARNING: Line " + row + " is null!");
                }
            }

            interior.setCollisionMap(collisionMap);

            br.close();
        } catch (Exception e) {
            System.err.println("ERROR loading collision map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //=============================
    // ENTER AN INTERIOR ROOM
    //=============================
    public void enterInterior(String interiorType) {
        InteriorRoom interior = interiors.get(interiorType);
        if (interior != null) {
            currentInterior = interior;
            isInInterior = true;

            // MOVE PLAYER TO SPAWN POSITION
            Point spawn = interior.getPlayerSpawnTile();
            gp.player.worldX = spawn.x * gp.tileSize;
            gp.player.worldY = spawn.y * gp.tileSize;

            if (interior.getRoomSprite() == null) {
                System.err.println("WARNING: Interior sprite is NULL! Check sprite sheet coordinates.");
            }
        } else {
            System.err.println("Interior not found: " + interiorType);
        }
    }

    //=============================
    // EXIT THE CURRENT INTERIOR
    //=============================
    public void exitInterior() {
        if (isInInterior) {
            currentInterior = null;
            isInInterior = false;
        }
    }

    //=======================================
    // CHECK IF PLAYER IS AT THE EXIT DOOR 
    //=======================================
    public boolean checkExit(int playerTileX, int playerTileY) {
        if (isInInterior && currentInterior != null) {
            boolean atExitTile = currentInterior.isPlayerAtExit(playerTileX, playerTileY);
            boolean facingDown = gp.player.direction.equals("down");

            return atExitTile && facingDown;
        }
        return false;
    }

    //=============================
    // DRAW THE  INTERIOR ROOM
    //=============================
    public void draw(Graphics2D g2) {
        if (isInInterior && currentInterior != null) {
            // RENDER THE ROOM AT TILE-BASED SIZE (14 COLUMNS X 8 ROWS)
            int roomPixelWidth = currentInterior.getRoomWidthTiles() * gp.tileSize;
            int roomPixelHeight = currentInterior.getRoomHeightTiles() * gp.tileSize;

            // POSITION THE ROOM SO IT'S CENTERED RELATIVE TO THE PLAYER'S VIEW
            int worldX = 0; // ROOM STARTS AT WORLD ORIGIN
            int worldY = 0;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // DRAW THE COMPLETE INTERIOR ROOM SCALED TO TILE GRID
            if (currentInterior.getRoomSprite() != null) {
                g2.drawImage(
                        currentInterior.getRoomSprite(),
                        screenX,
                        screenY,
                        roomPixelWidth, // SCALED TO 14 TILES WIDE (14 * 48 = 672 PIXELS)
                        roomPixelHeight, // SCALED TO 8 TILES HIGH (8 * 48 = 384 PIXELS)
                        null
                );
            } else {
                // FALLBACK: DRAW A COLORED RECTANGLE IF SPRITE IS NULL
                System.err.println("WARNING: Room sprite is NULL! Drawing fallback rectangle.");
                g2.setColor(new Color(100, 100, 150)); // BLUE-GRAY COLOR
                g2.fillRect(screenX, screenY, roomPixelWidth, roomPixelHeight);

                // DRAW A BORDER
                g2.setColor(Color.WHITE);
                g2.drawRect(screenX, screenY, roomPixelWidth, roomPixelHeight);

                // DRAW TEXT
                g2.setColor(Color.WHITE);
                g2.drawString("Interior Sprite Missing", screenX + 50, screenY + 50);
                g2.drawString("Check sprite coordinates", screenX + 50, screenY + 70);
                g2.drawString("Size: " + roomPixelWidth + "x" + roomPixelHeight, screenX + 50, screenY + 90);
            }
        }
    }
    
    // CHECK FOR COLLISIONS WITH INTERIOR WALLS USING TILE-BASED COLLISION MAP 
    // RETURNS TRUE IF THE PLAYER WOULD COLLIDE WITH A SOLID TILE
    public boolean checkInteriorCollision(Rectangle playerBounds, int playerWorldX, int playerWorldY) {
        if (!isInInterior || currentInterior == null) {
            return false;
        }

        // CHECK IF COLLISION MAP IS LOADED
        if (currentInterior.getCollisionMap() == null) {
            System.err.println("WARNING: Collision map is NULL for " + currentInterior.getName());
            return false; // NO COLLISION IF MAP NOT LOADED
        }

        // CALCULATE THE PLAYER'S COLLISION BOX EDGES AT THE FUTURE POSITION
        int entityLeftWorldX = playerWorldX + playerBounds.x;
        int entityRightWorldX = playerWorldX + playerBounds.x + playerBounds.width - 1;
        int entityTopWorldY = playerWorldY + playerBounds.y;
        int entityBottomWorldY = playerWorldY + playerBounds.y + playerBounds.height - 1;

        // CONVERT TO TILE COORDINATES USING MATH.FLOORDIV FOR PROPER NEGATIVE HANDLING
        int entityLeftCol = Math.floorDiv(entityLeftWorldX, gp.tileSize);
        int entityRightCol = Math.floorDiv(entityRightWorldX, gp.tileSize);
        int entityTopRow = Math.floorDiv(entityTopWorldY, gp.tileSize);
        int entityBottomRow = Math.floorDiv(entityBottomWorldY, gp.tileSize);

        // CHECK ALL FOUR CORNERS OF THE PLAYER'S COLLISION BOX
        boolean topLeftCollision = currentInterior.hasTileCollision(entityLeftCol, entityTopRow);
        boolean topRightCollision = currentInterior.hasTileCollision(entityRightCol, entityTopRow);
        boolean bottomLeftCollision = currentInterior.hasTileCollision(entityLeftCol, entityBottomRow);
        boolean bottomRightCollision = currentInterior.hasTileCollision(entityRightCol, entityBottomRow);

        boolean hasCollision = topLeftCollision || topRightCollision || bottomLeftCollision || bottomRightCollision;

        // RETURN TRUE IF ANY CORNER COLLIDES
        return hasCollision;
    }

    //=============================
    // GETTERS
    //=============================
    public boolean isInInterior() {
        return isInInterior;
    }

    public InteriorRoom getCurrentInterior() {
        return currentInterior;
    }

    public String getCurrentInteriorType() {
        return currentInterior != null ? currentInterior.getName() : null;
    }
}
