package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Rattata extends Pokemon {

    public Rattata(int level) {
        super("Rattata", Type.NORMAL, Type.NONE, level, 30, 56, 35, 25, 35, 72);

        // INITIALIZE MOVES
        this.learnMove(com.game.moves.MoveDatabase.get("Tackle"), 0);
    }
}
