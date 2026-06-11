package com.game.pokemons;

import com.game.logic.*;

public class Marowak extends Pokemon {

    public Marowak(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Marowak", Type.GROUND, Type.NONE, level, 60, 80, 110, 50, 80, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(8, "False Swipe");
        moveLevelUpTable.put(12, "Headbutt");
        moveLevelUpTable.put(24, "Bulldoze");
        moveLevelUpTable.put(30, "Bonemerang");
        moveLevelUpTable.put(32, "Double-Edge");

        
        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

