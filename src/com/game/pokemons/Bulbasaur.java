package com.game.pokemons;

import com.game.logic.*;

public class Bulbasaur extends Pokemon {

    public Bulbasaur(int level) {
        super("Bulbasaur", Type.GRASS, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(7, "Leech Seed");
        moveLevelUpTable.put(13, "Vine Whip");
    }
}
