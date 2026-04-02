package com.game.moves;

import com.game.logic.Type;
import java.util.*;

public class MoveDatabase {

    private static final Map<String, Moves> moves = new HashMap<>();

    static {
        // Format: Name, Category, Type, Power, Accuracy, PP
        addMove(new Moves("Tackle", "Physical", Type.NORMAL, 40, 100, 35));
        addMove(new Moves("Ember", "Special", Type.FIRE, 40, 100, 25));
        addMove(new Moves("Bubble", "Special", Type.WATER, 40, 100, 30));
        addMove(new Moves("Vine Whip", "Physical", Type.GRASS, 45, 100, 25));
        addMove(new Moves("Thunder Shock", "Special", Type.ELECTRIC, 40, 100, 30));
    }

    private static void addMove(Moves move) {
        moves.put(move.moveName.toLowerCase(), move);
    }

    public static Moves get(String moveName) {
        Moves template = moves.get(moveName.toLowerCase());
        if (template == null) {
            System.out.println("Error: Move " + moveName + " not found!");
            return null;
        }
        // INDIVIDUAL PP TRACKING
        return new Moves(template);
    }
}
