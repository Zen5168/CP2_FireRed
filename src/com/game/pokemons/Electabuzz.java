package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Electabuzz extends Pokemon {

    public Electabuzz(int level) {
        super("Electabuzz", Type.ELECTRIC, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Thunder Shock");
        moveLevelUpTable.put(7, "Thunderbolt");

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

