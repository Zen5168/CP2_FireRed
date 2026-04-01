package com.game.moves;

import com.game.logic.*;
import java.util.*;

public class Moves {

    private static final Random rand = new Random();
    public String moveName;
    public Type moveType;
    public String moveCategory; // PHYSICAL OR SPECIAL
    public int power;
    public int accuracy;
    public int pp;
    public int maxPp;

    //====================================
    // CONSTRUCTOR
    //====================================
    public Moves(String moveName, String moveCategory, Type moveType, int power, int accuracy, int pp) {
        this.moveName = moveName;
        this.moveCategory = moveCategory;
        this.moveType = moveType;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.maxPp = pp;
    }

    //====================================
    // PP TRACKING CONSTRUCTOR
    //====================================
    public Moves(Moves other) {
        this.moveName = other.moveName;
        this.moveCategory = other.moveCategory;
        this.moveType = other.moveType;
        this.power = other.power;
        this.accuracy = other.accuracy;
        this.pp = other.pp;
        this.maxPp = other.maxPp;
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

    //====================================
    // SPECIAL OR PHYSICAL CHECKER
    //====================================
    public boolean isSpecial() {
        return "Special".equalsIgnoreCase(this.moveCategory);
    }

    //====================================
    // GETTERS
    //====================================
    public Type getTypeEnum() {
        return this.moveType;
    }
     public String getName() {
         return this.moveName;
     }
}