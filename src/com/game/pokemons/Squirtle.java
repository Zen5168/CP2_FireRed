package com.game.pokemons;

import com.game.logic.*;

public class Squirtle extends Pokemon {

    public Squirtle(int level) {
        super("Squirtle", Type.WATER, Type.NONE, level, 44, 48, 50, 65, 64, 43);

        // INITIALIZE MOVES
        this.learnMove(com.game.moves.MoveDatabase.get("Tackle"), 0);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(7, "Bubble");
    }
}
