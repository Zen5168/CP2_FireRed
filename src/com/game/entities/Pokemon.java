package com.game.entities;
import com.game.logic.Type;
import com.game.moves.Moves;
import java.util.Arrays;

public abstract class Pokemon {

    //================================
    // POKEMON IDENTITY
    //================================
    protected String pokeName;
    protected Type type;
    protected int level;

    //================================
    // CALCULATED STATS
    //================================
    protected int hp, maxHp, atk, def, spAtk, spDef, speed;

    //================================
    // BASE STATS
    //================================
    protected final int baseHp, baseAtk, baseDef, baseSpAtk, baseSpDef, baseSpeed;

    //================================
    // MOVE MANAGEMENT (4-SLOT LIMIT)
    //================================
    protected Moves[] moves = new Moves[4];

    public Pokemon(String name, Type type, int level, int bHp, int bAtk, int bDef, int bSpAtk, int bSpDef, int bSpeed) {
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
    public void calculateStats() {
        // HP Formula
        this.maxHp = ((2 * baseHp) * level / 100) + level + 10;
        this.hp = maxHp;

        // Other Stats Formula
        this.atk = ((2 * baseAtk) * level / 100) + 5;
        this.def = ((2 * baseDef) * level / 100) + 5;
        this.spAtk = ((2 * baseSpAtk) * level / 100) + 5;
        this.spDef = ((2 * baseSpDef) * level / 100) + 5;
        this.speed = ((2 * baseSpeed) * level / 100) + 5;
    }

    //=====================================
    // BASIC DAMAGE LOGIC
    //=====================================
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(pokeName + " took " + damage + " damage! (HP: " + hp + "/" + maxHp + ")");
    }

    public boolean isFainted() {
        return this.hp <= 0;
    }

    public void learnMove(Moves newMove, int slot) {
        if (slot >= 0 && slot < 4) {
            this.moves[slot] = newMove;
        }
    }

    //=====================================
    // GETTERS
    //=====================================
    public String getName() {
        return this.pokeName;
    }

    public Type getTypeEnum() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAtk() {
        return this.atk;
    }

    public int getDef() {
        return this.def;
    }

    public int getSpAtk() {
        return this.spAtk;
    }

    public int getSpDef() {
        return this.spDef;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Moves[] getMoves() {
        return this.moves;
    }
}
