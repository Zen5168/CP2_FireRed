package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Blastoise extends Pokemon {

    public Blastoise(int level) {
        super("Blastoise", Type.WATER, Type.NONE, level, 79, 83, 100, 85, 105, 78);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Bubble");
        moveLevelUpTable.put(13, "Water Gun");
        moveLevelUpTable.put(20, "Bite");
        moveLevelUpTable.put(27, "Water Pulse");
        moveLevelUpTable.put(36, "Hydro Pump");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
