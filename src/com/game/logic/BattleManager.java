package com.game.logic;

import com.game.pokemons.Pokemon;
import com.game.moves.Moves;
import java.util.*;

public class BattleManager {

    private Scanner scanner = new Scanner(System.in);
    private BattleEngine engine = new BattleEngine();

    public void startBattle(Pokemon player, Pokemon wild) {
        System.out.println("A wild " + wild.getName() + " appeared!");

        while (!player.isFainted() && !wild.isFainted()) {
            printBattleStatus(player, wild);

            Moves playerMove = selectMove(player);

            Moves wildMove = getBestMove(wild, player);

            //=====================================
            // SPEED CHECKER
            //=====================================
            if (player.getSpeed() >= wild.getSpeed()) {
                executeFullTurn(player, wild, playerMove, wildMove);
            } else {
                executeFullTurn(wild, player, wildMove, playerMove);
            }
        }

        //=====================================
        // POKEMON STATUS
        //=====================================
        if (player.isFainted()) {
            System.out.println("Your Pokemon fainted!");
        } else {
            System.out.println("The wild " + wild.getName() + " fainted!");
        }
    }

    //=====================================
    // HARDER OPPONENT
    //=====================================
    private Moves getBestMove(Pokemon attacker, Pokemon defender) {
        Moves bestMove = null;
        double maxDamage = -1;

        for (Moves move : attacker.getMoves()) {
            if (move == null || move.pp <= 0) {
                continue;
            }

            double typeEff = Type.getEffectiveness(move.getTypeEnum(), defender.getType1(), defender.getType2());
            double potentialPower = move.power * typeEff;

            if (potentialPower > maxDamage) {
                maxDamage = potentialPower;
                bestMove = move;
            }
        }
        return (bestMove != null) ? bestMove : attacker.getMoves()[0];
    }

    //=====================================
    // EXECUTE A TURN
    //=====================================
    private void executeFullTurn(Pokemon first, Pokemon second, Moves firstMove, Moves secondMove) {
        engine.executeTurn(first, second, firstMove);

        if (!second.isFainted()) {
            engine.executeTurn(second, first, secondMove);
        }
    }

    //=====================================
    // MOVE SELECTOR
    //=====================================
    private Moves selectMove(Pokemon p) {
        System.out.println("What will " + p.getName() + " do?");
        Moves[] availableMoves = p.getMoves();

        for (int i = 0; i < availableMoves.length; i++) {
            if (availableMoves[i] != null) {
                System.out.println((i + 1) + ". " + availableMoves[i].moveName + " (PP: " + availableMoves[i].pp + ")");
            }
        }

        System.out.print("\n> ");
        int choice = scanner.nextInt() - 1;
        if (choice < 0 || choice >= 4 || availableMoves[choice] == null) {
            System.out.println("Invalid choice! Using first move instead.");
            return availableMoves[0];
        }
        return availableMoves[choice];
    }

    //=====================================
    // BATTLE INFO
    //=====================================
    private void printBattleStatus(Pokemon p, Pokemon w) {
    System.out.println("\n==================================================");
    
    //=====================================
    // OPPONENT STATUS
    //=====================================
    String wTypes = w.getType1() + "/" + w.getType2();
    System.out.println(w.getName() + " [" + wTypes + "] LVL:" + w.getLevel());
    System.out.println("HP: " + w.getHp() + "/" + w.getHp()); 
    
    System.out.println("--------------------------------------------------");
    
    //=====================================
    // PLAYER STATUS
    //=====================================
    String pTypes = p.getType1() + "/" + p.getType2();
    System.out.println(p.getName() + " [" + pTypes + "] LVL:" + p.getLevel());
    System.out.println("HP: " + p.getHp() + "/" + p.getHp());
    
    System.out.println("==================================================");
}
}
