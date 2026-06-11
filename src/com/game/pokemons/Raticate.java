package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Rattata extends Pokemon {

    public Rattata(int level) {
        super("Rattata", Type.NORMAL, Type.NONE, level, 30, 56, 35, 25, 35, 72);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(4, "Quick Attack");
        moveLevelUpTable.put(10, "Bite");
        moveLevelUpTable.put(16, "Take Down");
        moveLevelUpTable.put(19, "Assurance");
        moveLevelUpTable.put(24, "Crunch");
        moveLevelUpTable.put(29, "Sucker Punch");
        moveLevelUpTable.put(39, "Double-Edge");
        
        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}