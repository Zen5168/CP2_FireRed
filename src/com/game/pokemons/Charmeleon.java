package com.game.pokemons;

import com.game.logic.*;

public class Charmeleon extends Pokemon {

    public Charmeleon(int level) {
        super("Charmeleon", Type.FIRE, Type.NONE, level, 58, 64, 78, 58, 65, 80);

        // EVOLUTION SETUP
        this.evolutionLevel = 36;
        this.evolutionName = "com.game.pokemons.Charizard";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(4, "Ember");
        moveLevelUpTable.put(12, "Dragon Breath");
        moveLevelUpTable.put(17, "Fire Fang");
        moveLevelUpTable.put(20, "Fire Spin");
        moveLevelUpTable.put(24, "Slash");
        moveLevelUpTable.put(30, "FlameThrower");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
