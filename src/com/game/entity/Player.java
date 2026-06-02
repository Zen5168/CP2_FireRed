package com.game.entity;

import com.game.main.GamePanel;
import com.game.logic.*;
import com.game.pokemons.Pokemon;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    private BufferedImage[][] walkingSprites; // [DIRECTION][FRAME]

    private int lastTileX = -1;
    private int lastTileY = -1;

    // GRID-BASED MOVEMENT VARIABLES
    private boolean isMoving = false;
    private int pixelCounter = 0;
    private String movementDirection = "";
    private int targetX = 0;
    private int targetY = 0;
    private boolean justTurned = false; // PREVENT IMMEDIATE WALKING AFTER TURNING
    private long turnTime = 0; // TIME WHEN PLAYER TURNED
    private static final long TURN_DELAY_MS = 100; // DELAY BEFORE WALKING AFTER TURNING (0.1 SECONDS)

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
    // GRID-BASED MOVEMENT 
    //=====================================
    public void update() {

        // DETERMINE MOVEMENT SPEED
        int moveSpeed = keyH.ctrlPressed ? 3 : 2; // RUN OR WALK

        // CHECK FOR BUILDING ENTRY/EXIT WITH U or J KEYS
        if (keyH.hasKeyPress()) {
            String key = keyH.getNextKeyPress();
            System.out.println(">>> KEY PRESSED: " + key + " <<<");
            // U = A BUTTON (CONFIRM/ACTION), J = X BUTTON (ALTERNATE ACTION)
            if (key.equals("U") || key.equals("J")) {
                System.out.println(">>> ACTION KEY DETECTED! <<<");
                int playerTileX = worldX / gp.tileSize;
                int playerTileY = worldY / gp.tileSize;
                System.out.println("Player world position: (" + worldX + ", " + worldY + ")");
                System.out.println("Player tile position: (" + playerTileX + ", " + playerTileY + ")");

                if (gp.buildingManager.isInBuilding()) {
                    System.out.println("Checking exit...");
                    gp.buildingManager.checkBuildingExit(playerTileX, playerTileY);
                } else {
                    System.out.println("Checking entry...");
                    gp.buildingManager.checkBuildingEntry(playerTileX, playerTileY);
                }
            }
        }

        // IF CURRENTLY MOVING, CONTINUE THE MOVEMENT ANIMATION
        if (isMoving) {
            pixelCounter += moveSpeed;

            // MOVE TOWARDS TARGET
            switch (movementDirection) {
                case "up":
                    worldY -= moveSpeed;
                    break;
                case "down":
                    worldY += moveSpeed;
                    break;
                case "left":
                    worldX -= moveSpeed;
                    break;
                case "right":
                    worldX += moveSpeed;
                    break;
            }

            // Animate sprite
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 0) ? 2 : 0; // LEFT FOOT OR RIGHT FOOT
                spriteCounter = 0;
            }

            // CHECK IF REACHED TARGET TILE
            if (pixelCounter >= gp.tileSize) {
                // SNAP TO EXACT TILE POSITION
                worldX = targetX;
                worldY = targetY;
                isMoving = false;
                pixelCounter = 0;
                spriteNum = 1; // STANDING SPRITE

                // CHECK FOR WILD ENCOUNTERS (ONLY IF NOT IN A BUILDING)
                if (!gp.buildingManager.isInBuilding()) {
                    checkWildEncounter();
                }
            }
        } // IF NOT MOVING, CHECK FOR NEW INPUT
        else {
            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
                // DETERMINE DESIRED DIRECTION
                String desiredDirection = "";
                if (keyH.upPressed) {
                    desiredDirection = "up";
                } else if (keyH.downPressed) {
                    desiredDirection = "down";
                } else if (keyH.leftPressed) {
                    desiredDirection = "left";
                } else if (keyH.rightPressed) {
                    desiredDirection = "right";
                }

                // IF PLAYER IS FACING A DIFFERENT DIRECTION, JUST TURN (DON'T MOVE)
                if (!direction.equals(desiredDirection)) {
                    direction = desiredDirection;
                    spriteNum = 1; // STANDING SPRITE (FACING NEW DIRECTION)
                    justTurned = true; // SET FLAG TO PREVENT IMMEDIATE MOVEMENT
                    turnTime = System.currentTimeMillis(); // RECORD TURN TIME
                    return; // DON'T MOVE, JUST TURN
                }

                // IF JUST TURNED, CHECK IF ENOUGH TIME HAS PASSED
                if (justTurned) {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - turnTime;

                    // IF NOT ENOUGH TIME HAS PASSED, DON'T WALK YET
                    if (elapsedTime < TURN_DELAY_MS) {
                        return; // WAIT FOR DELAY TO PASS
                    }

                    // ENOUGH TIME HAS PASSED, ALLOW WALKING
                    justTurned = false;
                }

                // IF ALREADY FACING THE DESIRED DIRECTION, PROCEED WITH MOVEMENT
                movementDirection = desiredDirection;

                // CALCULATE TARGET TILE
                targetX = worldX;
                targetY = worldY;

                switch (movementDirection) {
                    case "up":
                        targetY -= gp.tileSize;
                        break;
                    case "down":
                        targetY += gp.tileSize;
                        break;
                    case "left":
                        targetX -= gp.tileSize;
                        break;
                    case "right":
                        targetX += gp.tileSize;
                        break;
                }

                // CHECK COLLISION AT TARGET TILE
                collisionOn = false;

                // TEMPORARILY SET POSITION TO TARGET FOR COLLISION CHECK
                int oldX = worldX;
                int oldY = worldY;
                worldX = targetX;
                worldY = targetY;

                gp.cChecker.checkTile(this);

                // RESTORE POSITION
                worldX = oldX;
                worldY = oldY;

                // IF NO COLLISION, START MOVING
                if (!collisionOn) {
                    isMoving = true;
                    pixelCounter = 0;
                    spriteNum = 0; // START WALKING ANIMATION
                }
            } else {
                // NO KEYS PRESSED - RESET THE TURN FLAG
                justTurned = false;
                spriteNum = 1; // STANDING SPRITE
            }
        }
    }

    //=====================================
    // WILD ENCOUNTER CHECK
    //=====================================
    private void checkWildEncounter() {
        // GET CURRENT TILE POSITION
        int currentTileX = worldX / gp.tileSize;
        int currentTileY = worldY / gp.tileSize;

        // CHECK IF PLAYER MOVED TO A NEW TILE
        if (currentTileX != lastTileX || currentTileY != lastTileY) {
            lastTileX = currentTileX;
            lastTileY = currentTileY;

            // GET THE TILE THE PLAYER IS STANDING ON
            int tileNum = gp.tileM.mapTileNum[currentTileX][currentTileY];

            // CHECK IF IT'S A GRASS TILE
            if (gp.tileM.tile[tileNum].hasWildEncounter) {
                if (gp.encounterManager.checkForEncounter()) {
                    triggerWildBattle();
                }
            }
        }
    }

    //=====================================
    // TRIGGER WILD BATTLE
    //=====================================
    private void triggerWildBattle() {
        // CHECK IF PLAYER HAS ANY CONSCIOUS POKEMON
        boolean hasConsciousPokemon = false;
        for (Pokemon p : gp.playerTrainer.getParty()) {
            if (!p.isFainted()) {
                hasConsciousPokemon = true;
                break;
            }
        }

        // PREVENT BATTLE IF ALL POKEMON ARE FAINTED
        if (!hasConsciousPokemon) {
            System.out.println("All your Pokemon have fainted! Cannot enter battle.");
            return;
        }

        Pokemon wildPokemon = gp.encounterManager.generateWildPokemon();

        System.out.println("\n========================================");
        System.out.println("   A wild " + wildPokemon.getName() + " appeared!");
        System.out.println("========================================");

        // SWITCH TO BATTLE STATE
        gp.currentWildPokemon = wildPokemon;
        gp.gameState = GameState.BATTLE;
        gp.battleScreen.startTransition();

        // GET FIRST CONSCIOUS POKEMON
        Pokemon playerPokemon = null;
        for (Pokemon p : gp.playerTrainer.getParty()) {
            if (!p.isFainted()) {
                playerPokemon = p;
                break;
            }
        }

        // INITIALIZE BATTLE UI
        gp.battleUI.initBattle(gp.playerTrainer, playerPokemon, wildPokemon, false);

        // RESET HP ANIMATION FOR NEW BATTLE
        gp.battleScreen.resetHPAnimation(playerPokemon, wildPokemon);
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
