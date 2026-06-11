package com.game.pokemons;

import com.game.logic.*;

public class Blastoise extends Pokemon {

    public Blastoise(int level) {
        super("Blastoise", Type.WATER, Type.NONE, level, 79, 83, 100, 85, 105, 78);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(3, "Water Whip");
        moveLevelUpTable.put(12, "Bite");
        moveLevelUpTable.put(15, "Bubble Beam");
        moveLevelUpTable.put(20, "Icy Wind");
        moveLevelUpTable.put(35, "Aqua Jet");
        moveLevelUpTable.put(45, "Liquidation");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
