package com.game.entities;

public class Charmander extends Pokemon {

    public Charmander(String name, String type, int level, int hp, int atk, int spAtk, int def, int spDef, int speed) {
        super(name, type, level, hp, atk, spAtk, def, spDef, speed);

    }

    @Override
    public String toString() {
        return this.pokeName + " " + this.type + " Lvl:" + this.level
                + " HP:" + this.hp + " Atk:" + this.atk + " SpAtk:" + this.spAtk
                + " Def:" + this.def + " SpDef:" + this.spDef + " Spd:" + this.speed;
    }
}
