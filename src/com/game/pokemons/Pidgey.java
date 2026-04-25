package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Pidgey extends Pokemon {

    public Pidgey(int level) {
        super("Pidgey", Type.NORMAL, Type.FLYING, level, 40, 45, 40, 35, 35, 56);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(7, "Ember");

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
