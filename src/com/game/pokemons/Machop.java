package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Machop extends Pokemon {

    public Machop(int level) {
        super("Machop", Type.FIGHTING, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Rock Smash");
        moveLevelUpTable.put(7, "Power-Up Punch");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

