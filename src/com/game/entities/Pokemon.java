package com.game.entities;

import com.game.moves.Moves;

public class Pokemon {

    protected String pokeName;
    protected String type;
    protected int level;

    protected int hp, maxHp, atk, def, spAtk, spDef, speed;

    protected int baseHp, baseAtk, baseDef, baseSpAtk, baseSpDef, baseSpeed;
    protected Moves[] moves = new Moves[4];

    public Pokemon(String name, String type, int level, int bHp, int bAtk, int bDef, int bSpAtk, int bSpDef, int bSpeed) {
        this.pokeName = name;
        this.type = type;
        this.level = level;
        this.baseHp = bHp;
        this.baseAtk = bAtk;
        this.baseDef = bDef;
        this.baseSpAtk = bSpAtk;
        this.baseSpDef = bSpDef;
        this.baseSpeed = bSpeed;

        calculateStats();
    }

    //========================================
    // STAT CALCULATOR
    //========================================
    public void calculateStats() {
        this.maxHp = ((2 * baseHp) * level / 100) + level + 10;
        this.hp = maxHp;

        this.atk = ((2 * baseAtk) * level / 100) + 5;
        this.def = ((2 * baseDef) * level / 100) + 5;
        this.spAtk = ((2 * baseSpAtk) * level / 100) + 5;
        this.spDef = ((2 * baseSpDef) * level / 100) + 5;
        this.speed = ((2 * baseSpeed) * level / 100) + 5;
    }

    public int getLevel() {
        return this.level;
    }
}
