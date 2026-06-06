package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Electabuzz extends Pokemon {

    public Electabuzz(int level) {
        super("Electabuzz", Type.ELECTRIC, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Thunder Shock");
        moveLevelUpTable.put(7, "Thunderbolt");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

