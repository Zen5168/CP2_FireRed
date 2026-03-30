package com.game.main;

import com.game.entities.*;
import java.util.*;

public class FireRed {
    
    static Scanner sc = new Scanner(System.in);
    
    //=========================
    // GBA STYE
    //=========================
    public static void gbaPrint(String text) {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            try {

                Thread.sleep(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        sc.nextLine();
    }

    public static void main(String[] args) {

        Random rand = new Random();
        int randomLvl = rand.nextInt(20) + 1;

        //=========================
        // STARTER POKEMONS
        //=========================
        Pokemon Bulbasaur = new Pokemon("Bulbasaur", "Grass", 5, 45, 49, 65, 49, 65, 45);
        Pokemon Charmander = new Pokemon("Charmander", "Fire", 5, 39, 52, 60, 43, 50, 65);
        Pokemon Squirtle = new Pokemon("Squirtle", "Water", 5, 44, 48, 50, 65, 64, 43);
    }
}
