package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Scyther extends Pokemon {

    public Scyther(int level) {
        super("Scyther", Type.BUG, Type.FLYING, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Quick Attack");
        moveLevelUpTable.put(7, "Wing Attack");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

