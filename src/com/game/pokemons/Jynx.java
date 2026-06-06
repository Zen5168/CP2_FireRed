package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Jynx extends Pokemon {

    public Jynx(int level) {
        super("Jynx", Type.ICE, Type.PSYCHIC, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Powder Snow");
        moveLevelUpTable.put(7, "Confusion");

        // INITIALIZE MOVES
        int slot = 0;
        for (int i = 1; i <= level; i++) {
            if (moveLevelUpTable.containsKey(i)) {
                String moveName = moveLevelUpTable.get(i);
                Moves newMove = MoveDatabase.getMoveFromDB(moveName);

                if (newMove != null && slot < 4) {
                    this.learnMove(newMove, slot);
                    slot++;
                }
            }
        }
    }
}

