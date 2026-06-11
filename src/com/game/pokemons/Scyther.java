package com.game.pokemons;

import com.game.logic.*;

public class Scyther extends Pokemon {

    public Scyther(int level) {
        super("Scyther", Type.BUG, Type.FLYING, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Quick Attack");
        moveLevelUpTable.put(4, "Fury Cutter");
        moveLevelUpTable.put(8, "False Swipe");
        moveLevelUpTable.put(12, "Wing Attack");
        moveLevelUpTable.put(20, "Double Hit");
        moveLevelUpTable.put(24, "Slash");
        moveLevelUpTable.put(36, "Air Slash");
        moveLevelUpTable.put(40, "X-Scissor");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

