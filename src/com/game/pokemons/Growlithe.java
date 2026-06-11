package com.game.pokemons;

import com.game.logic.*;

public class Growlithe extends Pokemon {

    public Growlithe(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Growlithe", Type.FIRE, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Ember");
        moveLevelUpTable.put(8, "Bite");
        moveLevelUpTable.put(12, "Flame Wheel");
        moveLevelUpTable.put(24, "Fire Fang");
        moveLevelUpTable.put(28, "Retaliate");
        moveLevelUpTable.put(32, "Crunch");
        moveLevelUpTable.put(36, "Take Down");
        moveLevelUpTable.put(40, "Flamethrower");
        moveLevelUpTable.put(48, "Play Rough");
        moveLevelUpTable.put(56, "Flare Blitz");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

