package com.game.logic;

import com.game.pokemons.Pokemon;
import com.game.moves.Moves;
import java.util.Random;

public class BattleEngine {

    private static final Random rand = new Random();

    // ======================================
    // EXECUTE TURN
    // ======================================
    public void executeTurn(Pokemon attacker, Pokemon defender, Moves move) {
        System.out.println(attacker.getName() + " used " + move.moveName + "!");

        if (!move.useMove()) {
            System.out.println("But it missed!");
            return;
        }

        // ======================================
        // CRITICAL HIT LOGIC
        // ======================================
        double critMult = (rand.nextInt(16) == 0) ? 2.0 : 1.0;
        if (critMult > 1.0) {
            System.out.println("A critical hit!");
        }

        // ======================================
        // STAB (SAME TYPE ATTACK BONUS)
        // ======================================
        double stab = 1.0;
        if (move.getTypeEnum() == attacker.getType1() || move.getTypeEnum() == attacker.getType2()) {
            stab = 1.5;
        }

        // ======================================
        // TYPE EFFECTIVENESS 
        // ======================================
        double typeEff = Type.getEffectiveness(move.getTypeEnum(), defender.getType1(), defender.getType2());

        if (typeEff > 1.0) {
            System.out.println("It's super effective!");
        } else if (typeEff < 1.0 && typeEff > 0) {
            System.out.println("It's not very effective...");
        } else if (typeEff == 0) {
            System.out.println("It had no effect...");
        }

        // ======================================
        // RANDOM VARIANCE
        // ======================================
        double random = 0.85 + (1.0 - 0.85) * rand.nextDouble();

        // ======================================
        // FINAL DAMAGE CALCULATION 
        // ======================================
        int damage = calculateDamage(attacker, defender, move, critMult, stab, typeEff, random);

        defender.takeDamage(damage);
    }

    private int calculateDamage(Pokemon atk, Pokemon def, Moves move, double crit, double stab, double type, double randVar) {
        // ======================================
        // PHYSICAL OR SPECIAL?
        // ======================================
        int effectiveAtk = move.isSpecial() ? atk.getSpAtk() : atk.getAtk();
        int effectiveDef = move.isSpecial() ? def.getSpDef() : def.getDef();

        // ======================================
        // BASE DAMAGE FORMULA
        // ======================================
        double base = (((2.0 * atk.getLevel() / 5.0) + 2.0) * move.power * ((double) effectiveAtk / effectiveDef) / 50.0) + 2.0;

        return (int) (base * crit * stab * type * randVar);
    }
}