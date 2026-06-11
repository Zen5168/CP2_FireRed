package com.game.pokemons;

import com.game.logic.*;

public class Pidgey extends Pokemon {

    public Pidgey(int level) {
        super("Pidgey", Type.NORMAL, Type.FLYING, level, 40, 45, 40, 35, 35, 56);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(9, "Gust");
        moveLevelUpTable.put(13, "Quick Attack");
        moveLevelUpTable.put(17, "Twister");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
