package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Ivysaur extends Pokemon {

    public Ivysaur(int level) {
        super("Ivysaur", Type.GRASS, Type.POISON, level, 60, 62, 63, 80, 80, 60);

        // EVOLUTION SETUP
        this.evolutionLevel = 32;
        this.evolutionName = "com.game.pokemons.Venusaur";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Vine Whip");
        moveLevelUpTable.put(13, "Razor Leaf");
        moveLevelUpTable.put(20, "Headbutt");
        moveLevelUpTable.put(27, "Body Slam");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
