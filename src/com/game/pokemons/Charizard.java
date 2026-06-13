package com.game.pokemons;

import com.game.logic.*;

public class Charizard extends Pokemon {

    public Charizard(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Charizard", Type.FIRE, Type.FLYING, level, 78, 84, 78, 109, 85, 100);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(4, "Ember");
        moveLevelUpTable.put(12, "Dragon Breath");
        moveLevelUpTable.put(17, "Fire Fang");
        moveLevelUpTable.put(20, "Fire Spin");
        moveLevelUpTable.put(24, "Slash");
        moveLevelUpTable.put(30, "Flame  Thrower");
        moveLevelUpTable.put(42, "Dragon Rush");
        moveLevelUpTable.put(54, "Flare Blitz");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
