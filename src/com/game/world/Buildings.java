package com.game.world;

import com.game.trainers.*;

public class Buildings {
    
    public enum BuildingType {
        POKEMON_CENTER,
        POKEMART
    }
    
    public static void healPokemon(Player player) {
        System.out.println("\n========================================");
        System.out.println("   Welcome to the Pokemon Center!");
        System.out.println("   We'll heal your Pokemon!");
        System.out.println("========================================");
        
        //=============================
        // HEAL POKEMON
        //=============================
        for (com.game.pokemons.Pokemon pokemon : player.getParty()) {
            pokemon.heal(pokemon.getMaxHp());
            System.out.println(pokemon.getName() + " has been fully healed!");
        }
        
        System.out.println("\nYour Pokemon are now fully healed!");
        System.out.println("We hope to see you again!\n");
    }
    
    public static void openShop() {
        System.out.println("\n========================================");
        System.out.println("   Welcome to the PokeMart!");
        System.out.println("   Shop functionality coming soon!");
        System.out.println("========================================\n");
    }
}