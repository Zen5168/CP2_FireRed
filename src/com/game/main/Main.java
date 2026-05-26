package com.game.main;

import javax.swing.*;
import com.game.pokemons.Pokemon;
import com.game.logic.StarterSelectionScreen;

public class Main { 
    public static void main (String [] args) {
        
        JFrame window = new JFrame ();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pokemon Java");
        
        // SHOW STARTER SELECTION SCREEN
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
        GamePanel gamePanel = new GamePanel(playerName, starterPokemon);
        window.add(gamePanel);
        
        window.pack(); // FIT THE PREFERRED SIZE 
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.startGameThread();
    }
}
