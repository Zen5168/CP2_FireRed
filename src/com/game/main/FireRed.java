package com.game.main;
import com.game.pokemons.*;
import com.game.logic.*;
import java.util.*;

public class FireRed {
    
    static Scanner sc = new Scanner(System.in);
    
    //=========================
    // GBA STYE (SOON)
    //=========================
//    public static void gbaPrint(String text) {
//        for (int i = 0; i < text.length(); i++) {
//            System.out.print(text.charAt(i));
//            try {
//
//                Thread.sleep(0);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        sc.nextLine();
//    }

    public static void main(String[] args) {

        Random rand = new Random();
        int randomLvl = rand.nextInt(20) + 1;

        //=========================
        // STARTER POKEMONS
        //=========================
        
        OverworldManager game = new OverworldManager();
    game.start();
    }
}
