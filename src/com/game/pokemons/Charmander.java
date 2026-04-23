package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Charmander extends Pokemon {

    public Charmander(int level) {
        super("Charmander", Type.FIRE, Type.NONE, level, 39, 52, 60, 43, 50, 65);

        // INITIALIZE MOVES
        this.learnMove(com.game.moves.MoveDatabase.get("Scratch"), 0);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(7, "Ember");

    }
}
