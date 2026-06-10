package com.game.ui;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import com.game.logic.GameState;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EvolutionUI {
    
    private GamePanel gp;
    private Pokemon evolvingPokemon;
    private String oldName;
    private int stage = 0; // 0 = WHAT?, 1 = IS EVOLVING, 2 = ANIMATION, 3 = CONGRATULATIONS
    private int frameCounter = 0;
    private boolean evolutionComplete = false;
    
    // SPRITE ANIMATION
    private BufferedImage oldSprite; // PRE - EVOLUTION
    private BufferedImage newSprite; // POST - EVOLUTION
    private boolean showSprite = true;
    private int flashCounter = 0;
    private float spriteAlpha = 1.0f;
    private boolean growing = false;
    private float spriteScale = 1.0f;
    
    public EvolutionUI(GamePanel gp) {
        this.gp = gp;
    }
    
    public void startEvolution(Pokemon pokemon) {
        this.evolvingPokemon = pokemon;
        this.oldName = pokemon.getName();
        this.stage = 0;
        this.frameCounter = 0;
        this.evolutionComplete = false;
        this.showSprite = true;
        this.flashCounter = 0;
        this.spriteAlpha = 1.0f;
        this.spriteScale = 1.0f;
        this.growing = false;
        
        // STOP MUSIC DURING EVOLUTION
        gp.audioManager.stopCurrent();
        
        // GET OLD SPRITE (FRONT SPRITE)
        this.oldSprite = gp.battleScreen.getPokemonSprite(oldName, false);
    }
    
    public void update() {
        if (evolvingPokemon == null || evolutionComplete) {
            return;
        }
        
        frameCounter++;
        
        switch (stage) {
            case 0: // "WHAT?"
                if (frameCounter > 60) { // WAIT 1 SECOND (60 FRAMES)
                    stage = 1;
                    frameCounter = 0;
                }
                break;
                
            case 1: // "X IS EVOLVING!" - SHOW SPRITE
                if (frameCounter > 60) { // WAIT 1 SECOND
                    stage = 2;
                    frameCounter = 0;
                }
                break;
                
            case 2: // ANIMATION (FLASHING SPRITE)
                flashCounter++;
                
                // FLASH EFFECT - SPRITE APPEARS/DISAPPEARS
                if (flashCounter % 10 < 5) {
                    showSprite = true;
                    spriteAlpha = 1.0f;
                } else {
                    spriteAlpha = 0.3f;
                }
                
                // GROWING EFFECT
                if (flashCounter % 20 < 10) {
                    spriteScale = 1.0f + (flashCounter % 10) * 0.02f;
                } else {
                    spriteScale = 1.2f - ((flashCounter % 10) * 0.02f);
                }
                
                // AFTER FLASHING ANIMATION, PERFORM EVOLUTION
                if (frameCounter > 120) { // 2 SECONDS OF ANIMATION
                    // POKEMON EVOLVES
                    evolvingPokemon.evolve();
                    
                    // GET NEW SPRITE (FRONT SPRITE)
                    newSprite = gp.battleScreen.getPokemonSprite(evolvingPokemon.getName(), false);
                    
                    stage = 3;
                    frameCounter = 0;
                    showSprite = true;
                    spriteAlpha = 1.0f;
                    spriteScale = 1.0f;
                }
                break;
                
            case 3: // "CONGRATULATIONS!" - SHOW NEW SPRITE
                if (frameCounter > 120) { // WAIT 2 SECONDS
                    
                    // CHECK IF THERE'S ANOTHER POKEMON THAT NEEDS TO EVOLVE
                    boolean hasMoreEvolutions = false;
                    for (Pokemon p : gp.playerTrainer.getParty()) {
                        if (p.hasPendingEvolution()) {
                            startEvolution(p);
                            hasMoreEvolutions = true;
                            break;
                        }
                    }
                    
                    if (!hasMoreEvolutions) {
                        // MARK AS COMPLETED AND TRANSITION TO OVERWORLD
                        evolutionComplete = true;
                        evolvingPokemon = null;
                        gp.gameState = GameState.OVERWORLD;
                        gp.audioManager.playWithLoop("BGM.wav", 0);
                        
                        // RESET ENCOUNTER TRANSITION TO PREVENT BLACK SCREEN
                        gp.encounterTransition.stopTransition();
                    }
                }
                break;
        }
    }
    
    public void draw(Graphics2D g2) {
        if (evolvingPokemon == null) {
            return;
        }
        
        // WHITE BACKGROUND 
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        int centerX = gp.screenWidth / 2;
        int centerY = gp.screenHeight / 2;
        
        // DRAW SPRITE DURING ANIMATION STAGES
        if (stage >= 1 && stage <= 3) {
            BufferedImage currentSprite = (stage == 3) ? newSprite : oldSprite;
            
            if (currentSprite != null) {
                // SET TRANSPARENCY FOR FLASH EFFECT
                Composite originalComposite = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, spriteAlpha));
                
                // CALCULATE SPRITE SIZE AND POSITION
                int spriteSize = (int)(200 * spriteScale);
                int spriteX = centerX - spriteSize / 2;
                int spriteY = centerY - spriteSize / 2 - 50; // SLIGHTLY ABOVE CENTER
                
                g2.drawImage(currentSprite, spriteX, spriteY, spriteSize, spriteSize, null);
                
                // RESTORE ORIGINAL COMPOSITE
                g2.setComposite(originalComposite);
                
                // GLOW EFFECT DURING EVOLUTION
                if (stage == 2 && showSprite) {
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.fillOval(spriteX - 20, spriteY - 20, spriteSize + 40, spriteSize + 40);
                }
            }
        }
        
        // DRAW TEXT AT BOTTOM
        g2.setFont(new Font("Arial", Font.BOLD, 28)); 
        
        String message = "";
        
        switch (stage) {
            case 0:
                // BLACK SCREEN FOR "WHAT?"
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                g2.setColor(Color.WHITE); 
                message = "What?";
                
                // DRAW CENTERED MESSAGE FOR "WHAT?"
                FontMetrics fm0 = g2.getFontMetrics();
                int textWidth0 = fm0.stringWidth(message);
                g2.drawString(message, centerX - textWidth0 / 2, centerY);
                return; // EARLY RETURN
                
            case 1:
                g2.setColor(Color.BLACK);
                message = oldName + " is evolving!";
                break;
                
            case 2:
                g2.setColor(Color.BLACK);
                // ANIMATED DOTS
                int dots = (frameCounter / 20) % 4;
                message = oldName + " is evolving";
                for (int i = 0; i < dots; i++) {
                    message += ".";
                }
                break;
                
            case 3:
                g2.setColor(Color.BLACK);
                // SPLIT INTO TWO LINES IF TOO LONG
                String line1 = "Congratulations!";
                String line2 = "Your " + oldName + " evolved into " + evolvingPokemon.getName() + "!";
                
                FontMetrics fm3 = g2.getFontMetrics();
                int textWidth1 = fm3.stringWidth(line1);
                int textWidth2 = fm3.stringWidth(line2);
                int textY = gp.screenHeight - 100;
                
                // CHECK IF LINE 2 IS TOO LONG (WIDER THAN SCREEN - 40PX PADDING)
                if (textWidth2 > gp.screenWidth - 40) {
                    // SPLIT INTO 3 LINES
                    String line2a = "Your " + oldName + " evolved into";
                    String line2b = evolvingPokemon.getName() + "!";
                    
                    int textWidth2a = fm3.stringWidth(line2a);
                    int textWidth2b = fm3.stringWidth(line2b);
                    
                    // DRAW TEXT SHADOWS
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawString(line1, centerX - textWidth1 / 2 + 2, textY - 60 + 2);
                    g2.drawString(line2a, centerX - textWidth2a / 2 + 2, textY - 25 + 2);
                    g2.drawString(line2b, centerX - textWidth2b / 2 + 2, textY + 10 + 2);
                    
                    // DRAW MAIN TEXT
                    g2.setColor(Color.BLACK);
                    g2.drawString(line1, centerX - textWidth1 / 2, textY - 60);
                    g2.drawString(line2a, centerX - textWidth2a / 2, textY - 25);
                    g2.drawString(line2b, centerX - textWidth2b / 2, textY + 10);
                } else {
                    // DRAW TEXT SHADOWS
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawString(line1, centerX - textWidth1 / 2 + 2, textY - 35 + 2);
                    g2.drawString(line2, centerX - textWidth2 / 2 + 2, textY + 2);
                    
                    // DRAW MAIN TEXT
                    g2.setColor(Color.BLACK);
                    g2.drawString(line1, centerX - textWidth1 / 2, textY - 35);
                    g2.drawString(line2, centerX - textWidth2 / 2, textY);
                }
                return; // EARLY RETURN
        }
        
        // DRAW CENTERED MESSAGE AT BOTTOM OF SCREEN (FOR STAGES 1-2)
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textY = gp.screenHeight - 100;
        
        // DRAW TEXT SHADOW FOR BETTER VISIBILITY
        g2.setColor(new Color(200, 200, 200));
        g2.drawString(message, centerX - textWidth / 2 + 2, textY + 2);
        
        // DRAW MAIN TEXT
        g2.setColor(Color.BLACK);
        g2.drawString(message, centerX - textWidth / 2, textY);
    }
    
    public void handleInput(String key) {
    }
}