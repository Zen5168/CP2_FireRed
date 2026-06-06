package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Charizard extends Pokemon {

    public Charizard(int level) {
        super("Charizard", Type.FIRE, Type.FLYING, level, 78, 84, 78, 109, 85, 100);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Ember");
        moveLevelUpTable.put(13, "Flame Wheel");
        moveLevelUpTable.put(20, "Slash");
        moveLevelUpTable.put(27, "Flamethrower");
        moveLevelUpTable.put(36, "Wing Attack");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
