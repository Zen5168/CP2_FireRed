package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Psyduck extends Pokemon {

    public Psyduck(int level) {
        super("Psyduck", Type.WATER, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Water Gun");
        moveLevelUpTable.put(7, "Water Pulse");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

