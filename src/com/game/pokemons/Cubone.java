package com.game.pokemons;

import com.game.logic.*;

public class Cubone extends Pokemon {

    public Cubone(int level) {
        super("Cubone", Type.GROUND, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(8, "False Swipe");
        moveLevelUpTable.put(12, "Headbutt");
        moveLevelUpTable.put(24, "Bulldoze");
        
        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

