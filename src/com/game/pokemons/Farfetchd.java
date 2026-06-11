package com.game.pokemons;

import com.game.logic.*;

public class Farfetchd extends Pokemon {

    public Farfetchd(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Farfetch'd", Type.NORMAL, Type.FLYING, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Peck");
        moveLevelUpTable.put(10, "Gust");
        moveLevelUpTable.put(12, "Steel Wing");
        moveLevelUpTable.put(16, "Aerial Ace");
        moveLevelUpTable.put(20, "Knock Off");
        moveLevelUpTable.put(22, "Quick Attack");
        moveLevelUpTable.put(24, "Brutal Swing");
        moveLevelUpTable.put(25, "False Swipe");
        moveLevelUpTable.put(30, "Slash");
        moveLevelUpTable.put(40, "Air Slash");
        moveLevelUpTable.put(42, "Leaf Blade");
        moveLevelUpTable.put(55, "Brave Bird");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

