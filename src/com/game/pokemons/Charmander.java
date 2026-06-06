package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Charmander extends Pokemon {

    public Charmander(int level) {
        super("Charmander", Type.FIRE, Type.NONE, level, 39, 52, 60, 43, 50, 65);

        // EVOLUTION SETUP
        this.evolutionLevel = 16;
        this.evolutionName = "com.game.pokemons.Charmeleon";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Ember");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
