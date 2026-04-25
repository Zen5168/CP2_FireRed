package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Squirtle extends Pokemon {

    public Squirtle(int level) {
        super("Squirtle", Type.WATER, Type.NONE, level, 44, 48, 50, 65, 64, 43);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Bubble");

        // INITIALIZE MOVES
        for (int i = 1; i <= level; i++) {
            if (moveLevelUpTable.containsKey(i)) {
                String moveName = moveLevelUpTable.get(i);
                Moves newMove = MoveDatabase.getMoveFromDB(moveName);

                if (newMove != null) {
                    // LEARN THE MOVE OBJECT CREATED FROM THE DB ROW
                    this.learnMove(newMove, i);
                }
            }
        }
    }
}
