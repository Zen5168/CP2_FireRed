package com.game.logic;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON;

    private static final Map<Type, Map<Type, Double>> chart = new HashMap<>();

    static {
        for (Type t1 : Type.values()) {
            chart.put(t1, new HashMap<>());
            for (Type t2 : Type.values()) chart.get(t1).put(t2, 1.0);
        }

        // (ATTACKER vs. DEFENDER)
        setEff(FIRE, GRASS, 2.0);
        setEff(FIRE, WATER, 0.5);
        setEff(WATER, FIRE, 2.0);
        setEff(GRASS, WATER, 2.0);
        setEff(NORMAL, GHOST, 0.0); // IMMUNE
        // UNFINISHED
    }

    private static void setEff(Type attacker, Type defender, double mult) {
        chart.get(attacker).put(defender, mult);
    }

    public static double getEffectiveness(Type atkType, Type defType) {
        return chart.get(atkType).get(defType);
    }
}