package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Venusaur extends Pokemon {

    public Venusaur(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Venusaur", Type.GRASS, Type.POISON, level, 80, 82, 83, 100, 100, 80);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Vine Whip");
        moveLevelUpTable.put(13, "Razor Leaf");
        moveLevelUpTable.put(20, "Headbutt");
        moveLevelUpTable.put(27, "Body Slam");
        moveLevelUpTable.put(32, "Solar Beam");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
