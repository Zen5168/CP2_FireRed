package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Charmeleon extends Pokemon {

    public Charmeleon(int level) {
        super("Charmeleon", Type.FIRE, Type.NONE, level, 58, 64, 78, 58, 65, 80);

        // EVOLUTION SETUP
        this.evolutionLevel = 36;
        this.evolutionName = "com.game.pokemons.Charizard";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Ember");
        moveLevelUpTable.put(13, "Flame Wheel");
        moveLevelUpTable.put(20, "Slash");
        moveLevelUpTable.put(27, "Flamethrower");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
