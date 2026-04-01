package com.game.pokemons;

import com.game.logic.Type;
import com.game.moves.Moves;
import java.util.Arrays;

public abstract class Pokemon {

    //================================
    // POKEMON IDENTITY
    //================================
    protected String pokeName;
    protected Type type1, type2;
    protected int level;
    protected int exp = 0;
    protected int nextLevelExp = 100;

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

    public Pokemon(String name, Type type1, Type type2, int level, int bHp, int bAtk, int bDef, int bSpAtk, int bSpDef, int bSpeed) {
        this.pokeName = name;
        this.type1 = type1;
        this.type2 = type2;
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
    // EXP LOGIC
    //=====================================
    public void gainExp(int amount) {
        this.exp += amount;
        System.out.println(this.pokeName + " gained " + amount + " EXP!");

        while (this.exp >= this.nextLevelExp) {
            levelUp();
        }
    }

    //=====================================
    // LEVEL UP LOGIC
    //=====================================
    private void levelUp() {
        this.level++;
        this.exp -= this.nextLevelExp;
        this.nextLevelExp = (int) (this.nextLevelExp * 1.2); // INCREMENTING EXP REQUIREMENTS

        // RECALCULATE STATS BASED ON NEW LEVEL
        calculateStats();
        this.hp = this.maxHp;
        System.out.println("Congratulations! " + pokeName + " grew to Level " + level + "!");
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
    // GETTERS & SETTERS
    //=====================================
    public String getName() {
        return this.pokeName;
    }

    public Type getType1() {
        return this.type1;
    }

    public Type getType2() {
        return this.type2;
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

    public int getMaxHp() {
        return this.maxHp;
    }

    public void setHp(int newHp) {
        if (newHp > this.maxHp) {
            this.hp = this.maxHp;
        } else if (newHp < 0) {
            this.hp = 0;
        } else {
            this.hp = newHp;
        }
    }
}
