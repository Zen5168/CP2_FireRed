package com.game.logic;

import com.game.pokemons.Pokemon;
import com.game.moves.Moves;
import java.util.*;

public class BattleManager {

    private Scanner scanner = new Scanner(System.in);
    private BattleEngine engine = new BattleEngine();

    // ======================================
    // BATTLE INTERFACE
    // ======================================
    public void startBattle(Pokemon playerMon, Pokemon enemyMon, boolean isTrainerBattle) {
        boolean battleActive = true;

        while (battleActive) {
            printBattleStatus(playerMon, enemyMon);
            System.out.println("What will " + playerMon.getName() + " do?");
            System.out.print("\n1. Fight  \n2. Bag  \n3. Pokemon  \n4. Run \n> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // FIGHT
                    Moves playerMove = selectMove(playerMon);
                    Moves enemyMove = getBestMove(enemyMon, playerMon);

                    if (playerMon.getSpeed() >= enemyMon.getSpeed()) {
                        executeFullTurn(playerMon, enemyMon, playerMove, enemyMove);
                    } else {
                        executeFullTurn(enemyMon, playerMon, enemyMove, playerMove);
                    }
                    break;

                case "2":
                    // BAG: Placeholder
                    System.out.println("This function is currently not available in this version");
                    break;

                case "3":
                    // POKEMON: Placeholder
                    System.out.println("This function is currently not available in this version");
                    break;

                case "4":
                    // RUN
                    if (isTrainerBattle) {
                        System.out.println("No! There's no running from a Trainer battle!");
                    } else {
                        System.out.println("Got away safely!");
                        battleActive = false;
                    }
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }

            // CHECK IF ANYONE FAINTED AFTER THE TURN
            if (playerMon.isFainted()) {
                System.out.println(playerMon.getName() + " fainted! You lost the battle...");
                battleActive = false;
            } else if (enemyMon.isFainted()) {
                System.out.println("The wild " + enemyMon.getName() + " fainted!");
                battleActive = false;
            }
        }
    }

    // ======================================
    // EXECUTE FULL TURN
    // ======================================
    private void executeFullTurn(Pokemon first, Pokemon second, Moves firstMove, Moves secondMove) {
        // FIRST POKEMON ATTACKS
        engine.executeTurn(first, second, firstMove);

        // SECOND POKEMON ATTACKS ONLY IF IT SURVIVED
        if (!second.isFainted()) {
            engine.executeTurn(second, first, secondMove);
        }
    }

    // ======================================
    // MOVE SELECTOR
    // ======================================
    private Moves selectMove(Pokemon p) {
        Moves[] availableMoves = p.getMoves();
        for (int i = 0; i < availableMoves.length; i++) {
            if (availableMoves[i] != null) {
                System.out.println((i + 1) + ". " + availableMoves[i].moveName + " (PP: " + availableMoves[i].pp + ")");
            }
        }

        System.out.print("\nSelect a move: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < 4 && availableMoves[choice] != null) {
                return availableMoves[choice];
            }
        } catch (Exception e) {
        }

        System.out.println("Invalid choice! Struggling...");
        return availableMoves[0]; // DEFAULT TO FIRST MOVE
    }

    // ======================================
    // BATTLE STATUS
    // ======================================
    private void printBattleStatus(Pokemon p, Pokemon w) {
        System.out.println("\n==================================================");

        // ENEMY STATUS
        String wTypeDisplay = w.getType1().toString();
        if (w.getType2() != null && w.getType2() != Type.NONE) {
            wTypeDisplay += "/" + w.getType2();
        }

        System.out.println("ENEMY: " + w.getName() + " [" + wTypeDisplay + "] LVL:" + w.getLevel());
        System.out.println("HP:    " + w.getHp() + "/" + w.getMaxHp());
        System.out.println("--------------------------------------------------");

        // PLAYER STATUS
        String pTypeDisplay = p.getType1().toString();
        if (p.getType2() != null && p.getType2() != Type.NONE) {
            pTypeDisplay += "/" + p.getType2();
        }

        System.out.println("YOU:   " + p.getName() + " [" + pTypeDisplay + "] LVL:" + p.getLevel());
        System.out.println("HP:    " + p.getHp() + "/" + p.getMaxHp());
        System.out.println("==================================================");
    }

    // ======================================
    // ENEMY AI
    // ======================================
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
}
