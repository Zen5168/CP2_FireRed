package com.game.pokemons;

import com.game.logic.*;

public class Wartortle extends Pokemon {

    public Wartortle(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Wartortle", Type.WATER, Type.NONE, level, 59, 63, 80, 65, 80, 58);

        // EVOLUTION SETUP
        this.evolutionLevel = 36;
        this.evolutionName = "com.game.pokemons.Blastoise";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(3, "Water Gun");
        moveLevelUpTable.put(12, "Bite");
        moveLevelUpTable.put(15, "Bubble Beam");
        moveLevelUpTable.put(20, "Icy Wind");
        moveLevelUpTable.put(35, "Aqua Jet");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
