package com.game.main;

import com.game.gui.*;
import javax.swing.*;
import com.game.pokemons.*;
import com.game.logic.*;
import java.util.*;

public class Main { 
    public static void main (String [] args) {
        
        JFrame window = new JFrame ();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pokemon Fire Red Fan Made");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        
        window.pack(); // FIT THE PREFERRED SIZE 
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.startGameThread();
    }
}
