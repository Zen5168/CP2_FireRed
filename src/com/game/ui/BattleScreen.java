package com.game.ui;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import com.game.logic.Type;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BattleScreen {

    private GamePanel gp;
    private BufferedImage[][] pokemonSprites; // [ROW][COL]
    private BufferedImage[] trainerIntroFrames; // TRAINER THROWING POKEBALL ANIMATION FRAMES
    private BufferedImage[] pokeballSprites; // [0=CLOSED, 1=OPENED]
    private BufferedImage battleBackground; // BATTLE BACKGROUND FROM SPRITE SHEET

    private int transitionAlpha = 0;
    private boolean isTransitioning = false;
    private int transitionSpeed = 15;

    // HP BAR ANIMATION
    private double playerDisplayedHP = 0;
    private double enemyDisplayedHP = 0;
    private double hpAnimationSpeed = 2.0; // HP POINTS PER FRAME

    // TRAINER INTRO ANIMATION
    private boolean showTrainerIntro = false;
    private int trainerIntroFrame = 0;
    private int trainerIntroFrameCounter = 0;
    private int trainerIntroFrameDelay = 8; // FRAMES TO WAIT BEFORE CHANGING SPRITE
    private int trainerIntroTotalFrames = 5; // TOTAL FRAMES IN ANIMATION 
    private int trainerSlideOffset = 0; // FOR SLIDE-IN ANIMATION

    // POKEBALL THROW ANIMATION
    private boolean showPokeballThrow = false;
    private double pokeballX = 0;
    private double pokeballY = 0;
    private int pokeballTargetX = 0;
    private int pokeballTargetY = 0;
    private double pokeballVelocityX = 0;
    private double pokeballVelocityY = 0;
    private int pokeballFrame = 0; // 0 = CLOSED, 1 = OPENED
    private int pokeballAnimationStage = 0; // 0 = THROWING, 1 = OPENING, 2 = CATCHING, 3 = COMPLETE
    private int pokeballAnimationCounter = 0;
    private boolean catchSuccessful = false;
    private int wobbleCount = 0;
    private double wobbleAngle = 0;
    private boolean hideEnemyPokemon = false; // HIDE ENEMY WHEN CAUGHT IN POKEBALL

    // ATTACK VISUAL EFFECTS
    private boolean showAttackEffect = false;
    private Type attackEffectType = Type.NONE;
    private int attackEffectX = 0;
    private int attackEffectY = 0;
    private int attackEffectFrame = 0;
    private int attackEffectMaxFrames = 20;
    private boolean attackTargetIsEnemy = true;

    public BattleScreen(GamePanel gp) {
        this.gp = gp;
        loadBattleAssets();
    }

    //=====================================
    // LOAD POKEMON BATTLE SPRITES
    //=====================================
    private void loadBattleAssets() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/PokeSprites.png"));

            // INITIALIZE SPRITE ARRAY (ADJUST SIZE AS NEEDED)
            pokemonSprites = new BufferedImage[30][2]; // [POKEMON_ID][0=FRONT, 1=BACK]

            // MAP EACH POKEMON SPRITE TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            
            // FORMAT: [POKEMON_ID][FRONT_SPRITE, BACK_SPRITE]
            
            Rectangle[][] spriteMap = { // NOTE TO SELF: RE-MAP THESPRITE FOR A CLEANER LOOK
                
                // BULBASAUR [0]
                {new Rectangle(12, 46, 63, 63), new Rectangle(17, 117, 49, 49)}, // DONE
                
                // CHARMANDER [1]
                {new Rectangle(402, 46, 63, 63), new Rectangle(402, 111, 63, 63)}, // DONE
                
                // SQUIRTLE [2]
                {new Rectangle(792, 46, 63, 63), new Rectangle(797, 117, 50, 50)}, // DONE
                
                // PIDGEY [3]
                {new Rectangle(12, 212, 63, 63), new Rectangle(14, 283, 60, 57)}, // DONE
                
                // RATTATA [4]
                {new Rectangle(401, 212, 63, 63), new Rectangle(406, 276, 59, 59)}, // DONE
                
                // CUBONE [5]
                {new Rectangle(1702, 1036, 63, 63), new Rectangle(1702, 1101, 63, 63)}, // DONE
                
                // ELECTABUZZ [6]
                {new Rectangle(531, 1367, 63, 63), new Rectangle(531, 1433, 63, 63)}, // DONE
                
                // FARFETCH'D [7]
                {new Rectangle(922, 871, 63, 63), new Rectangle(922, 936, 63, 63)}, // DONE
                
                // GROWLITHE [8]
                {new Rectangle(1571, 541, 63, 63), new Rectangle(1571, 608, 63, 63)}, // DONE
                
                // JYNX [9]
                {new Rectangle(401, 1367, 63, 63), new Rectangle(402, 1432, 63, 58)}, // DONE
                
                // KANGASKHAN [10]
                {new Rectangle(1181, 1200, 63, 63), new Rectangle(1186, 1272, 55, 62)},//DONE
                
                // MACHOP [11]
                {new Rectangle(662, 706, 63, 63), new Rectangle(662, 773, 63, 58)}, // DONE
                
                // MAGMAR [12]
                {new Rectangle(662, 1367, 63, 63), new Rectangle(662, 1434, 63, 63)}, // DONE
                
                // PSYDUCK [13]
                {new Rectangle(1051, 541, 63, 63), new Rectangle(1051, 609, 63, 63)}, // DONE
                
                // SCYTHER [14]
                {new Rectangle(271, 1367, 63, 63), new Rectangle(272, 1435, 63, 65)}, // DONE
                
                // IVYSAUR [15]
                {new Rectangle(142, 47, 63, 63), new Rectangle(142, 111, 63, 63)}, // Done
                
                // CHARMELEON [16]
                {new Rectangle(532, 47, 63, 63), new Rectangle(532, 114, 63, 63)}, // DONE
                
                // WARTORTLE [17]
                {new Rectangle(922, 47, 63, 63), new Rectangle(922, 111, 63, 63)}, // DONE
                
                // VENUSAUR [18]
                {new Rectangle(272, 47, 63, 63), new Rectangle(272, 111, 63, 63)}, // DONE
                
                // CHARIZARD [19]
               {new Rectangle(661, 47, 63, 63), new Rectangle(661, 114, 64, 66)}, // DONE
                
                // BLASTOISE [20]
                {new Rectangle(1052, 47, 63, 63), new Rectangle(1051, 111, 64, 63)} // DONE

            // ADD MORE POKEMON HERE AS NEEDED
            // {new Rectangle(x, y, width, height), new Rectangle(x, y, width, height)},
            };

            // EXTRACT SPRITES USING THE MAP
            for (int i = 0; i < spriteMap.length; i++) {
                // POKEMON BATTLE SPRITE (FRONT)
                Rectangle frontRect = spriteMap[i][0];
                pokemonSprites[i][0] = spriteSheet.getSubimage(
                        frontRect.x,
                        frontRect.y,
                        frontRect.width,
                        frontRect.height
                );

                // POKEMON BATTLE SPRITE (BACK)
                Rectangle backRect = spriteMap[i][1];
                pokemonSprites[i][1] = spriteSheet.getSubimage(
                        backRect.x,
                        backRect.y,
                        backRect.width,
                        backRect.height
                );
            }

            System.out.println("Battle sprites loaded successfully!");

        } catch (Exception e) {
            System.out.println("Could not load battle sprites: " + e.getMessage());
            e.printStackTrace();
        }

        //=====================================
        // LOAD TRAINER INTRO ANIMATION
        //=====================================
        try {
            BufferedImage trainerSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/FMC_Battle_intro_sprite_sheet.png"));

            // MAPPED EACH FRAME TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            Rectangle[] trainerFrameMap = {
                // FRAME 1
                new Rectangle(0, 0, 38, 51),
                // FRAME 2
                new Rectangle(39, 0, 51, 51),
                // FRAME 3
                new Rectangle(90, 0, 51, 51),
                // FRAME 4
                new Rectangle(144, 0, 51, 51),
                // FRAME 5
                new Rectangle(196, 0, 51, 51),};

            // EXTRACT FRAMES USING THE MAP
            trainerIntroFrames = new BufferedImage[5];
            for (int i = 0; i < trainerFrameMap.length; i++) {
                Rectangle frameRect = trainerFrameMap[i];
                trainerIntroFrames[i] = trainerSheet.getSubimage(
                        frameRect.x,
                        frameRect.y,
                        frameRect.width,
                        frameRect.height
                );
            }

            System.out.println("Trainer intro animation loaded successfully!");

        } catch (Exception e) {
            System.out.println("Could not load trainer intro animation: " + e.getMessage());
            e.printStackTrace();
        }

        //=====================================
        // LOAD POKEBALL SPRITES
        //=====================================
        try {
            BufferedImage pokeballSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/pokeball.png"));

            if (pokeballSheet == null) {
                System.out.println("ERROR: Pokeball sprite sheet is null! File not found or couldn't be read.");
                System.out.println("Creating placeholder pokeball sprites...");
                return;
            }

            System.out.println("Pokeball sheet loaded: " + pokeballSheet.getWidth() + "x" + pokeballSheet.getHeight());

            // POKEBALL SPRITE ARRAY
            pokeballSprites = new BufferedImage[2]; // [0 = CLOSED, 1 = OPENED]

            // ADJUST THESE COORDINATES TO MATCH YOUR SPRITE LAYOUT
            // FORMAT: X, Y, WIDTH, HEIGHT
            Rectangle[] pokeballFrameMap = {
                // CLOSED POKEBALL 
                new Rectangle(33, 1, 14, 14),
                // OPENED POKEBALL  
                new Rectangle(33, 18, 14, 14),};

            // EXTRACT FRAMES USING THE MAP
            boolean allLoaded = true;
            for (int i = 0; i < pokeballFrameMap.length; i++) {
                Rectangle frameRect = pokeballFrameMap[i];

                // CHECK IF COORDINATES ARE WITHIN BOUNDS
                if (frameRect.x + frameRect.width <= pokeballSheet.getWidth()
                        && frameRect.y + frameRect.height <= pokeballSheet.getHeight()) {

                    pokeballSprites[i] = pokeballSheet.getSubimage(
                            frameRect.x,
                            frameRect.y,
                            frameRect.width,
                            frameRect.height
                    );
                    System.out.println("Loaded pokeball sprite " + i + " at (" + frameRect.x + "," + frameRect.y + ") size " + frameRect.width + "x" + frameRect.height + ": SUCCESS");
                } else {
                    System.out.println("ERROR: Pokeball sprite " + i + " coordinates out of bounds!");
                    System.out.println("  Requested: x=" + frameRect.x + " y=" + frameRect.y + " w=" + frameRect.width + " h=" + frameRect.height);
                    System.out.println("  Sheet size: " + pokeballSheet.getWidth() + "x" + pokeballSheet.getHeight());
                    allLoaded = false;
                }
            }

            if (allLoaded) {
                System.out.println(" Pokeball sprites loaded successfully!");
            } else {
                System.out.println("Some pokeball sprites failed to load!");
            }

        } catch (Exception e) {
            System.out.println("Could not load pokeball sprites: " + e.getMessage());
            e.printStackTrace();
        }

        //=============================================
        // LOAD BATTLE BACKGROUND FROM SPRITE SHEET
        //=============================================
        try {
            BufferedImage backgroundSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/battle_backgrounds.png"));

            if (backgroundSheet == null) {
                System.out.println("ERROR: Battle background sprite sheet is null! Using fallback background.");
                battleBackground = null;
                return;
            }

            System.out.println("Battle background sheet loaded: " + backgroundSheet.getWidth() + "x" + backgroundSheet.getHeight());

            // FORMAT: X, Y, WIDTH, HEIGHT
            Rectangle backgroundRect = new Rectangle(249, 7, 240, 111);

            // EXTRACT BACKGROUND SPRITE
            if (backgroundRect.x + backgroundRect.width <= backgroundSheet.getWidth()
                    && backgroundRect.y + backgroundRect.height <= backgroundSheet.getHeight()) {

                battleBackground = backgroundSheet.getSubimage(
                        backgroundRect.x,
                        backgroundRect.y,
                        backgroundRect.width,
                        backgroundRect.height
                );
                System.out.println("Battle background loaded successfully from sprite sheet!");
            } else {
                System.out.println("ERROR: Battle background coordinates out of bounds!");
                System.out.println("Requested: x=" + backgroundRect.x + " y=" + backgroundRect.y
                        + " w=" + backgroundRect.width + " h=" + backgroundRect.height);
                System.out.println("Sheet size: " + backgroundSheet.getWidth() + "x" + backgroundSheet.getHeight());
                battleBackground = null;
            }

        } catch (Exception e) {
            System.out.println("Could not load battle background: " + e.getMessage());
            System.out.println("Using fallback colored background instead.");
            e.printStackTrace();
            battleBackground = null;
        }
    }

    //=====================================
    // START TRANSITION
    //=====================================
    public void startTransition() {
        isTransitioning = true;
        transitionAlpha = 255; // START FULLY BLACK
        showTrainerIntro = true; // START TRAINER INTRO ANIMATION
        trainerIntroFrame = 0;
        trainerIntroFrameCounter = 0;
        trainerSlideOffset = -200; // START OFF-SCREEN FOR SLIDE-IN EFFECT

        // RESET POKEMON VISIBILITY (FIX FOR BUG WHERE POKEMON DON'T APPEAR AFTER CATCH)
        hideEnemyPokemon = false;

        // START POKEBALL THROW ANIMATION SYNCED WITH TRAINER
        showPokeballThrow = true;
        pokeballAnimationStage = -1; // SPECIAL STAGE FOR TRAINER INTRO
        pokeballAnimationCounter = 0;
        pokeballFrame = 0;
        pokeballX = -100; // START OFF-SCREEN
        pokeballY = gp.screenHeight - 200;
    }

    //=====================================
    // UPDATE TRANSITION
    //=====================================
    public void updateTransition() {
        if (isTransitioning) {
            transitionAlpha -= transitionSpeed; // FADE OUT
            if (transitionAlpha <= 0) {
                transitionAlpha = 0;
                isTransitioning = false;
            }
        }

        // UPDATE TRAINER INTRO ANIMATION (USED FOR BOTH BATTLE START AND POKEMON SWITCH)
        if (showTrainerIntro) {
            // SLIDE-IN EFFECT 
            if (trainerSlideOffset < 0) {
                trainerSlideOffset += 15; // SLIDE SPEED
                if (trainerSlideOffset > 0) {
                    trainerSlideOffset = 0;
                }
            }

            trainerIntroFrameCounter++;
            if (trainerIntroFrameCounter >= trainerIntroFrameDelay) {
                trainerIntroFrameCounter = 0;
                trainerIntroFrame++;

                // END ANIMATION AFTER ALL FRAMES PLAYED
                if (trainerIntroFrame >= trainerIntroTotalFrames) {
                    showTrainerIntro = false;
                    trainerIntroFrame = 0;
                    showPokeballThrow = false; // END POKEBALL ANIMATION TOO
                }
            }

            // POKEBALL POSITION SYNCED WITH TRAINER ANIMATION
            if (showPokeballThrow && pokeballAnimationStage == -1) {
                // SYNC POKEBALL WITH TRAINER FRAMES
                if (trainerIntroFrame >= 2 && trainerIntroFrame <= 4) {
                    // CALCULATE POKEBALL POSITION BASED ON TRAINER POSITION
                    int trainerX = -30 + trainerSlideOffset;
                    pokeballX = trainerX + 200 + (trainerIntroFrame - 2) * 80;
                    pokeballY = (gp.screenHeight - 280 - 140) + 100 - (trainerIntroFrame - 2) * 40;

                    // ROTATE POKEBALL
                    if (trainerIntroFrameCounter % 2 == 0) {
                        pokeballFrame = (pokeballFrame == 0) ? 1 : 0;
                    }
                } else if (trainerIntroFrame < 2) {
                    // HIDE POKEBALL BEFORE THROW
                    pokeballX = -100;
                } else {
                    // POKEBALL CONTINUES OFF-SCREEN AFTER THROW
                    pokeballX += 20;
                    pokeballY -= 5;
                }
            }
        }

        // UPDATE POKEBALL THROW ANIMATION (FOR CATCHING)
        if (showPokeballThrow && pokeballAnimationStage >= 0) {
            updatePokeballAnimation();
        }
        
        // UPDATE ATTACK EFFECT ANIMATION
        if (showAttackEffect) {
            attackEffectFrame++;
            if (attackEffectFrame >= attackEffectMaxFrames) {
                showAttackEffect = false;
                attackEffectFrame = 0;
            }
        }
    }

    //=====================================
    // START POKEBALL THROW ANIMATION
    //=====================================
    public void startPokeballThrow(int targetX, int targetY, boolean willCatchSucceed) {
        System.out.println("Starting pokeball throw animation! Target: " + targetX + ", " + targetY + " Success: " + willCatchSucceed);
        showPokeballThrow = true;
        catchSuccessful = willCatchSucceed;
        hideEnemyPokemon = false;

        // START POSITION (BOTTOM LEFT, PLAYER SIDE)
        pokeballX = 100;
        pokeballY = gp.screenHeight - 200;

        // TARGET POSITION (ENEMY POKEMON)
        pokeballTargetX = targetX;
        pokeballTargetY = targetY;

        // CALCULATE VELOCITY FOR ARC TRAJECTORY
        int dx = pokeballTargetX - (int) pokeballX;
        int dy = pokeballTargetY - (int) pokeballY;
        pokeballVelocityX = dx / 30.0; // REACH TARGET IN 30 FRAMES
        pokeballVelocityY = dy / 30.0 - 5; // ADD UPWARD ARC

        pokeballFrame = 0; // START WITH CLOSED POKEBALL
        pokeballAnimationStage = 0; // THROWING STAGE
        pokeballAnimationCounter = 0;
        wobbleCount = 0;
        wobbleAngle = 0;

        System.out.println("Pokeball animation initialized. Start pos: " + pokeballX + ", " + pokeballY);
    }

    //=====================================
    // START POKEMON SWITCH ANIMATION
    //=====================================
    public void startPokemonSwitchAnimation(int targetX, int targetY) {
        System.out.println("Starting pokemon switch animation! Target: " + targetX + ", " + targetY);

        // USE TRAINER INTRO ANIMATION
        showTrainerIntro = true;
        trainerIntroFrame = 0;
        trainerIntroFrameCounter = 0;
        trainerSlideOffset = -200; // START OFF-SCREEN FOR SLIDE-IN EFFECT

        // START POKEBALL THROW ANIMATION SYNCED WITH TRAINER
        showPokeballThrow = true;
        catchSuccessful = false; // NOT A CATCH, JUST A SWITCH
        hideEnemyPokemon = false; // DON'T HIDE ENEMY POKEMON

        // USE SPECIAL STAGE FOR TRAINER INTRO ANIMATION
        pokeballAnimationStage = -1; // SPECIAL STAGE FOR TRAINER INTRO
        pokeballAnimationCounter = 0;
        pokeballFrame = 0;
        pokeballX = -100; // START OFF-SCREEN
        pokeballY = gp.screenHeight - 200;

        System.out.println("Pokemon switch animation initialized with trainer intro style");
    }

    //=====================================
    // UPDATE POKEBALL ANIMATION
    //=====================================
    private void updatePokeballAnimation() {
        pokeballAnimationCounter++;

        switch (pokeballAnimationStage) {
            case 0: // THROWING STAGE
                // UPDATE POSITION WITH ARC TRAJECTORY
                pokeballX += pokeballVelocityX;
                pokeballY += pokeballVelocityY;
                pokeballVelocityY += 0.3; // GRAVITY

                // ROTATE POKEBALL (VISUAL EFFECT)
                if (pokeballAnimationCounter % 3 == 0) {
                    pokeballFrame = (pokeballFrame == 0) ? 1 : 0;
                }

                // CHECK IF REACHED TARGET
                if (pokeballY >= pokeballTargetY) {
                    pokeballY = pokeballTargetY;
                    pokeballX = pokeballTargetX;
                    pokeballAnimationStage = 1; // MOVE TO OPENING STAGE
                    pokeballAnimationCounter = 0;
                    pokeballFrame = 1; // OPEN POKEBALL
                }
                break;

            case 1: // OPENING STAGE (POKEBALL OPENS AND CAPTURES POKEMON)
                pokeballFrame = 1; // KEEP OPENED

                // WAIT FOR A MOMENT THEN CLOSE
                if (pokeballAnimationCounter > 15) {
                    pokeballFrame = 0; // CLOSE POKEBALL
                    hideEnemyPokemon = true; // HIDE ENEMY POKEMON (CAPTURED IN BALL)
                    pokeballAnimationStage = 2; // MOVE TO CATCHING STAGE
                    pokeballAnimationCounter = 0;
                    wobbleCount = 0;
                }
                break;

            case 2: // CATCHING STAGE (WOBBLE ANIMATION)
                pokeballFrame = 0; // KEEP CLOSED

                if (catchSuccessful) {
                    // POKEBALL FALLS TO GROUND
                    if (pokeballAnimationCounter < 10) {
                        pokeballY += 2; // FALL DOWN
                    }

                    // WOBBLE ANIMATION (3 WOBBLES)
                    if (pokeballAnimationCounter >= 10 && wobbleCount < 3) {
                        int wobblePhase = (pokeballAnimationCounter - 10) % 40;

                        if (wobblePhase < 20) {
                            // WOBBLE LEFT TO RIGHT
                            wobbleAngle = Math.sin(wobblePhase * Math.PI / 10) * 15;
                        } else if (wobblePhase == 20) {
                            // PAUSE BETWEEN WOBBLES
                            wobbleAngle = 0;
                            wobbleCount++;
                        }
                    }

                    // END AFTER 3 WOBBLES + PAUSE
                    if (pokeballAnimationCounter > 130) {
                        wobbleAngle = 0;
                        pokeballAnimationStage = 3; // COMPLETE - SUCCESS
                        pokeballAnimationCounter = 0;
                    }
                } else {
                    // POKEBALL FALLS TO GROUND
                    if (pokeballAnimationCounter < 10) {
                        pokeballY += 2; // FALL DOWN
                    }

                    // WOBBLE ONCE THEN BREAK FREE
                    if (pokeballAnimationCounter >= 10 && pokeballAnimationCounter < 30) {
                        int wobblePhase = (pokeballAnimationCounter - 10);
                        wobbleAngle = Math.sin(wobblePhase * Math.PI / 10) * 15;
                    }

                    // BREAK FREE
                    if (pokeballAnimationCounter >= 30) {
                        wobbleAngle = 0;
                        pokeballFrame = 1; // OPEN POKEBALL
                        hideEnemyPokemon = false; // SHOW ENEMY POKEMON AGAIN

                        if (pokeballAnimationCounter > 50) {
                            pokeballAnimationStage = 3; // COMPLETE - FAILED
                            pokeballAnimationCounter = 0;
                        }
                    }
                }
                break;

            case 3: // COMPLETE
                // WAIT A MOMENT THEN END
                if (pokeballAnimationCounter > 30) {
                    showPokeballThrow = false;
                    pokeballAnimationStage = 0;
                    // ONLY SHOW POKEMON AGAIN IF CATCH FAILED
                    if (!catchSuccessful) {
                        hideEnemyPokemon = false;
                    }
                    wobbleAngle = 0;
                }
                break;
        }
    }

    //=====================================
    // START ATTACK EFFECT ANIMATION
    //=====================================
    public void startAttackEffect(Type moveType, boolean targetIsEnemy) {
        showAttackEffect = true;
        attackEffectType = moveType;
        attackEffectFrame = 0;
        attackTargetIsEnemy = targetIsEnemy;
        
        // SET POSITION BASED ON TARGET
        if (targetIsEnemy) {
            // ENEMY POKEMON POSITION
            attackEffectX = gp.screenWidth - 350 + 110;
            attackEffectY = 40 + 110;
        } else {
            // PLAYER POKEMON POSITION
            attackEffectX = 50 + 140;
            attackEffectY = gp.screenHeight - 420 + 140;
        }
    }
    
    //=====================================
    // DRAW ATTACK EFFECT
    //=====================================
    private void drawAttackEffect(Graphics2D g2) {
        if (!showAttackEffect) return;
        
        float progress = (float) attackEffectFrame / attackEffectMaxFrames;
        int alpha = (int) (255 * (1 - progress)); // FADE OUT OVER TIME
        
        // SAVE ORIGINAL COMPOSITE
        Composite originalComposite = g2.getComposite();
        
        switch (attackEffectType) {
            case FIRE:
                drawFireEffect(g2, alpha, progress);
                break;
            case WATER:
                drawWaterEffect(g2, alpha, progress);
                break;
            case GRASS:
                drawGrassEffect(g2, alpha, progress);
                break;
            case ELECTRIC:
                drawElectricEffect(g2, alpha, progress);
                break;
            case ICE:
                drawIceEffect(g2, alpha, progress);
                break;
            case FIGHTING:
                drawFightingEffect(g2, alpha, progress);
                break;
            case POISON:
                drawPoisonEffect(g2, alpha, progress);
                break;
            case GROUND:
                drawGroundEffect(g2, alpha, progress);
                break;
            case FLYING:
                drawFlyingEffect(g2, alpha, progress);
                break;
            case PSYCHIC:
                drawPsychicEffect(g2, alpha, progress);
                break;
            case BUG:
                drawBugEffect(g2, alpha, progress);
                break;
            case ROCK:
                drawRockEffect(g2, alpha, progress);
                break;
            case GHOST:
                drawGhostEffect(g2, alpha, progress);
                break;
            case DRAGON:
                drawDragonEffect(g2, alpha, progress);
                break;
            case NORMAL:
            default:
                drawNormalEffect(g2, alpha, progress);
                break;
        }
        
        // RESTORE ORIGINAL COMPOSITE
        g2.setComposite(originalComposite);
    }
    
    //=====================================
    // INDIVIDUAL TYPE EFFECTS
    //=====================================
    private void drawFireEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        int size = (int) (80 + progress * 100);
        int x = attackEffectX - size / 2;
        int y = attackEffectY - size / 2;
        
        // OUTER FLAME (ORANGE)
        g2.setColor(new Color(255, 100, 0, alpha));
        g2.fillOval(x, y, size, size);
        
        // INNER FLAME (YELLOW)
        int innerSize = (int) (size * 0.6);
        g2.setColor(new Color(255, 200, 0, alpha));
        g2.fillOval(x + (size - innerSize) / 2, y + (size - innerSize) / 2, innerSize, innerSize);
    }
    
    private void drawWaterEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // WATER DROPLETS
        for (int i = 0; i < 6; i++) {
            double angle = (i * Math.PI / 3) + (progress * Math.PI * 2);
            int offsetX = (int) (Math.cos(angle) * 60 * progress);
            int offsetY = (int) (Math.sin(angle) * 60 * progress);
            
            g2.setColor(new Color(0, 120, 255, alpha));
            g2.fillOval(attackEffectX + offsetX - 15, attackEffectY + offsetY - 15, 30, 30);
            
            g2.setColor(new Color(150, 200, 255, alpha));
            g2.fillOval(attackEffectX + offsetX - 10, attackEffectY + offsetY - 10, 20, 20);
        }
    }
    
    private void drawGrassEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // LEAVES SWIRLING
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI / 4) + (progress * Math.PI * 3);
            int radius = (int) (50 + progress * 40);
            int offsetX = (int) (Math.cos(angle) * radius);
            int offsetY = (int) (Math.sin(angle) * radius);
            
            g2.setColor(new Color(50, 200, 50, alpha));
            int[] xPoints = {
                attackEffectX + offsetX, 
                attackEffectX + offsetX + 15, 
                attackEffectX + offsetX + 7
            };
            int[] yPoints = {
                attackEffectY + offsetY, 
                attackEffectY + offsetY + 10, 
                attackEffectY + offsetY + 20
            };
            g2.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    private void drawElectricEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        g2.setStroke(new BasicStroke(4));
        
        // LIGHTNING BOLTS
        for (int i = 0; i < 5; i++) {
            double angle = (i * Math.PI * 2 / 5);
            int endX = attackEffectX + (int) (Math.cos(angle) * 80 * progress);
            int endY = attackEffectY + (int) (Math.sin(angle) * 80 * progress);
            
            g2.setColor(new Color(255, 255, 0, alpha));
            g2.drawLine(attackEffectX, attackEffectY, endX, endY);
            
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.drawLine(attackEffectX, attackEffectY, 
                       attackEffectX + (int) ((endX - attackEffectX) * 0.7),
                       attackEffectY + (int) ((endY - attackEffectY) * 0.7));
        }
        
        // ELECTRIC CORE
        g2.setColor(new Color(255, 255, 200, alpha));
        g2.fillOval(attackEffectX - 20, attackEffectY - 20, 40, 40);
    }
    
    private void drawIceEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        int size = (int) (60 + progress * 40);
        
        // ICE CRYSTALS (HEXAGON)
        for (int ring = 0; ring < 2; ring++) {
            int ringSize = size - ring * 20;
            int[] xPoints = new int[6];
            int[] yPoints = new int[6];
            
            for (int i = 0; i < 6; i++) {
                double angle = i * Math.PI / 3;
                xPoints[i] = attackEffectX + (int) (Math.cos(angle) * ringSize);
                yPoints[i] = attackEffectY + (int) (Math.sin(angle) * ringSize);
            }
            
            g2.setColor(new Color(150, 200, 255, alpha));
            g2.fillPolygon(xPoints, yPoints, 6);
            
            g2.setColor(new Color(200, 230, 255, alpha));
            g2.setStroke(new BasicStroke(3));
            g2.drawPolygon(xPoints, yPoints, 6);
        }
    }
    
    private void drawFightingEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // PUNCH IMPACT LINES
        g2.setStroke(new BasicStroke(5));
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int startRadius = (int) (20 * progress);
            int endRadius = (int) (70 * progress);
            
            int startX = attackEffectX + (int) (Math.cos(angle) * startRadius);
            int startY = attackEffectY + (int) (Math.sin(angle) * startRadius);
            int endX = attackEffectX + (int) (Math.cos(angle) * endRadius);
            int endY = attackEffectY + (int) (Math.sin(angle) * endRadius);
            
            g2.setColor(new Color(200, 50, 0, alpha));
            g2.drawLine(startX, startY, endX, endY);
        }
    }
    
    private void drawPoisonEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // POISON BUBBLES
        for (int i = 0; i < 10; i++) {
            double angle = i * Math.PI * 2 / 10;
            int radius = (int) (40 + progress * 50);
            int offsetX = (int) (Math.cos(angle) * radius);
            int offsetY = (int) (Math.sin(angle) * radius) - (int) (progress * 30);
            int bubbleSize = 15 + (i % 3) * 5;
            
            g2.setColor(new Color(160, 32, 240, alpha));
            g2.fillOval(attackEffectX + offsetX - bubbleSize / 2, 
                       attackEffectY + offsetY - bubbleSize / 2, 
                       bubbleSize, bubbleSize);
        }
    }
    
    private void drawGroundEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // ROCKS FLYING UP
        for (int i = 0; i < 6; i++) {
            int offsetX = (i - 3) * 30;
            int offsetY = -(int) (progress * 80);
            int rockSize = 20 + (i % 3) * 10;
            
            g2.setColor(new Color(139, 90, 43, alpha));
            g2.fillRect(attackEffectX + offsetX - rockSize / 2, 
                       attackEffectY + offsetY, 
                       rockSize, rockSize);
            
            g2.setColor(new Color(101, 67, 33, alpha));
            g2.drawRect(attackEffectX + offsetX - rockSize / 2, 
                       attackEffectY + offsetY, 
                       rockSize, rockSize);
        }
    }
    
    private void drawFlyingEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // WIND GUSTS
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < 5; i++) {
            int y = attackEffectY - 50 + i * 25;
            int xOffset = (int) (progress * 100);
            
            g2.setColor(new Color(200, 230, 255, alpha));
            for (int j = 0; j < 3; j++) {
                int x = attackEffectX - 60 + xOffset + j * 15;
                g2.drawArc(x, y, 40, 20, 0, 180);
            }
        }
    }
    
    private void drawPsychicEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // PSYCHIC RINGS
        for (int i = 0; i < 4; i++) {
            int size = (int) (50 + i * 30 + progress * 80);
            int ringAlpha = (int) (alpha * (1 - i * 0.2));
            
            g2.setColor(new Color(255, 0, 255, ringAlpha));
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(attackEffectX - size / 2, attackEffectY - size / 2, size, size);
        }
    }
    
    private void drawBugEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // BUG BITE MARKS
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3 + progress * Math.PI * 2;
            int radius = 40;
            int offsetX = (int) (Math.cos(angle) * radius);
            int offsetY = (int) (Math.sin(angle) * radius);
            
            g2.setColor(new Color(100, 180, 50, alpha));
            g2.fillOval(attackEffectX + offsetX - 10, attackEffectY + offsetY - 10, 20, 20);
            
            g2.setColor(new Color(50, 120, 20, alpha));
            g2.setStroke(new BasicStroke(2));
            g2.drawArc(attackEffectX + offsetX - 10, attackEffectY + offsetY - 10, 20, 20, 45, 180);
        }
    }
    
    private void drawRockEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // FALLING ROCKS
        for (int i = 0; i < 5; i++) {
            int offsetX = (i - 2) * 40;
            int offsetY = (int) (progress * 100) - 50;
            int rockSize = 30 + (i % 2) * 15;
            
            g2.setColor(new Color(120, 100, 80, alpha));
            int[] xPoints = {
                attackEffectX + offsetX,
                attackEffectX + offsetX + rockSize,
                attackEffectX + offsetX + rockSize - 10,
                attackEffectX + offsetX + 10
            };
            int[] yPoints = {
                attackEffectY + offsetY,
                attackEffectY + offsetY + 10,
                attackEffectY + offsetY + rockSize,
                attackEffectY + offsetY + rockSize - 5
            };
            g2.fillPolygon(xPoints, yPoints, 4);
        }
    }
    
    private void drawGhostEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // GHOSTLY WISPS
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4 + progress * Math.PI;
            int radius = (int) (40 + Math.sin(progress * Math.PI * 4 + i) * 20);
            int offsetX = (int) (Math.cos(angle) * radius);
            int offsetY = (int) (Math.sin(angle) * radius);
            
            g2.setColor(new Color(120, 0, 180, alpha / 2));
            g2.fillOval(attackEffectX + offsetX - 20, attackEffectY + offsetY - 20, 40, 40);
        }
        
        // DARK CENTER
        g2.setColor(new Color(80, 0, 120, alpha));
        g2.fillOval(attackEffectX - 30, attackEffectY - 30, 60, 60);
    }
    
    private void drawDragonEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // DRAGON RAGE (SPIRAL FLAMES)
        for (int i = 0; i < 12; i++) {
            double angle = i * Math.PI / 6 + progress * Math.PI * 3;
            int radius = (int) (30 + i * 5);
            int offsetX = (int) (Math.cos(angle) * radius);
            int offsetY = (int) (Math.sin(angle) * radius);
            
            // GRADIENT FROM BLUE TO PURPLE
            int r = (int) (0 + progress * 200);
            int g = (int) (100 - progress * 100);
            int b = 255;
            
            g2.setColor(new Color(r, g, b, alpha));
            g2.fillOval(attackEffectX + offsetX - 15, attackEffectY + offsetY - 15, 30, 30);
        }
    }
    
    private void drawNormalEffect(Graphics2D g2, int alpha, float progress) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha / 255f))));
        
        // SIMPLE IMPACT FLASH
        int size = (int) (50 + progress * 80);
        g2.setColor(new Color(255, 255, 255, alpha));
        g2.fillOval(attackEffectX - size / 2, attackEffectY - size / 2, size, size);
        
        g2.setColor(new Color(200, 200, 200, alpha));
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(attackEffectX - size / 2, attackEffectY - size / 2, size, size);
    }

    //=====================================
    // CHECK IF POKEBALL ANIMATION IS ACTIVE
    //=====================================
    public boolean isPokeballAnimationActive() {
        return showPokeballThrow;
    }

    //=====================================
    // UPDATE HP ANIMATION
    //=====================================
    public void updateHPAnimation(Pokemon playerPokemon, Pokemon enemyPokemon) {
        // INITIALIZE DISPLAYED HP ON FIRST CALL
        if (playerDisplayedHP == 0 && playerPokemon != null) {
            playerDisplayedHP = playerPokemon.getHp();
        }
        if (enemyDisplayedHP == 0 && enemyPokemon != null) {
            enemyDisplayedHP = enemyPokemon.getHp();
        }

        // ANIMATE PLAYER HP
        if (playerPokemon != null) {
            double targetHP = playerPokemon.getHp();
            if (playerDisplayedHP > targetHP) {
                playerDisplayedHP -= hpAnimationSpeed;
                if (playerDisplayedHP < targetHP) {
                    playerDisplayedHP = targetHP;
                }
            } else if (playerDisplayedHP < targetHP) {
                playerDisplayedHP += hpAnimationSpeed;
                if (playerDisplayedHP > targetHP) {
                    playerDisplayedHP = targetHP;
                }
            }
        }

        // ANIMATE ENEMY HP
        if (enemyPokemon != null) {
            double targetHP = enemyPokemon.getHp();
            if (enemyDisplayedHP > targetHP) {
                enemyDisplayedHP -= hpAnimationSpeed;
                if (enemyDisplayedHP < targetHP) {
                    enemyDisplayedHP = targetHP;
                }
            } else if (enemyDisplayedHP < targetHP) {
                enemyDisplayedHP += hpAnimationSpeed;
                if (enemyDisplayedHP > targetHP) {
                    enemyDisplayedHP = targetHP;
                }
            }
        }
    }

    //=====================================
    // RESET HP ANIMATION
    //=====================================
    public void resetHPAnimation(Pokemon playerPokemon, Pokemon enemyPokemon) {
        if (playerPokemon != null) {
            playerDisplayedHP = playerPokemon.getHp();
        }
        if (enemyPokemon != null) {
            enemyDisplayedHP = enemyPokemon.getHp();
        }
    }

    //=====================================
    // DRAW BATTLE SCREEN
    //=====================================
    public void draw(Graphics2D g2, Pokemon playerPokemon, Pokemon wildPokemon) {
        //======================================
        // DRAW BACKGROUND FROM SPRITE SHEET
        //======================================
        // CALCULATE BATTLE AREA 
        int uiHeight = 150; // HEIGHT RESERVED FOR UI/DIALOGUE BOX AT BOTTOM
        int battleAreaHeight = gp.screenHeight - uiHeight;

        if (battleBackground != null) {
            // SCALE AND DRAW THE SPRITE SHEET BACKGROUND TO FIT BATTLE AREA ONLY
            g2.drawImage(battleBackground, 0, 0, gp.screenWidth, battleAreaHeight, null);

            // FILL REMAINING SPACE (UI AREA) WITH BLACK OR NEUTRAL COLOR
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, battleAreaHeight, gp.screenWidth, uiHeight);
        } else {
            // FALLBACK: USE SOLID COLORS IF SPRITE SHEET NOT LOADED
            // SKY (TOP HALF OF BATTLE AREA)
            g2.setColor(new Color(200, 230, 200));
            g2.fillRect(0, 0, gp.screenWidth, battleAreaHeight / 2);

            // GROUND (BOTTOM HALF OF BATTLE AREA)
            g2.setColor(new Color(139, 90, 43));
            g2.fillRect(0, battleAreaHeight / 2, gp.screenWidth, battleAreaHeight / 2);

            // UI AREA (BLACK BACKGROUND)
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, battleAreaHeight, gp.screenWidth, uiHeight);
        }

        // SHOW TRAINER INTRO ANIMATION IF ACTIVE (USED FOR BOTH BATTLE START AND POKEMON SWITCH)
        if (showTrainerIntro && trainerIntroFrames != null) {
            // DRAW TRAINER THROWING POKEBALL ANIMATION
            BufferedImage currentFrame = trainerIntroFrames[trainerIntroFrame];

            // SMALLER SIZE AND HIGHER POSITION TO AVOID DIALOGUE BOX
            int trainerWidth = 280;
            int trainerHeight = 280;
            int trainerX = -30 + trainerSlideOffset;  // SLIDE-IN FROM LEFT
            int trainerY = gp.screenHeight - trainerHeight - 140;

            g2.drawImage(currentFrame, trainerX, trainerY, trainerWidth, trainerHeight, null);

            // ALWAYS DRAW WILD POKEMON DURING ANIMATION 
            if (wildPokemon != null && !hideEnemyPokemon) {
                int enemyX = gp.screenWidth - 350 + 25; 
                int enemyY = 40 + 25;
                int enemySpriteSize = 220;

                if (pokemonSprites != null) {
                    BufferedImage enemySprite = getPokemonSprite(wildPokemon.getName(), false);
                    if (enemySprite != null) {
                        g2.drawImage(enemySprite, enemyX, enemyY, enemySpriteSize, enemySpriteSize, null);
                    } else {
                        // FALLBACK: DRAW PLACEHOLDER
                        drawPlaceholderSprite(g2, enemyX, enemyY, enemySpriteSize, enemySpriteSize, Color.RED);
                    }
                } else {
                    // FALLBACK: DRAW PLACEHOLDER
                    drawPlaceholderSprite(g2, enemyX, enemyY, enemySpriteSize, enemySpriteSize, Color.RED);
                }

                // ENEMY INFO BOX
                drawInfoBox(g2, wildPokemon, 50, 50, false, enemyDisplayedHP);
            }

            // DON'T DRAW PLAYER POKEMON DURING ANIMATION
        } else {
            // DRAW WILD POKEMON (ENEMY SIDE - TOP RIGHT) - MUCH BIGGER SIZE
            // ONLY DRAW IF NOT HIDDEN (WHEN CAUGHT IN POKEBALL)
            if (wildPokemon != null && !hideEnemyPokemon) {
                int enemyX = gp.screenWidth - 350 + 25; 
                int enemyY = 40 + 25;
                int enemySpriteSize = 220;

                if (pokemonSprites != null) {
                    BufferedImage enemySprite = getPokemonSprite(wildPokemon.getName(), false);
                    if (enemySprite != null) {
                        g2.drawImage(enemySprite, enemyX, enemyY, enemySpriteSize, enemySpriteSize, null);
                    } else {
                        // FALLBACK: DRAW PLACEHOLDER
                        drawPlaceholderSprite(g2, enemyX, enemyY, enemySpriteSize, enemySpriteSize, Color.RED);
                    }
                } else {
                    // FALLBACK: DRAW PLACEHOLDER
                    drawPlaceholderSprite(g2, enemyX, enemyY, enemySpriteSize, enemySpriteSize, Color.RED);
                }

                // ENEMY INFO BOX
                drawInfoBox(g2, wildPokemon, 50, 50, false, enemyDisplayedHP);
            }

            // DRAW PLAYER POKEMON (PLAYER SIDE - BOTTOM LEFT) - MUCH BIGGER AND FULLY VISIBLE
            if (playerPokemon != null) {
                int playerSpriteSize = 280;
                int playerX = 50;
                // POSITION SO THE ENTIRE SPRITE IS VISIBLE ABOVE THE UI
                int playerY = gp.screenHeight - 420 + 30;

                if (pokemonSprites != null) {
                    BufferedImage playerSprite = getPokemonSprite(playerPokemon.getName(), true);
                    if (playerSprite != null) {
                        g2.drawImage(playerSprite, playerX, playerY, playerSpriteSize, playerSpriteSize, null);
                    } else {
                        // FALLBACK: DRAW PLACEHOLDER
                        drawPlaceholderSprite(g2, playerX, playerY, playerSpriteSize, playerSpriteSize, Color.BLUE);
                    }
                } else {
                    // FALLBACK: DRAW PLACEHOLDER
                    drawPlaceholderSprite(g2, playerX, playerY, playerSpriteSize, playerSpriteSize, Color.BLUE);
                }

                // PLAYER INFO BOX (POSITIONED TO NOT OVERLAP WITH SPRITE)
                drawInfoBox(g2, playerPokemon, gp.screenWidth - 300, gp.screenHeight - 290, true, playerDisplayedHP);
            }
        }

        // DRAW POKEBALL THROW ANIMATION (DRAWN OVER EVERYTHING ELSE)
        if (showPokeballThrow && pokeballSprites != null) {
            BufferedImage pokeballSprite = pokeballSprites[pokeballFrame];
            if (pokeballSprite != null) {
                // USE DIFFERENT SIZE FOR TRAINER INTRO VS CATCHING
                int pokeballSize = (pokeballAnimationStage == -1) ? 48 : 64;

                // APPLY WOBBLE ROTATION
                if (wobbleAngle != 0) {
                    // SAVE ORIGINAL TRANSFORM
                    var originalTransform = g2.getTransform();

                    // ROTATE AROUND POKEBALL CENTER
                    int centerX = (int) pokeballX + pokeballSize / 2;
                    int centerY = (int) pokeballY + pokeballSize / 2;
                    g2.rotate(Math.toRadians(wobbleAngle), centerX, centerY);

                    // DRAW POKEBALL
                    g2.drawImage(pokeballSprite, (int) pokeballX, (int) pokeballY, pokeballSize, pokeballSize, null);

                    // RESTORE ORIGINAL TRANSFORM
                    g2.setTransform(originalTransform);
                } else {
                    // DRAW POKEBALL WITHOUT ROTATION
                    g2.drawImage(pokeballSprite, (int) pokeballX, (int) pokeballY, pokeballSize, pokeballSize, null);
                }
            } else {
                System.out.println("ERROR: Pokeball sprite is null! Frame: " + pokeballFrame);
            }
        } else if (showPokeballThrow && pokeballSprites == null) {
            System.out.println("ERROR: showPokeballThrow is true but pokeballSprites is null!");
        }
        
        // DRAW ATTACK EFFECTS (DRAWN OVER POKEMON)
        if (showAttackEffect) {
            drawAttackEffect(g2);
        }

        // TRANSITION EFFECT (DRAWN LAST)
        if (transitionAlpha > 0) {
            g2.setColor(new Color(0, 0, 0, Math.max(0, Math.min(transitionAlpha, 255))));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }
    }

    //=====================================
    // DRAW PLACEHOLDER SPRITE
    //=====================================
    private void drawPlaceholderSprite(Graphics2D g2, int x, int y, int width, int height, Color color) {
        g2.setColor(color);
        g2.fillOval(x, y, width, height);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, width, height);
    }

    //=====================================
    // GET POKEMON SPRITE
    //=====================================
    public BufferedImage getPokemonSprite(String pokemonName, boolean isBack) {
        if (pokemonSprites == null || pokemonSprites.length == 0) {
            return null;
        }

        // MAP POKEMON NAME TO SPRITE INDEX
        int spriteIndex = -1;

        switch (pokemonName.toLowerCase()) {
            case "bulbasaur":
                spriteIndex = 0;
                break;
            case "charmander":
                spriteIndex = 1;
                break;
            case "squirtle":
                spriteIndex = 2;
                break;
            case "pidgey":
                spriteIndex = 3;
                break;
            case "rattata":
                spriteIndex = 4;
                break;
            case "cubone":
                spriteIndex = 5;
                break;
            case "electabuzz":
                spriteIndex = 6;
                break;
            case "farfetch'd":
            case "farfetchd":
                spriteIndex = 7;
                break;
            case "growlithe":
                spriteIndex = 8;
                break;
            case "jynx":
                spriteIndex = 9;
                break;
            case "kangaskhan":
                spriteIndex = 10;
                break;
            case "machop":
                spriteIndex = 11;
                break;
            case "magmar":
                spriteIndex = 12;
                break;
            case "psyduck":
                spriteIndex = 13;
                break;
            case "scyther":
                spriteIndex = 14;
                break;
            case "ivysaur":
                spriteIndex = 15;
                break;
            case "charmeleon":
                spriteIndex = 16;
                break;
            case "wartortle":
                spriteIndex = 17;
                break;
            case "venusaur":
                spriteIndex = 18;
                break;
            case "charizard":
                spriteIndex = 19;
                break;
            case "blastoise":
                spriteIndex = 20;
                break;
            default:
                System.out.println("WARNING: No sprite mapping found for Pokemon: " + pokemonName);
                return null; // POKEMON NOT FOUND
        }

        // GET SPRITE: [POKEMON_ID][0=FRONT, 1=BACK]
        if (spriteIndex >= 0 && spriteIndex < pokemonSprites.length) {
            int spriteType = isBack ? 1 : 0; // 0 = FRONT (ENEMY), 1 = BACK (PLAYER)
            return pokemonSprites[spriteIndex][spriteType];
        }

        return null;
    }

    //=====================================
    // DRAW INFO BOX
    //=====================================
    private void drawInfoBox(Graphics2D g2, Pokemon pokemon, int x, int y, boolean showExp, double displayedHP) {
        // BOX BACKGROUND
        g2.setColor(new Color(248, 248, 248, 230));
        g2.fillRoundRect(x, y, 250, showExp ? 90 : 70, 10, 10);

        // BOX BORDER
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, 250, showExp ? 90 : 70, 10, 10);

        // POKEMON NAME AND LEVEL
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString(pokemon.getName(), x + 10, y + 25);
        g2.drawString("Lv" + pokemon.getLevel(), x + 200, y + 25);

        // HP BAR BACKGROUND
        g2.setColor(new Color(200, 200, 200));
        g2.fillRect(x + 50, y + 40, 180, 12);

        // HP BAR WITH ANIMATION
        double hpPercent = displayedHP / pokemon.getMaxHp();
        Color hpColor;
        if (hpPercent > 0.5) {
            hpColor = new Color(0, 200, 0);
        } else if (hpPercent > 0.2) {
            hpColor = new Color(255, 200, 0);
        } else {
            hpColor = new Color(255, 0, 0);
        }
        g2.setColor(hpColor);
        int hpBarWidth = (int) (180 * hpPercent);
        g2.fillRect(x + 50, y + 40, Math.max(0, hpBarWidth), 12);

        // HP BAR BORDER
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(x + 50, y + 40, 180, 12);

        // HP TEXT
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("HP:", x + 10, y + 50);

        // ALWAYS SHOW HP NUMBERS (FOR BOTH PLAYER AND OPPONENT)
        // SHOW ANIMATED HP VALUE
        int displayedHPInt = (int) Math.ceil(displayedHP);
        g2.drawString(displayedHPInt + "/" + pokemon.getMaxHp(), x + 50, y + 65);

        if (showExp) {
            // EXP BAR (ONLY FOR PLAYER)
            g2.setColor(new Color(100, 150, 255));
            double expPercent = (double) pokemon.getExp() / pokemon.getNextLevelExp();
            g2.fillRect(x + 50, y + 75, (int) (180 * expPercent), 8);

            // EXP BAR BORDER
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(x + 50, y + 75, 180, 8);
        }
    }

    //=====================================
    // CHECK IF TRANSITION COMPLETE
    //=====================================
    public boolean isTransitionComplete() {
        return !isTransitioning && transitionAlpha <= 0;
    }

    //=====================================
    // SET BATTLE BACKGROUND FROM SPRITE SHEET
    //=====================================
    // ALLOWS CHANGING BACKGROUND SPRITE COORDINATES AT RUNTIME
    // USE THIS TO SELECT DIFFERENT BACKGROUNDS FROM YOUR SPRITE SHEET
    public void setBattleBackground(int x, int y, int width, int height) {
        try {
            BufferedImage backgroundSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/battle_backgrounds.png"));

            if (backgroundSheet == null) {
                System.out.println("ERROR: Battle background sprite sheet not found!");
                return;
            }

            // CHECK IF COORDINATES ARE VALID
            if (x + width <= backgroundSheet.getWidth() && y + height <= backgroundSheet.getHeight()) {
                battleBackground = backgroundSheet.getSubimage(x, y, width, height);
                System.out.println("✓ Battle background updated! Coords: (" + x + "," + y + ") Size: " + width + "x" + height);
            } else {
                System.out.println("ERROR: Invalid background coordinates!");
                System.out.println("  Requested: x=" + x + " y=" + y + " w=" + width + " h=" + height);
                System.out.println("  Sheet size: " + backgroundSheet.getWidth() + "x" + backgroundSheet.getHeight());
            }

        } catch (Exception e) {
            System.out.println("✗ Could not update battle background: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
