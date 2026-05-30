package com.game.main;

import javax.swing.*;
import java.awt.*;
import com.game.logic.*;
import com.game.entity.*;
import com.game.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    //========================================
    // SCREEN SETTINGS
    //========================================
    final int originalTileSize = 16; // 16x16 Tile
    final int scale = 3; // TILE SCALING 

    public final int tileSize = originalTileSize * scale; // MAKES THE TILE 48x48
    public final int maxScreenCol = 16; // 16 TILES HORIZONTALLY
    public final int maxScreenRow = 12; // 12 TILES VERTICALLY
    public int screenWidth = tileSize * maxScreenCol; // 768 PIXELS
    public int screenHeight = tileSize * maxScreenRow; // 576 PIXELS

    //========================================
    // WORLD SETTINGS
    //========================================
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //========================================
    // FPS
    //========================================
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public WildEncounterManager encounterManager = new WildEncounterManager();
    public com.game.world.BuildingManager buildingManager;

    // GAME STATE
    public GameState gameState = GameState.OVERWORLD;
    public BattleScreen battleScreen;
    public BattleUI battleUI;
    public com.game.pokemons.Pokemon currentWildPokemon;
    public com.game.trainers.Player playerTrainer;

    public GamePanel(String playerName, com.game.pokemons.Pokemon starterPokemon) {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // PREVENTS FLICKERING (BETTER RENDERING)
        this.addKeyListener(keyH);
        this.setFocusable(true);

        battleScreen = new BattleScreen(this);
        battleUI = new BattleUI(this);
        buildingManager = new com.game.world.BuildingManager(this);

        // INITIALIZE PLAYER TRAINER WITH CUSTOM NAME AND STARTER POKEMON
        playerTrainer = new com.game.trainers.Player(playerName, 23, 21);
        playerTrainer.addPokemon(starterPokemon);

        // ADD SOME ITEMS FOR TESTING
        playerTrainer.getBag().addItem(new com.game.items.Pokeball(), 5);
        playerTrainer.getBag().addItem(new com.game.items.Potion(), 3);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    //========================================
    // FPS LIMITER / GAME LOOP (DELTA METHOD)
    //========================================
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount); // FPS DISPLAY
                drawCount = 0;
                timer = 0;
            }
        }

    }

    public void update() {

        if (gameState == GameState.OVERWORLD) {
            player.update();
            buildingManager.update(); // UPDATE TRANSITIONS
        } else if (gameState == GameState.BATTLE) {
            battleScreen.updateTransition();

            // UPDATE HP BAR ANIMATION
            com.game.pokemons.Pokemon playerPokemon = battleUI.getPlayerPokemon();
            com.game.pokemons.Pokemon enemyPokemon = battleUI.getEnemyPokemon();
            battleScreen.updateHPAnimation(playerPokemon, enemyPokemon);

            battleUI.update();

            // HANDLE BATTLE INPUT
            if (keyH.hasKeyPress()) {
                String key = keyH.getNextKeyPress();
                battleUI.handleInput(key);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.OVERWORLD) {
            if (buildingManager.isInBuilding()) {
                // DRAW INTERIOR ROOM
                buildingManager.drawInterior(g2);
                player.draw(g2);
            } else {
                // DRAW OVERWORLD
                tileM.draw(g2); // DRAW BASE TILES (GRASS, PATHS, ETC.)
                buildingManager.draw(g2); // DRAW BUILDINGS
                player.draw(g2); // DRAW PLAYER ON TOP
            }
            
            // DRAW TRANSITION OVERLAY (ALWAYS ON TOP)
            buildingManager.drawTransition(g2);
            
        } else if (gameState == GameState.BATTLE) {
            // DRAW BATTLE SCREEN
            com.game.pokemons.Pokemon playerPokemon = battleUI.getPlayerPokemon();
            com.game.pokemons.Pokemon enemyPokemon = battleUI.getEnemyPokemon();

            battleScreen.draw(g2, playerPokemon, enemyPokemon);
            battleUI.draw(g2);
        }

        g2.dispose(); // DISPOSES GRAPHICS CONTEXT
    }
}
