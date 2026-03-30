package com.game.main;
import com.game.logic.*;
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
        Pokemon Bulbasaur = new Charmander(5);
        Pokemon Charmander = new Bulbasaur(5);
        Pokemon Squirtle = new Squirtle(5);
    }
}
