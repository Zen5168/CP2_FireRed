package com.game.logic;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import com.game.moves.Moves;
import com.game.items.*;
import com.game.trainers.Player;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BattleUI {

    private GamePanel gp;

    // BATTLE STATE
    public enum BattleState {
        INTRO, // "A WILD X APPEARED!"
        MAIN_MENU, // FIGHT, BAG, POKEMON, RUN
        FIGHT_MENU, // MOVE SELECTION
        BAG_MENU, // ITEM CATEGORIES
        BAG_ITEMS, // ITEMS IN CATEGORY
        POKEMON_MENU, // POKEMON PARTY
        BATTLE_ACTION, // EXECUTING MOVES
        DIALOGUE, // BATTLE MESSAGES
        VICTORY, // WON THE BATTLE
        DEFEAT           // LOST THE BATTLE
    }

    // BATTLE ACTION QUEUE
    private class BattleAction {

        Pokemon attacker;
        Pokemon defender;
        Moves move;
        BattleEngine engine;

        BattleAction(Pokemon attacker, Pokemon defender, Moves move, BattleEngine engine) {
            this.attacker = attacker;
            this.defender = defender;
            this.move = move;
            this.engine = engine;
        }
    }

    private BattleState currentState = BattleState.INTRO;
    private int selectedOption = 0;
    private String currentDialogue = "";
    private List<String> dialogueQueue = new ArrayList<>();
    private List<BattleAction> actionQueue = new ArrayList<>();

    // MENU OPTIONS
    private String[] mainMenuOptions = {"FIGHT", "BAG", "POKEMON", "RUN"};
    private Moves[] fightMenuMoves;
    private String[] bagCategories;
    private List<String> bagItems;
    private int selectedCategory = 0;

    // BATTLE DATA
    private Pokemon playerPokemon;
    private Pokemon enemyPokemon;
    private Player playerTrainer;
    private boolean isTrainerBattle;

    public BattleUI(GamePanel gp) {
        this.gp = gp;
    }

    //=====================================
    // INITIALIZE BATTLE
    //=====================================
    public void initBattle(Player trainer, Pokemon player, Pokemon enemy, boolean trainerBattle) {
        this.playerTrainer = trainer;
        this.playerPokemon = player;
        this.enemyPokemon = enemy;
        this.isTrainerBattle = trainerBattle;
        this.currentState = BattleState.INTRO;
        this.selectedOption = 0;

        // CLEAR ANY PREVIOUS DIALOGUE AND ACTIONS
        this.dialogueQueue.clear();
        this.actionQueue.clear();
        this.currentDialogue = "";

        // ADD INTRO DIALOGUE
        addDialogue("A wild " + enemy.getName() + " appeared!");
    }

    //=====================================
    // UPDATE
    //=====================================
    public void update() {
        // REMOVED AUTO-ADVANCE - Now requires button press
        // Battle messages will only advance when player presses a button
    }

    //=====================================
    // HANDLE INPUT
    //=====================================
    public void handleInput(String input) {
        switch (currentState) {
            case INTRO:
            case DIALOGUE:
            case BATTLE_ACTION:
            case VICTORY:
            case DEFEAT:
                if (input.equals("U") || input.equals("SPACE") || input.equals("J") || input.equals("ENTER")) {
                    nextDialogue();
                }
                break;

            case MAIN_MENU:
                handleMainMenuInput(input);
                break;

            case FIGHT_MENU:
                handleFightMenuInput(input);
                break;

            case BAG_MENU:
                handleBagMenuInput(input);
                break;

            case BAG_ITEMS:
                handleBagItemsInput(input);
                break;

            case POKEMON_MENU:
                handlePokemonMenuInput(input);
                break;
        }
    }

    //=====================================
    // MAIN MENU INPUT
    //=====================================
    private void handleMainMenuInput(String input) {
        switch (input) {
            case "W":
                if (selectedOption >= 2) {
                    selectedOption -= 2;
                }
                break;
            case "S":
                if (selectedOption < 2) {
                    selectedOption += 2;
                }
                break;
            case "A":
                if (selectedOption % 2 == 1) {
                    selectedOption--;
                }
                break;
            case "D":
                if (selectedOption % 2 == 0 && selectedOption < 3) {
                    selectedOption++;
                }
                break;
            case "U": // Confirm
                selectMainMenuOption();
                break;
            case "I": // Back (not applicable in main menu)
                break;
        }
    }

    //=====================================
    // SELECT MAIN MENU OPTION
    //=====================================
    private void selectMainMenuOption() {
        switch (selectedOption) {
            case 0: // FIGHT
                enterFightMenu();
                break;
            case 1: // BAG
                enterBagMenu();
                break;
            case 2: // POKEMON
                enterPokemonMenu();
                break;
            case 3: // RUN
                attemptRun();
                break;
        }
    }

    //=====================================
    // FIGHT MENU
    //=====================================
    private void enterFightMenu() {
        
        fightMenuMoves = playerPokemon.getMoves();
        selectedOption = 0;
        currentState = BattleState.FIGHT_MENU;

        System.out.println("\n=== ENTERING FIGHT MENU ===");
        System.out.println("Player Pokemon: " + playerPokemon.getName());
        System.out.println("Moves array length: " + fightMenuMoves.length);

        boolean hasAnyMoves = false;
        for (int i = 0; i < fightMenuMoves.length; i++) {
            if (fightMenuMoves[i] != null) {
                hasAnyMoves = true;
                System.out.println("  Move " + i + ": " + fightMenuMoves[i].moveName
                        + " (PP: " + fightMenuMoves[i].pp + "/" + fightMenuMoves[i].maxPp
                        + ", Type: " + fightMenuMoves[i].moveType + ")");
            } else {
                System.out.println("  Move " + i + ": null");
            }
        }

        if (!hasAnyMoves) {
            System.out.println("WARNING: No moves found! Pokemon may not have learned any moves.");
            System.out.println("Check if MoveDatabase is loading correctly.");
        }
        System.out.println("===========================\n");
    }

    private void handleFightMenuInput(String input) {
        int moveCount = 0;
        for (Moves m : fightMenuMoves) {
            if (m != null) {
                moveCount++;
            }
        }

        switch (input) {
            case "W":
                if (selectedOption >= 2) {
                    selectedOption -= 2;
                }
                break;
            case "S":
                if (selectedOption < 2 && selectedOption + 2 < moveCount) {
                    selectedOption += 2;
                }
                break;
            case "A":
                if (selectedOption % 2 == 1) {
                    selectedOption--;
                }
                break;
            case "D":
                if (selectedOption % 2 == 0 && selectedOption + 1 < moveCount) {
                    selectedOption++;
                }
                break;
            case "U": // Confirm
                if (fightMenuMoves[selectedOption] != null) {
                    executePlayerMove(fightMenuMoves[selectedOption]);
                }
                break;
            case "I": // Back
                currentState = BattleState.MAIN_MENU;
                selectedOption = 0;
                break;
        }
    }

    //=====================================
    // BAG MENU
    //=====================================
    private void enterBagMenu() {
        var categories = playerTrainer.getBag().getCategories();
        bagCategories = categories.keySet().toArray(new String[0]);
        selectedOption = 0;
        currentState = BattleState.BAG_MENU;
    }

    private void handleBagMenuInput(String input) {
        switch (input) {
            case "W":
                if (selectedOption > 0) {
                    selectedOption--;
                }
                break;
            case "S":
                if (selectedOption < bagCategories.length - 1) {
                    selectedOption++;
                }
                break;
            case "U": // Confirm
                enterBagItems();
                break;
            case "I": // Back
                currentState = BattleState.MAIN_MENU;
                selectedOption = 0;
                break;
        }
    }

    private void enterBagItems() {
        String category = bagCategories[selectedOption];
        var items = playerTrainer.getBag().getCategories().get(category);
        bagItems = new ArrayList<>(items.keySet());

        if (bagItems.isEmpty()) {
            addDialogue("This pocket is empty!");
            currentState = BattleState.DIALOGUE;
        } else {
            selectedCategory = selectedOption;
            selectedOption = 0;
            currentState = BattleState.BAG_ITEMS;
        }
    }

    private void handleBagItemsInput(String input) {
        switch (input) {
            case "W":
                if (selectedOption > 0) {
                    selectedOption--;
                }
                break;
            case "S":
                if (selectedOption < bagItems.size() - 1) {
                    selectedOption++;
                }
                break;
            case "U": // Confirm
                useItem(bagItems.get(selectedOption));
                break;
            case "I": // Back
                currentState = BattleState.BAG_MENU;
                selectedOption = selectedCategory;
                break;
        }
    }

    //=====================================
    // POKEMON MENU
    //=====================================
    private void enterPokemonMenu() {
        selectedOption = 0;
        currentState = BattleState.POKEMON_MENU;
    }

    private void handlePokemonMenuInput(String input) {
        int partySize = playerTrainer.getParty().size();

        switch (input) {
            case "W":
                if (selectedOption > 0) {
                    selectedOption--;
                }
                break;
            case "S":
                if (selectedOption < partySize - 1) {
                    selectedOption++;
                }
                break;
            case "U": // Confirm
                switchPokemon(selectedOption);
                break;
            case "I": // Back
                currentState = BattleState.MAIN_MENU;
                selectedOption = 0;
                break;
        }
    }

    //=====================================
    // BATTLE ACTIONS
    //=====================================
    private void executePlayerMove(Moves move) {
        currentState = BattleState.BATTLE_ACTION;

        BattleEngine engine = new BattleEngine();

        // TURN ORDER AND QUEUE ACTIONS
        if (playerPokemon.getSpeed() >= enemyPokemon.getSpeed()) {
            // PLAYER FIRST, THEN ENEMY
            executeMoveWithDialogue(playerPokemon, enemyPokemon, move, engine);

            Moves enemyMove = getEnemyMove();
            if (enemyMove != null) {
                executeMoveWithDialogue(enemyPokemon, playerPokemon, enemyMove, engine);
            }
        } else {
            // ENEMY FIRST, THEN PLAYER
            Moves enemyMove = getEnemyMove();
            if (enemyMove != null) {
                executeMoveWithDialogue(enemyPokemon, playerPokemon, enemyMove, engine);
            }

            executeMoveWithDialogue(playerPokemon, enemyPokemon, move, engine);
        }
    }

    private void executeMoveWithDialogue(Pokemon attacker, Pokemon defender, Moves move, BattleEngine engine) {
        // QUEUE THE MOVE MESSAGE FIRST
        addDialogue(attacker.getName() + " used " + move.moveName + "!");

        // QUEUE THE ACTUAL DAMAGE APPLICATION
        actionQueue.add(new BattleAction(attacker, defender, move, engine));
    }

    private void useItem(String itemName) {
        Item item = playerTrainer.getBag().getItemObject(itemName);

        if (item instanceof Pokeball) {
            addDialogue("You threw a " + item.getName() + "!");
            Pokeball ball = (Pokeball) item;
            playerTrainer.getBag().removeItem(itemName, 1);

            if (ball.tryCatch(enemyPokemon)) {
                addDialogue("Gotcha! " + enemyPokemon.getName() + " was caught!");
                playerTrainer.addPokemon(enemyPokemon);
                // Don't set HP to 0 - just end the battle
                currentState = BattleState.VICTORY;
                return; // Exit immediately to prevent state override
            } else {
                addDialogue("Oh no! The Pokemon broke free!");
                // ENEMY ATTACKS
                Moves enemyMove = getEnemyMove();
                BattleEngine engine = new BattleEngine();
                executeMoveWithDialogue(enemyPokemon, playerPokemon, enemyMove, engine);
                checkBattleEnd();
            }
        } else {
            item.use(playerPokemon);
            playerTrainer.getBag().removeItem(itemName, 1);
            addDialogue("Used " + item.getName() + " on " + playerPokemon.getName() + "!");

            // ENEMY ATTACKS
            Moves enemyMove = getEnemyMove();
            BattleEngine engine = new BattleEngine();
            executeMoveWithDialogue(enemyPokemon, playerPokemon, enemyMove, engine);
            checkBattleEnd();
        }

        currentState = BattleState.DIALOGUE;
    }

    private void switchPokemon(int index) {
        Pokemon newPokemon = playerTrainer.getParty().get(index);

        if (newPokemon == playerPokemon) {
            addDialogue(newPokemon.getName() + " is already in battle!");
            currentState = BattleState.DIALOGUE;
            return;
        }

        if (newPokemon.isFainted()) {
            addDialogue(newPokemon.getName() + " has no energy left!");
            currentState = BattleState.DIALOGUE;
            return;
        }

        playerPokemon = newPokemon;
        addDialogue("Go! " + playerPokemon.getName() + "!");

        // ENEMY ATTACKS
        Moves enemyMove = getEnemyMove();
        BattleEngine engine = new BattleEngine();
        executeMoveWithDialogue(enemyPokemon, playerPokemon, enemyMove, engine);

        checkBattleEnd();
        currentState = BattleState.DIALOGUE;
    }

    private void attemptRun() {
        if (isTrainerBattle) {
            addDialogue("No! There's no running from a Trainer battle!");
            currentState = BattleState.DIALOGUE;
        } else {
            addDialogue("Got away safely!");
            currentState = BattleState.VICTORY; // Use VICTORY state so dialogue shows before exiting
        }
    }

    //=====================================
    // ENEMY AI
    //=====================================
    private Moves getEnemyMove() {
        Moves bestMove = null;
        double maxDamage = -1;

        // DEBUG: Check enemy moves
        System.out.println("\n=== ENEMY AI SELECTING MOVE ===");
        System.out.println("Enemy Pokemon: " + enemyPokemon.getName());

        Moves[] enemyMoves = enemyPokemon.getMoves();
        if (enemyMoves == null || enemyMoves.length == 0) {
            System.out.println("ERROR: Enemy has no moves array!");
            return null;
        }

        for (int i = 0; i < enemyMoves.length; i++) {
            Moves move = enemyMoves[i];
            if (move == null) {
                System.out.println("  Move " + i + ": null");
                continue;
            }

            System.out.println("  Move " + i + ": " + move.moveName + " (PP: " + move.pp + ", Power: " + move.power + ")");

            if (move.pp <= 0) {
                System.out.println("    -> Skipped (no PP)");
                continue;
            }

            double typeEff = Type.getEffectiveness(move.getTypeEnum(),
                    playerPokemon.getType1(), playerPokemon.getType2());
            double potentialPower = move.power * typeEff;

            System.out.println("    -> Potential damage: " + potentialPower + " (Type effectiveness: " + typeEff + ")");

            if (potentialPower > maxDamage) {
                maxDamage = potentialPower;
                bestMove = move;
            }
        }

        // FALLBACK: If no best move found, use first available move with PP
        if (bestMove == null) {
            System.out.println("No best move found, using fallback...");
            for (Moves move : enemyMoves) {
                if (move != null && move.pp > 0) {
                    bestMove = move;
                    System.out.println("Fallback move: " + move.moveName);
                    break;
                }
            }
        }

        // LAST RESORT: Use first move regardless of PP
        if (bestMove == null && enemyMoves[0] != null) {
            System.out.println("LAST RESORT: Using first move regardless of PP");
            bestMove = enemyMoves[0];
        }

        if (bestMove != null) {
            System.out.println("Selected move: " + bestMove.moveName);
        } else {
            System.out.println("ERROR: No move could be selected!");
        }
        System.out.println("===============================\n");

        return bestMove;
    }

    //=====================================
    // BATTLE END CHECK
    //=====================================
    private void checkBattleEnd() {
        if (playerPokemon.isFainted()) {
            addDialogue(playerPokemon.getName() + " fainted!");
            addDialogue("You lost the battle...");
            currentState = BattleState.DEFEAT;
        } else if (enemyPokemon.isFainted()) {
            addDialogue("The wild " + enemyPokemon.getName() + " fainted!");
            int expGained = (200 * enemyPokemon.getLevel()) / 7;
            playerPokemon.gainExp(expGained);
            addDialogue(playerPokemon.getName() + " gained " + expGained + " EXP!");
            currentState = BattleState.VICTORY;
        } else {
        }
    }

    //=====================================
    // DIALOGUE SYSTEM
    //=====================================
    private void addDialogue(String text) {
        dialogueQueue.add(text);
        if (currentDialogue.isEmpty()) {
            nextDialogue();
        }
    }

    private void nextDialogue() {
        // CHECKS IF WE JUST DISPLAYED A MOVE MESSAGE (CONTAINS "USED")
        // IF SO, EXECUTE THE CORRESPONDING ACTION NOW
        if (currentDialogue.contains(" used ") && !actionQueue.isEmpty()) {
            System.out.println("[DEBUG] Executing damage after showing: " + currentDialogue);
            System.out.println("[DEBUG] Action queue size: " + actionQueue.size());

            BattleAction action = actionQueue.remove(0);
            System.out.println("[DEBUG] Executing: " + action.attacker.getName() + " -> " + action.defender.getName());

            executeDamageOnly(action);

            // NOW SHOW THE NEXT MESSAGE (EFFECTIVENESS, ETC.)
            if (!dialogueQueue.isEmpty()) {
                currentDialogue = dialogueQueue.remove(0);
            } else {
                currentDialogue = "";
            }
            return;
        }

        if (!dialogueQueue.isEmpty()) {
            currentDialogue = dialogueQueue.remove(0);
            System.out.println("[DEBUG] Showing dialogue: " + currentDialogue);
        } else {
            currentDialogue = "";

            // NO MORE DIALOGUE OR ACTIONS
            if (currentState == BattleState.INTRO) {
                currentState = BattleState.MAIN_MENU;
                selectedOption = 0;
            } else if (currentState == BattleState.DIALOGUE || currentState == BattleState.BATTLE_ACTION) {
                // CHECK IF BATTLE SHOULD END (BOTH POKEMON STILL ALIVE = CONTINUE)
                if (!playerPokemon.isFainted() && !enemyPokemon.isFainted()) {
                    currentState = BattleState.MAIN_MENU;
                    selectedOption = 0;
                } else {
                    // SOMEONE FAINTED, CHECKBATTLEEND SHOULD HAVE BEEN CALLED
                    checkBattleEnd();
                }
            } else if (currentState == BattleState.VICTORY || currentState == BattleState.DEFEAT) {
                gp.gameState = GameState.OVERWORLD;
            }
        }
    }

    //=====================================
    // EXECUTE DAMAGE ONLY (CALLED AFTER MOVE MESSAGE)
    //=====================================
    private void executeDamageOnly(BattleAction action) {
        // CHECK IF ATTACKER FAINTED BEFORE THIS ACTION
        if (action.attacker.isFainted()) {
            // ATTACKER FAINTED, SKIP THIS ACTION
            return;
        }

        // EXECUTE THE MOVE AND CAPTURE RESULTS
        String battleResult = action.engine.executeTurnWithResult(
                action.attacker, action.defender, action.move);

        // ADD EFFECTIVENESS/RESULT MESSAGES TO QUEUE (WILL SHOW AFTER NEXT BUTTON PRESS)
        if (battleResult != null && !battleResult.isEmpty()) {
            String[] messages = battleResult.split("\n");
            for (String msg : messages) {
                if (!msg.trim().isEmpty() && !msg.contains("used")) {
                    addDialogue(msg.trim());
                }
            }
        }

        // CHECK IF DEFENDER FAINTED AFTER THIS ACTION
        if (action.defender.isFainted()) {
            // CLEAR REMAINING ACTIONS SINCE BATTLE IS ENDING
            actionQueue.clear();
            checkBattleEnd();
        }
    }

    //=====================================
    // DRAW UI
    //=====================================
    public void draw(Graphics2D g2) {
        switch (currentState) {
            case INTRO:
            case DIALOGUE:
            case BATTLE_ACTION:
                // ONLY SHOW DIALOGUE BOX - NO MENU
                drawDialogueBox(g2);
                break;
            case VICTORY:
            case DEFEAT:
                // ONLY SHOW DIALOGUE BOX - NO MENU
                drawDialogueBox(g2);
                break;
            case MAIN_MENU:
                drawMainMenuFireRed(g2);
                break;
            case FIGHT_MENU:
                drawFightMenuFireRed(g2);
                break;
            case BAG_MENU:
                drawBagMenuFireRed(g2);
                break;
            case BAG_ITEMS:
                drawBagItemsFireRed(g2);
                break;
            case POKEMON_MENU:
                drawPokemonMenuFireRed(g2);
                break;
        }
    }

    //=====================================
    // DRAW DIALOGUE BOX (FIRE RED STYLE)
    //=====================================
    private void drawDialogueBox(Graphics2D g2) {
        int boxX = 10;
        int boxY = gp.screenHeight - 140;
        int boxWidth = gp.screenWidth - 20;
        int boxHeight = 130;

        // BOX BACKGROUND (WHITE)
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        // BOX BORDER (BLACK)
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        // TEXT
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(Color.BLACK);
        drawWrappedText(g2, currentDialogue, boxX + 15, boxY + 30, boxWidth - 30);

        // CONTINUE INDICATOR (ALWAYS SHOW - WAITING FOR INPUT)
        int blinkTimer = (int) (System.currentTimeMillis() / 500) % 2; // Blink every 500ms
        if (blinkTimer == 0) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.BLACK);
            g2.drawString("▼", boxX + boxWidth - 30, boxY + boxHeight - 15);
        }

        // SHOW CONTROLS HINT FOR DIALOGUE
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(100, 100, 100));
        g2.drawString("Press U/I/J/K/Space/Enter to continue", 20, gp.screenHeight - 10);
    }

    //=====================================
    // DRAW MAIN MENU (FIRE RED STYLE)
    //=====================================
    private void drawMainMenuFireRed(Graphics2D g2) {
        // MAIN DIALOGUE BOX
        int boxX = 10;
        int boxY = gp.screenHeight - 140;
        int boxWidth = gp.screenWidth / 2 - 15;
        int boxHeight = 130;

        // LEFT BOX - "What will [Pokemon] do?"
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        g2.setFont(new Font("Arial", Font.PLAIN, 15));
        g2.drawString("What will", boxX + 15, boxY + 30);
        g2.drawString(playerPokemon.getName() + " do?", boxX + 15, boxY + 50);

        // RIGHT BOX - MENU OPTIONS
        int menuX = gp.screenWidth / 2 + 5;
        int menuY = boxY;
        int menuWidth = gp.screenWidth / 2 - 15;
        int menuHeight = 130;

        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 10, 10);
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 10, 10);

        // MENU OPTIONS (2x2 GRID)
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        for (int i = 0; i < mainMenuOptions.length; i++) {
            int row = i / 2;
            int col = i % 2;
            int optX = menuX + 20 + (col * (menuWidth / 2));
            int optY = menuY + 40 + (row * 50);

            if (i == selectedOption) {
                // SELECTION ARROW
                g2.setColor(Color.BLACK);
                g2.fillPolygon(
                        new int[]{optX - 10, optX - 10, optX - 5},
                        new int[]{optY - 15, optY - 5, optY - 10},
                        3
                );
            }

            g2.setColor(Color.BLACK);
            g2.drawString(mainMenuOptions[i], optX, optY);
        }

        drawControlsHint(g2);
    }

    //=====================================
    // DRAW FIGHT MENU (FIRE RED STYLE)
    //=====================================
    private void drawFightMenuFireRed(Graphics2D g2) {
        // MAIN BOX AT BOTTOM
        int boxX = 10;
        int boxY = gp.screenHeight - 140;
        int boxWidth = gp.screenWidth - 20;
        int boxHeight = 130;

        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        // CHECK IF MOVES EXIST
        int moveCount = 0;
        for (Moves m : fightMenuMoves) {
            if (m != null) {
                moveCount++;
            }
        }

        if (moveCount == 0) {
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.RED);
            g2.drawString("No moves available!", boxX + 20, boxY + 50);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("Press I to go back", boxX + 20, boxY + 80);
        } else {
            // MOVES (2x2 GRID - FIRE RED STYLE)
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            for (int i = 0; i < fightMenuMoves.length && fightMenuMoves[i] != null; i++) {
                int row = i / 2;
                int col = i % 2;
                int optX = boxX + 30 + (col * (boxWidth / 2));
                int optY = boxY + 40 + (row * 50);

                Moves move = fightMenuMoves[i];

                // SELECTION ARROW
                if (i == selectedOption) {
                    g2.setColor(Color.BLACK);
                    g2.fillPolygon(
                            new int[]{optX - 15, optX - 15, optX - 10},
                            new int[]{optY - 15, optY - 5, optY - 10},
                            3
                    );
                }

                // MOVE NAME
                g2.setColor(Color.BLACK);
                g2.drawString(move.moveName, optX, optY);

                // PP INFO
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                g2.drawString("PP " + move.pp + "/" + move.maxPp, optX, optY + 18);

                // TYPE (COLORED BOX)
                drawTypeBox(g2, move.moveType, optX + 100, optY - 12);

                g2.setFont(new Font("Arial", Font.BOLD, 16));
            }
        }

        drawControlsHint(g2);
    }

    //=====================================
    // DRAW TYPE BOX
    //=====================================
    private void drawTypeBox(Graphics2D g2, Type type, int x, int y) {
        Color typeColor = getTypeColor(type);

        g2.setColor(typeColor);
        g2.fillRoundRect(x, y, 60, 20, 5, 5);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.drawString(type.toString(), x + 5, y + 14);
    }

    //=====================================
    // GET TYPE COLOR
    //=====================================
    private Color getTypeColor(Type type) {
        switch (type) {
            case FIRE:
                return new Color(240, 128, 48);
            case WATER:
                return new Color(104, 144, 240);
            case GRASS:
                return new Color(120, 200, 80);
            case ELECTRIC:
                return new Color(248, 208, 48);
            case NORMAL:
                return new Color(168, 168, 120);
            case FIGHTING:
                return new Color(192, 48, 40);
            case FLYING:
                return new Color(168, 144, 240);
            case POISON:
                return new Color(160, 64, 160);
            case GROUND:
                return new Color(224, 192, 104);
            case ROCK:
                return new Color(184, 160, 56);
            case BUG:
                return new Color(168, 184, 32);
            case GHOST:
                return new Color(112, 88, 152);
            case PSYCHIC:
                return new Color(248, 88, 136);
            case ICE:
                return new Color(152, 216, 216);
            case DRAGON:
                return new Color(112, 56, 248);
            case NONE:
                return new Color(168, 168, 120);
            default:
                return new Color(168, 168, 120);
        }
    }

    //=====================================
    // DRAW BAG MENU (FIRE RED STYLE)
    //=====================================
    private void drawBagMenuFireRed(Graphics2D g2) {
        int boxX = 10;
        int boxY = gp.screenHeight - 140;
        int boxWidth = gp.screenWidth - 20;
        int boxHeight = 130;

        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("BAG", boxX + 15, boxY + 25);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 0; i < bagCategories.length && i < 3; i++) {
            int optY = boxY + 50 + (i * 25);

            if (i == selectedOption) {
                g2.fillPolygon(
                        new int[]{boxX + 15, boxX + 15, boxX + 20},
                        new int[]{optY - 12, optY - 2, optY - 7},
                        3
                );
            }

            g2.drawString(bagCategories[i], boxX + 30, optY);
        }

        drawControlsHint(g2);
    }

    //=====================================
    // DRAW BAG ITEMS (FIRE RED STYLE)
    //=====================================
    private void drawBagItemsFireRed(Graphics2D g2) {
        int boxX = 10;
        int boxY = gp.screenHeight - 140;
        int boxWidth = gp.screenWidth - 20;
        int boxHeight = 130;

        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString(bagCategories[selectedCategory], boxX + 15, boxY + 25);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 0; i < bagItems.size() && i < 3; i++) {
            int optY = boxY + 50 + (i * 25);

            if (i == selectedOption) {
                g2.fillPolygon(
                        new int[]{boxX + 15, boxX + 15, boxX + 20},
                        new int[]{optY - 12, optY - 2, optY - 7},
                        3
                );
            }

            String itemName = bagItems.get(i);
            int quantity = playerTrainer.getBag().getCategories()
                    .get(bagCategories[selectedCategory]).get(itemName);
            g2.drawString(itemName + " x" + quantity, boxX + 30, optY);
        }

        drawControlsHint(g2);
    }

    //=====================================
    // DRAW POKEMON MENU (FIRE RED STYLE)
    //=====================================
    private void drawPokemonMenuFireRed(Graphics2D g2) {
        int boxX = 10;
        int boxY = gp.screenHeight - 200;
        int boxWidth = gp.screenWidth - 20;
        int boxHeight = 190;

        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("POKEMON", boxX + 15, boxY + 25);

        ArrayList<Pokemon> party = playerTrainer.getParty();
        g2.setFont(new Font("Arial", Font.PLAIN, 13));

        for (int i = 0; i < party.size() && i < 4; i++) {
            Pokemon p = party.get(i);
            int optY = boxY + 50 + (i * 35);

            if (i == selectedOption) {
                g2.fillPolygon(
                        new int[]{boxX + 15, boxX + 15, boxX + 20},
                        new int[]{optY - 12, optY - 2, optY - 7},
                        3
                );
            }

            String status = p.isFainted() ? " (FNT)" : " HP:" + p.getHp() + "/" + p.getMaxHp();
            g2.drawString(p.getName() + " Lv" + p.getLevel() + status, boxX + 30, optY);
        }

        drawControlsHint(g2);
    }

    //=====================================
    // DRAW CONTROLS HINT
    //=====================================
    private void drawControlsHint(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(100, 100, 100));
        g2.drawString("WASD: Navigate | U: Confirm | I: Back", 20, gp.screenHeight - 10);
    }

    //=====================================
    // HELPER: DRAW WRAPPED TEXT
    //=====================================
    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;

        for (String word : words) {
            String testLine = line + word + " ";
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x, lineY);
                line = new StringBuilder(word + " ");
                lineY += fm.getHeight();
            } else {
                line.append(word).append(" ");
            }
        }
        g2.drawString(line.toString(), x, lineY);
    }

    //=====================================
    // GETTERS
    //=====================================
    public BattleState getCurrentState() {
        return currentState;
    }

    public Pokemon getPlayerPokemon() {
        return playerPokemon;
    }

    public Pokemon getEnemyPokemon() {
        return enemyPokemon;
    }
}
