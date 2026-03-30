package com.game.moves;
import java.util.*;

public class Moves {

    private static final Random rand = new Random();
    public String moveName;
    String moveCategory;
    String moveType;
    public int power;
    int accuracy;
    public int pp;

    public Moves(String moveName, String moveCategory, String moveType, int power, int accuracy, int pp) {
        this.moveName = moveName;
        this.moveCategory = moveCategory;
        this.moveType = moveType;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
    }

    public boolean useMove() {
        if (this.pp <= 0) {
            return false;
        }

        reducePP();
        return willHit();
    }

    private void reducePP() {
        this.pp--;
    }

    private boolean willHit() {
        return rand.nextInt(100) + 1 <= this.accuracy;
    }
}
