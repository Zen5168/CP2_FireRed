package com.game.pokemons;

import com.game.logic.*;

public class Rattata extends Pokemon {

    public Rattata(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Rattata", Type.NORMAL, Type.NONE, level, 30, 56, 35, 25, 35, 72);
        
        // EVOLUTION SETUP
        this.evolutionLevel = 20;
        this.evolutionName = "com.game.pokemons.Raticate";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(4, "Quick Attack");
        moveLevelUpTable.put(10, "Bite");
        moveLevelUpTable.put(16, "Take Down");
        moveLevelUpTable.put(19, "Assurance");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
