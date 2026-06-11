package com.game.pokemons;

import com.game.logic.*;

public class Bulbasaur extends Pokemon {

    public Bulbasaur(int level) {
        super("Bulbasaur", Type.GRASS, Type.NONE, level, 45, 49, 49, 65, 65, 45);

        // EVOLUTION SETUP
        this.evolutionLevel = 16;
        this.evolutionName = "com.game.pokemons.Ivysaur";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(3, "Vine Whip");
        moveLevelUpTable.put(12, "Razor Leaf");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
