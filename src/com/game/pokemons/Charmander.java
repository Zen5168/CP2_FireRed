package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Charmander extends Pokemon {

    public Charmander(int level) {
        super("Charmander", Type.FIRE, Type.NONE, level, 39, 52, 60, 43, 50, 65);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Ember");

        // INITIALIZE MOVES
        for (int i = 1; i <= level; i++) {
            if (moveLevelUpTable.containsKey(i)) {
                String moveName = moveLevelUpTable.get(i);
                Moves newMove = MoveDatabase.getMoveFromDB(moveName);

                if (newMove != null) {
                    // LEARN THE MOVE CREATED FROM THE DATABASE ROW
                    this.learnMove(newMove, i);
                }
            }
        }
    }
}
