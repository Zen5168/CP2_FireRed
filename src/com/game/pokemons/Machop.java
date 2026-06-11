package com.game.pokemons;

import com.game.logic.*;

public class Machop extends Pokemon {

    public Machop(int level) {
        super("Machop", Type.FIGHTING, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Rock Smash");
        moveLevelUpTable.put(12, "Power-Up Punch");
        moveLevelUpTable.put(14, "Brutal Swing");
        moveLevelUpTable.put(16, "Bullet Punch");
        moveLevelUpTable.put(18, "Knock Off");
        moveLevelUpTable.put(25, "Brick Break");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

