package com.game.pokemons;

import com.game.logic.*;

public class Machoke extends Pokemon {

    public Machoke(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Machoke", Type.FIGHTING, Type.NONE, level, 80, 100, 70, 50, 60, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Rock Smash");
        moveLevelUpTable.put(12, "Power-Up Punch");
        moveLevelUpTable.put(14, "Brutal Swing");
        moveLevelUpTable.put(16, "Bullet Punch");
        moveLevelUpTable.put(18, "Knock Off");
        moveLevelUpTable.put(25, "Brick Break");
        moveLevelUpTable.put(30, "Bulldoze");
        moveLevelUpTable.put(44, "Double-edge");
        moveLevelUpTable.put(54, "Dynamic Punch");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
