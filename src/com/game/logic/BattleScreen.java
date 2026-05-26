package com.game.logic;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BattleScreen {

    private GamePanel gp;
    private BufferedImage[][] pokemonSprites; // [ROW][COL]
    private BufferedImage[] trainerIntroFrames; // TRAINER THROWING POKEBALL ANIMATION FRAMES
    
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
    
    public BattleScreen(GamePanel gp) {
        this.gp = gp;
        loadBattleAssets();
    }
    
    //=====================================
    // LOAD POKEMON BATTLE SPRITES
    //=====================================
    private void loadBattleAssets() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/pokemon_battle_sprites.png"));
            
            // INITIALIZE SPRITE ARRAY (ADJUST SIZE AS NEEDED)
            pokemonSprites = new BufferedImage[20][2]; // [POKEMON_ID][0=FRONT, 1=BACK]
            
            // MAP EACH POKEMON SPRITE TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            // FORMAT: [POKEMON_ID][FRONT_SPRITE, BACK_SPRITE]
            Rectangle[][] spriteMap = {
                // BULBASAUR [0]
                {new Rectangle(12, 46, 63, 63), new Rectangle(12, 111, 63, 63)},
                
                // CHARMANDER [1]
                {new Rectangle(401, 46, 63, 63), new Rectangle(402, 111, 63, 63)},
                
                // SQUIRTLE [2]
                {new Rectangle(792, 46, 63, 63), new Rectangle(792, 111, 63, 63)},
                
                // PIDGEY [3]
                {new Rectangle(12, 212, 63, 63), new Rectangle(12, 276, 63, 63)},
                
                // RATTATA [4]
                {new Rectangle(401, 212, 63, 63), new Rectangle(401, 276, 63, 63)},        
                
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
            
            // MAP EACH FRAME TO ITS LOCATION ON THE SHEET: {X, Y, WIDTH, HEIGHT}
            // ADJUST THESE VALUES BASED ON YOUR SPRITE SHEET LAYOUT
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
                new Rectangle(196, 0, 51, 51),  
            };
            
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
        
        // UPDATE TRAINER INTRO ANIMATION
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
                }
            }
        }
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
        // BACKGROUND
        g2.setColor(new Color(200, 230, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // GROUND
        g2.setColor(new Color(139, 90, 43));
        g2.fillRect(0, gp.screenHeight / 2, gp.screenWidth, gp.screenHeight / 2);
        
        // SHOW TRAINER INTRO ANIMATION IF ACTIVE 
        if (showTrainerIntro && trainerIntroFrames != null) {
            // DRAW TRAINER THROWING POKEBALL ANIMATION
            BufferedImage currentFrame = trainerIntroFrames[trainerIntroFrame];
            
            // SMALLER SIZE AND HIGHER POSITION TO AVOID DIALOGUE BOX
            int trainerWidth = 280;  
            int trainerHeight = 280; 
            int trainerX = -30 + trainerSlideOffset;  // SLIDE-IN FROM LEFT
            int trainerY = gp.screenHeight - trainerHeight - 140; 
            
            g2.drawImage(currentFrame, trainerX, trainerY, trainerWidth, trainerHeight, null);
            
            // DON'T DRAW POKEMON DURING INTRO ANIMATION
        } else {
            // DRAW WILD POKEMON (ENEMY SIDE - TOP RIGHT) - MUCH BIGGER SIZE
            if (wildPokemon != null) {
                int enemyX = gp.screenWidth - 350;
                int enemyY = 40;
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
                int playerY = gp.screenHeight - 420;
                
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
                drawInfoBox(g2, playerPokemon, gp.screenWidth - 300, gp.screenHeight - 300, true, playerDisplayedHP);
            }
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
    private BufferedImage getPokemonSprite(String pokemonName, boolean isBack) {
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
            // ADD MORE POKEMON HERE
            default:
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
        int hpBarWidth = (int)(180 * hpPercent);
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
        int displayedHPInt = (int)Math.ceil(displayedHP);
        g2.drawString(displayedHPInt + "/" + pokemon.getMaxHp(), x + 50, y + 65);
        
        if (showExp) {
            // EXP BAR (ONLY FOR PLAYER)
            g2.setColor(new Color(100, 150, 255));
            double expPercent = (double) pokemon.getExp() / pokemon.getNextLevelExp();
            g2.fillRect(x + 50, y + 75, (int)(180 * expPercent), 8);
            
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
}
