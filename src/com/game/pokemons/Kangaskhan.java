package com.game.pokemons;

import com.game.logic.*;

public class Kangaskhan extends Pokemon {

    public Kangaskhan(int level) {
        super("Kangaskhan", Type.NORMAL, Type.NORMAL, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(12, "Bite");
        moveLevelUpTable.put(24, "Headbutt");
        moveLevelUpTable.put(32, "Take Down");
        moveLevelUpTable.put(36, "Crunch");
        moveLevelUpTable.put(48, "Outrage");
        moveLevelUpTable.put(54, "Dynamic Punch");
        

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

