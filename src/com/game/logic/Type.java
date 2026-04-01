package com.game.logic;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON, NONE;

    private static final Map<Type, Map<Type, Double>> chart = new HashMap<>();

    static {
        for (Type t1 : Type.values()) {
            chart.put(t1, new HashMap<>());
            for (Type t2 : Type.values()) {
                chart.get(t1).put(t2, 1.0);
            }
        }

        // NORMAL
        setEff(NORMAL, ROCK, 0.5);
        setEff(NORMAL, GHOST, 0.0);

        // FIRE
        setEff(FIRE, FIRE, 0.5);
        setEff(FIRE, WATER, 0.5);
        setEff(FIRE, GRASS, 2.0);
        setEff(FIRE, ICE, 2.0);
        setEff(FIRE, BUG, 2.0);
        setEff(FIRE, ROCK, 0.5);
        setEff(FIRE, DRAGON, 0.5);

        // WATER
        setEff(WATER, FIRE, 2.0);
        setEff(WATER, WATER, 0.5);
        setEff(WATER, GRASS, 0.5);
        setEff(WATER, GROUND, 2.0);
        setEff(WATER, ROCK, 2.0);
        setEff(WATER, DRAGON, 0.5);

        // GRASS
        setEff(GRASS, FIRE, 0.5);
        setEff(GRASS, WATER, 2.0);
        setEff(GRASS, GRASS, 0.5);
        setEff(GRASS, POISON, 0.5);
        setEff(GRASS, GROUND, 2.0);
        setEff(GRASS, FLYING, 0.5);
        setEff(GRASS, BUG, 0.5);
        setEff(GRASS, ROCK, 2.0);
        setEff(GRASS, DRAGON, 0.5);

        // ELECTRIC
        setEff(ELECTRIC, WATER, 2.0);
        setEff(ELECTRIC, GRASS, 0.5);
        setEff(ELECTRIC, ELECTRIC, 0.5);
        setEff(ELECTRIC, GROUND, 0.0);
        setEff(ELECTRIC, FLYING, 2.0);
        setEff(ELECTRIC, DRAGON, 0.5);

        // ICE
        setEff(ICE, FIRE, 0.5);
        setEff(ICE, WATER, 0.5);
        setEff(ICE, GRASS, 2.0);
        setEff(ICE, ICE, 0.5);
        setEff(ICE, GROUND, 2.0);
        setEff(ICE, FLYING, 2.0);
        setEff(ICE, DRAGON, 2.0);

        // FIGHTING
        setEff(FIGHTING, NORMAL, 2.0);
        setEff(FIGHTING, ICE, 2.0);
        setEff(FIGHTING, POISON, 0.5);
        setEff(FIGHTING, FLYING, 0.5);
        setEff(FIGHTING, PSYCHIC, 0.5);
        setEff(FIGHTING, BUG, 0.5);
        setEff(FIGHTING, ROCK, 2.0);
        setEff(FIGHTING, GHOST, 0.0);

        // POISON
        setEff(POISON, GRASS, 2.0);
        setEff(POISON, POISON, 0.5);
        setEff(POISON, GROUND, 0.5);
        setEff(POISON, ROCK, 0.5);
        setEff(POISON, GHOST, 0.5);

        // GROUND
        setEff(GROUND, FIRE, 2.0);
        setEff(GROUND, ELECTRIC, 2.0);
        setEff(GROUND, GRASS, 0.5);
        setEff(GROUND, POISON, 2.0);
        setEff(GROUND, FLYING, 0.0);
        setEff(GROUND, BUG, 0.5);
        setEff(GROUND, ROCK, 2.0);

        // FLYING
        setEff(FLYING, ELECTRIC, 0.5);
        setEff(FLYING, GRASS, 2.0);
        setEff(FLYING, FIGHTING, 2.0);
        setEff(FLYING, BUG, 2.0);
        setEff(FLYING, ROCK, 0.5);

        // PSYCHIC
        setEff(PSYCHIC, FIGHTING, 2.0);
        setEff(PSYCHIC, POISON, 2.0);
        setEff(PSYCHIC, PSYCHIC, 0.5);

        // BUG
        setEff(BUG, FIRE, 0.5);
        setEff(BUG, GRASS, 2.0);
        setEff(BUG, FIGHTING, 0.5);
        setEff(BUG, POISON, 0.5);
        setEff(BUG, FLYING, 0.5);
        setEff(BUG, PSYCHIC, 2.0);
        setEff(BUG, GHOST, 0.5);

        // ROCK
        setEff(ROCK, FIRE, 2.0);
        setEff(ROCK, ICE, 2.0);
        setEff(ROCK, FIGHTING, 0.5);
        setEff(ROCK, GROUND, 0.5);
        setEff(ROCK, FLYING, 2.0);
        setEff(ROCK, BUG, 2.0);

        // GHOST
        setEff(GHOST, NORMAL, 0.0);
        setEff(GHOST, PSYCHIC, 2.0);
        setEff(GHOST, GHOST, 2.0);

        // DRAGON
        setEff(DRAGON, DRAGON, 2.0);
    }

    //=====================================
    // EFFECTIVENESS SETTER
    //=====================================
    private static void setEff(Type attacker, Type defender, double mult) {
        chart.get(attacker).put(defender, mult);
    }

    //=====================================
    // TYPE EFFECTIVENESS GETTER
    //=====================================
    public static double getEffectiveness(Type atkType, Type defType1, Type defType2) {
        double eff1 = chart.get(atkType).get(defType1);
        double eff2 = 1.0;

        if (defType2 != null && defType2 != Type.NONE) {
            eff2 = chart.get(atkType).get(defType2);
        }

        return eff1 * eff2;
    }
}
