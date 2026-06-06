package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Growlithe extends Pokemon {

    public Growlithe(int level) {
        super("Growlithe", Type.FIRE, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Ember");
        moveLevelUpTable.put(7, "Flame Wheel");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

