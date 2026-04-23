package com.game.pokemons;

import com.game.logic.*;
import com.game.moves.*;

public class Pidgey extends Pokemon {

    public Pidgey(int level) {
        super("Pidgey", Type.NORMAL, Type.FLYING, level, 40, 45, 40, 35, 35, 56);

        // INITIALIZE MOVES
        this.learnMove(com.game.moves.MoveDatabase.get("Tackle"), 0);
    }
}
