package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Rattata extends Pokemon {

    public Rattata(int level) {
        super("Rattata", Type.NORMAL, Type.NONE, level, 30, 56, 35, 25, 35, 72);

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
