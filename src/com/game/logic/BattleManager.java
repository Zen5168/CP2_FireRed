package com.game.logic;

import com.game.pokemons.Pokemon;
import com.game.moves.Moves;
import com.game.trainers.*;
import com.game.items.*;
import com.game.audio.*;
import java.util.*;

public class BattleManager {

    private Scanner scanner = new Scanner(System.in);
    private BattleEngine engine = new BattleEngine();
    private AudioManager audio = new AudioManager();

    // ======================================
    // BATTLE INTERFACE
    // ======================================
    public boolean startBattle(Player player, Pokemon playerMon, Pokemon enemyMon, boolean isTrainerBattle) {
        boolean battleActive = true;

        audio.playWithLoop("JJK.wav", 1500000L);

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
                    Item selectedItem = selectItem(player);
                    if (selectedItem != null) {
                        if (selectedItem instanceof Pokeball) {
                            handleCatchAttempt(player, (Pokeball) selectedItem, enemyMon);
                        } else {
                            selectedItem.use(playerMon);
                            player.getBag().removeItem(selectedItem.getName(), 1);
                            engine.executeTurn(enemyMon, playerMon, getBestMove(enemyMon, playerMon));
                        }
                    }
                    break;

                case "3":
                    Pokemon swappedMon = switchPokemon(player, playerMon);
                    if (swappedMon != null && swappedMon != playerMon) {
                        playerMon = swappedMon;
                        System.out.println("Go! " + playerMon.getName() + "!");

                        enemyMove = getBestMove(enemyMon, playerMon);
                        engine.executeTurn(enemyMon, playerMon, enemyMove);
                    }
                    break;

                case "4":
                    // RUN
                    if (isTrainerBattle) {
                        System.out.println("No! There's no running from a Trainer battle!");
                    } else {
                        System.out.println("Got away safely!");
                        audio.stopCurrent();
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
                audio.stopCurrent();
                return false;
            } else if (enemyMon.isFainted()) {
                System.out.println("The wild " + enemyMon.getName() + " fainted!");
                audio.stopCurrent();

                // GAIN EXP
                int expGained = calculateExpReward(enemyMon);
                playerMon.gainExp(expGained);
                return true;
            }
        }
        return true;
    }

    // ======================================
    // ITEM SELECTOR
    // ======================================
    private Item selectItem(Player player) {
        Bag bag = player.getBag();
        var allCategories = bag.getCategories();

        // SELECT CATEGORY
        System.out.println("\n--- BAG CATEGORIES ---");
        List<String> categoryNames = new ArrayList<>(allCategories.keySet());
        for (int i = 0; i < categoryNames.size(); i++) {
            System.out.println((i + 1) + ". " + categoryNames.get(i));
        }
        System.out.println("0. Back");
        System.out.print("Choose Category: ");

        try {
            int catChoice = Integer.parseInt(scanner.nextLine());
            if (catChoice == 0) {
                return null;
            }

            String selectedCat = categoryNames.get(catChoice - 1);
            Map<String, Integer> itemsInCat = allCategories.get(selectedCat);

            if (itemsInCat.isEmpty()) {
                System.out.println("This pocket is empty!");
                return null;
            }

            // SELECT ITEM FROM CATEGORY
            System.out.println("\n--- " + selectedCat.toUpperCase() + " ---");
            List<String> itemNames = new ArrayList<>(itemsInCat.keySet());
            for (int i = 0; i < itemNames.size(); i++) {
                String name = itemNames.get(i);
                System.out.println((i + 1) + ". " + name + " (x" + itemsInCat.get(name) + ")");
            }
            System.out.println("0. Back");
            System.out.print("Select item: ");

            int itemChoice = Integer.parseInt(scanner.nextLine());
            if (itemChoice == 0) {
                return null;
            }

            String finalItemName = itemNames.get(itemChoice - 1);
            return bag.getItemObject(finalItemName);

        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
        return null;
    }

    // ======================================
    // CATCHING MECHANIC
    // ======================================
    private void handleCatchAttempt(Player player, Pokeball ball, Pokemon target) {
        System.out.println("\nYou threw a " + ball.getName() + "!");

        player.getBag().removeItem(ball.getName(), 1);

        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(800);
                System.out.print("*wobble* ");
            }
            Thread.sleep(1000);
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (ball.tryCatch(target)) {
            System.out.println("Gotcha! " + target.getName() + " was caught!");
            player.addPokemon(target);

            // END THE LOOP IN BATTLE
            target.setHp(0);
        } else {
            System.out.println("Oh no! The Pokemon broke free!");

            Pokemon activeMon = player.getParty().get(0);
            Moves enemyMove = getBestMove(target, activeMon);

            System.out.println("The wild " + target.getName() + " counters!");
            engine.executeTurn(target, activeMon, enemyMove);
        }
    }

    // ======================================
    // SWITCH POKEMON
    // ======================================
    private Pokemon switchPokemon(Player player, Pokemon currentMon) {
        ArrayList<Pokemon> party = player.getParty();
        System.out.println("\n--- CHOOSE A POKEMON ---");

        for (int i = 0; i < party.size(); i++) {
            Pokemon p = party.get(i);
            String status = p.isFainted() ? "(FAINTED)" : "HP: " + p.getHp() + "/" + p.getMaxHp();
            System.out.println((i + 1) + ". " + p.getName() + " [" + status + "]");
        }
        System.out.println("0. Cancel");
        System.out.print("Select: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice == -1) {
                return null;
            }
            if (choice >= 0 && choice < party.size()) {
                Pokemon selected = party.get(choice);

                if (selected == currentMon) {
                    System.out.println(selected.getName() + " is already in battle!");
                    return null;
                }
                if (selected.isFainted()) {
                    System.out.println(selected.getName() + " has no energy left to battle!");
                    return null;
                }
                return selected;
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
        return null;
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
    // EXP REWARD
    // ======================================
    private int calculateExpReward(Pokemon faintedMon) {
        int baseYield = 200;
        return (baseYield * faintedMon.getLevel()) / 7;
    }

    // ======================================
    // MOVE SELECTOR
    // ======================================
    private Moves selectMove(Pokemon p) {
        Moves[] availableMoves = p.getMoves();

        while (true) {
            System.out.println("\nMoves: ");

            for (int i = 0; i < availableMoves.length; i++) {
                if (availableMoves[i] != null) {
                    System.out.println((i + 1) + ". " + availableMoves[i].moveName
                            + " [" + availableMoves[i].moveType
                            + "] (PP: " + availableMoves[i].pp + ")");
                }
            }

            System.out.print("\nSelect a move: ");

            try {
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);

                if ((choice-1) >= 0 && (choice-1) < availableMoves.length && availableMoves[(choice-1)] != null) {
                    return availableMoves[choice - 1];
                } else {
                    System.out.println("Invalid choice! Please pick one of the moves listed above.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
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
        System.out.println("EXP:   " + p.getExp() + " / " + p.getNextLevelExp() + " (Next Level)");
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
