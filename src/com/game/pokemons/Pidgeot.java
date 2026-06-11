package com.game.pokemons;

import com.game.logic.*;

public class Pidgeot extends Pokemon {

    public Pidgeot(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Pidgeot", Type.NORMAL, Type.FLYING, level, 83, 80, 75, 70, 70, 101);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(9, "Gust");
        moveLevelUpTable.put(13, "Quick Attack");
        moveLevelUpTable.put(17, "Twister");
        moveLevelUpTable.put(21, "Wing Attack");
        moveLevelUpTable.put(33, "Air Slash");
        moveLevelUpTable.put(45, "Hurricane");
        moveLevelUpTable.put(50, "Brave Bird");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
