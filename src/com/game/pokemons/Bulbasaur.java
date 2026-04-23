package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Bulbasaur extends Pokemon {

    public Bulbasaur(int level) {
        super("Bulbasaur", Type.GRASS, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // INITIALIZE MOVES
        this.learnMove(com.game.moves.MoveDatabase.get("Tackle"), 0);
        
        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(7, "Vine Whip");
    }
}
