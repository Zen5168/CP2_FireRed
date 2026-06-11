package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Jynx extends Pokemon {

    public Jynx(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Jynx", Type.ICE, Type.PSYCHIC, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Powder Snow");
        moveLevelUpTable.put(7, "Confusion");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

