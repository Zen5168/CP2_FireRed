package com.game.logic;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BattleScreen {

    private GamePanel gp;
    private BufferedImage[][] pokemonSprites; // [ROW][COL]
    
    private int transitionAlpha = 0;
    private boolean isTransitioning = false;
    private int transitionSpeed = 15;
    
    public BattleScreen(GamePanel gp) {
        this.gp = gp;
        loadBattleAssets();
    }
    
    //=====================================
    // LOAD BATTLE SPRITES
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
                {new Rectangle(11, 47, 63, 63), new Rectangle(11, 47, 63, 63)},
                
                // CHARMANDER [1]
                {new Rectangle(11, 45, 66, 66), new Rectangle(11, 47, 63, 63)},
                
                // SQUIRTLE [2]
                {new Rectangle(0, 64, 32, 32), new Rectangle(32, 64, 32, 32)},
                
                // PIDGEY [3]
                {new Rectangle(0, 96, 32, 32), new Rectangle(32, 96, 32, 32)},
                
                // RATTATA [4]
                {new Rectangle(0, 128, 32, 32), new Rectangle(32, 128, 32, 32)},
                
                // ADD MORE POKEMON HERE AS NEEDED
                // {new Rectangle(x, y, width, height), new Rectangle(x, y, width, height)},
            };
            
            // EXTRACT SPRITES USING THE MAP
            for (int i = 0; i < spriteMap.length; i++) {
                // FRONT SPRITE
                Rectangle frontRect = spriteMap[i][0];
                pokemonSprites[i][0] = spriteSheet.getSubimage(
                    frontRect.x, 
                    frontRect.y, 
                    frontRect.width, 
                    frontRect.height
                );
                
                // BACK SPRITE
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
    }
    
    //=====================================
    // START TRANSITION
    //=====================================
    public void startTransition() {
        isTransitioning = true;
        transitionAlpha = 255; // START FULLY BLACK
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
        
        // DRAW WILD POKEMON (ENEMY SIDE - TOP RIGHT)
        if (wildPokemon != null) {
            int enemyX = gp.screenWidth - 200;
            int enemyY = 100;
            
            if (pokemonSprites != null) {
                BufferedImage enemySprite = getPokemonSprite(wildPokemon.getName(), false);
                if (enemySprite != null) {
                    g2.drawImage(enemySprite, enemyX, enemyY, 96, 96, null);
                } else {
                    // FALLBACK: DRAW PLACEHOLDER
                    drawPlaceholderSprite(g2, enemyX, enemyY, 96, 96, Color.RED);
                }
            } else {
                // FALLBACK: DRAW PLACEHOLDER
                drawPlaceholderSprite(g2, enemyX, enemyY, 96, 96, Color.RED);
            }
            
            // ENEMY INFO BOX
            drawInfoBox(g2, wildPokemon, 50, 50, false);
        }
        
        // DRAW PLAYER POKEMON (PLAYER SIDE - BOTTOM LEFT)
        if (playerPokemon != null) {
            int playerX = 100;
            int playerY = gp.screenHeight - 250;
            
            if (pokemonSprites != null) {
                BufferedImage playerSprite = getPokemonSprite(playerPokemon.getName(), true);
                if (playerSprite != null) {
                    g2.drawImage(playerSprite, playerX, playerY, 96, 96, null);
                } else {
                    // FALLBACK: DRAW PLACEHOLDER
                    drawPlaceholderSprite(g2, playerX, playerY, 96, 96, Color.BLUE);
                }
            } else {
                // FALLBACK: DRAW PLACEHOLDER
                drawPlaceholderSprite(g2, playerX, playerY, 96, 96, Color.BLUE);
            }
            
            // PLAYER INFO BOX (MOVED UP TO AVOID UI OVERLAP)
            drawInfoBox(g2, playerPokemon, gp.screenWidth - 300, gp.screenHeight - 280, true);
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
    private void drawInfoBox(Graphics2D g2, Pokemon pokemon, int x, int y, boolean showExp) {
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
        
        // HP BAR
        double hpPercent = (double) pokemon.getHp() / pokemon.getMaxHp();
        Color hpColor;
        if (hpPercent > 0.5) {
            hpColor = new Color(0, 200, 0);
        } else if (hpPercent > 0.2) {
            hpColor = new Color(255, 200, 0);
        } else {
            hpColor = new Color(255, 0, 0);
        }
        g2.setColor(hpColor);
        g2.fillRect(x + 50, y + 40, (int)(180 * hpPercent), 12);
        
        // HP TEXT
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("HP:", x + 10, y + 50);
        
        // ALWAYS SHOW HP NUMBERS (FOR BOTH PLAYER AND OPPONENT)
        g2.drawString(pokemon.getHp() + "/" + pokemon.getMaxHp(), x + 50, y + 65);
        
        if (showExp) {
            // EXP BAR (ONLY FOR PLAYER)
            g2.setColor(new Color(100, 150, 255));
            double expPercent = (double) pokemon.getExp() / pokemon.getNextLevelExp();
            g2.fillRect(x + 50, y + 75, (int)(180 * expPercent), 8);
        }
    }
    
    //=====================================
    // CHECK IF TRANSITION COMPLETE
    //=====================================
    public boolean isTransitionComplete() {
        return !isTransitioning && transitionAlpha <= 0;
    }
}
