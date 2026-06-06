package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Bulbasaur extends Pokemon {

    public Bulbasaur(int level) {
        super("Bulbasaur", Type.GRASS, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(2, "Vine Whip"); // REMINDER: SWITCH TO LVL 7
        moveLevelUpTable.put(3, "Ember");
        moveLevelUpTable.put(4, "Bubble");
        moveLevelUpTable.put(5, "Headbutt");
        // PLACE HOLDER MOVES

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
