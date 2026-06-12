package com.game.main;

import javax.swing.*;
import com.game.pokemons.Pokemon;
import com.game.logic.StarterSelectionScreen;
import com.game.ui.MainMenu;

public class Main { 
    public static void main (String [] args) {
        
        JFrame window = new JFrame ();
        window.setIconImage(new ImageIcon(GamePanel.class.getResource("/res//image/icon.png")).getImage());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pokemon Java");
        
        // SHOW MAIN MENU
        MainMenu mainMenu = new MainMenu(window);
        mainMenu.showMenu();
        
        // CHECK IF USER CANCELLED
        if (mainMenu.isCancelled()) {
            System.out.println("Game cancelled by user.");
            System.exit(0);
        }
        
        GamePanel gamePanel = null;
        
        if (mainMenu.isNewGameSelected()) {
            // NEW GAME - SHOW STARTER SELECTION SCREEN
            StarterSelectionScreen starterScreen = new StarterSelectionScreen(window);
            starterScreen.showDialog();
            
            // CHECK IF SELECTION WAS COMPLETED (NOT CANCELLED)
            if (!starterScreen.isSelectionComplete()) {
                System.out.println("Game cancelled by user.");
                System.exit(0);
            }
            
            // GET PLAYER NAME AND STARTER POKEMON
            String playerName = starterScreen.getPlayerName();
            Pokemon starterPokemon = starterScreen.getSelectedStarter();
            
            // CREATE GAME PANEL WITH PLAYER DATA
            gamePanel = new GamePanel(playerName, starterPokemon);
            
        } else if (mainMenu.isContinueSelected()) {
            // CONTINUE - CREATE GAME PANEL WITH DEFAULT DATA, THEN LOAD
            // USE TEMPORARY DATA THAT WILL BE OVERWRITTEN BY LOAD
            gamePanel = new GamePanel("Player", new com.game.pokemons.Bulbasaur(5));
            
            // LOAD SAVED GAME
            boolean loadSuccess = gamePanel.saveManager.loadGame();
            
            if (!loadSuccess) {
                JOptionPane.showMessageDialog(window, 
                    "Failed to load save file!", 
                    "Load Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            
            System.out.println("Game loaded successfully!");
        }
        
        // ADD GAME PANEL TO WINDOW
        window.add(gamePanel);
        
        window.pack(); // FIT THE PREFERRED SIZE 
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.startGameThread();
    }
}
