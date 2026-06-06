package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Wartortle extends Pokemon {

    public Wartortle(int level) {
        super("Wartortle", Type.WATER, Type.NONE, level, 59, 63, 80, 65, 80, 58);

        // EVOLUTION SETUP
        this.evolutionLevel = 36;
        this.evolutionName = "com.game.pokemons.Blastoise";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Bubble");
        moveLevelUpTable.put(13, "Water Gun");
        moveLevelUpTable.put(20, "Bite");
        moveLevelUpTable.put(27, "Water Pulse");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
