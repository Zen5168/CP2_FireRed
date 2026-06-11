package com.game.pokemons;

import com.game.logic.*;

public class Pidgeotto extends Pokemon {

    public Pidgeotto(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Pidgeotto", Type.NORMAL, Type.FLYING, level, 63, 60, 55, 50, 50, 71);

        // EVOLUTION SETUP
        this.evolutionLevel = 36;
        this.evolutionName = "com.game.pokemons.Pidgeot";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(9, "Gust");
        moveLevelUpTable.put(13, "Quick Attack");
        moveLevelUpTable.put(17, "Twister");
        moveLevelUpTable.put(21, "Wing Attack");
        moveLevelUpTable.put(33, "Air Slash");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
