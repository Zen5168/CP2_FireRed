package com.game.ui;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import com.game.items.Item;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class OverworldMenuUI {
    
    private GamePanel gp;
    private boolean menuActive = false;
    
    // MENU STATE
    private enum MenuState {
        MAIN_MENU,      // POKEMON, BAG, SAVE, EXIT
        PARTY_VIEW,     // VIEW POKEMON PARTY WITH HEALTH
        BAG_VIEW,       // VIEW BAG INVENTORY
        PARTY_DETAIL,   // DETAILED VIEW OF SELECTED POKEMON
        BAG_ITEMS,      // VIEW ITEMS IN SELECTED CATEGORY
        ITEM_USE_TARGET // SELECT POKEMON TO USE ITEM ON
    }
    
    private MenuState currentState = MenuState.MAIN_MENU;
    private int selectedOption = 0;
    
    // MAIN MENU OPTIONS
    private String[] mainMenuOptions = {
        "POKEMON", 
        "BAG", 
        "SAVE", 
        "SAVE & EXIT"
    };
    
    // BAG STATE
    private int selectedCategory = 0;
    private int selectedItem = 0;
    private String[] bagCategories;
    private List<String> currentBagItems;
    
    // PARTY STATE
    private int selectedPokemon = 0;
    
    // ITEM USE STATE
    private String itemToUse = null;
    
    public OverworldMenuUI(GamePanel gp) {
        this.gp = gp;
    }

    //=====================================
    // MENU CONTROL
    //=====================================
    public void openMenu() {
        menuActive = true;
        currentState = MenuState.MAIN_MENU;
        selectedOption = 0;
    }
    
    public void closeMenu() {
        menuActive = false;
        currentState = MenuState.MAIN_MENU;
        selectedOption = 0;
    }
    
    public boolean isMenuActive() {
        return menuActive;
    }
    
    //=====================================
    // INPUT HANDLING
    //=====================================
    public void handleInput(String key) {
        if (!menuActive) return;
        
        switch (currentState) {
            case MAIN_MENU:
                handleMainMenuInput(key);
                break;
            case PARTY_VIEW:
                handlePartyViewInput(key);
                break;
            case BAG_VIEW:
                handleBagViewInput(key);
                break;
            case PARTY_DETAIL:
                handlePartyDetailInput(key);
                break;
            case BAG_ITEMS:
                handleBagItemsInput(key);
                break;
            case ITEM_USE_TARGET:
                handleItemUseTargetInput(key);
                break;
        }
    }
    
    //=====================================
    // MAIN MENU INPUT
    //=====================================
    private void handleMainMenuInput(String key) {
        switch (key) {
            case "W":
                if (selectedOption > 0) {
                    selectedOption--;
                }
                break;
            case "S":
                if (selectedOption < mainMenuOptions.length - 1) {
                    selectedOption++;
                }
                break;
            case "U":
            case "J":
            case "ENTER":
                selectMainMenuOption();
                break;
            case "I":
            case "K":
            case "ESCAPE":
                closeMenu();
                break;
        }
    }
    
    private void selectMainMenuOption() {
        switch (selectedOption) {
            case 0: // POKEMON
                openPartyView();
                break;
            case 1: // BAG
                openBagView();
                break;
            case 2: // SAVE
                saveGame();
                break;
            case 3: // SAVE & EXIT
                saveAndExit();
                break;
        }
    }
    
    //=====================================
    // PARTY VIEW
    //=====================================
    private void openPartyView() {
        currentState = MenuState.PARTY_VIEW;
        selectedPokemon = 0;
    }
    
    private void handlePartyViewInput(String key) {
        int partySize = gp.playerTrainer.getParty().size();
        
        switch (key) {
            case "W":
                if (selectedPokemon > 0) {
                    selectedPokemon--;
                }
                break;
            case "S":
                if (selectedPokemon < partySize - 1) {
                    selectedPokemon++;
                }
                break;
            case "U":
            case "J":
            case "ENTER":
                openPartyDetail();
                break;
            case "I":
            case "K":
            case "ESCAPE":
                currentState = MenuState.MAIN_MENU;
                selectedOption = 0; // RETURN TO POKEMON OPTION
                break;
        }
    }
    
    private void openPartyDetail() {
        currentState = MenuState.PARTY_DETAIL;
    }
    
    private void handlePartyDetailInput(String key) {
        if (key.equals("I") || key.equals("K") || key.equals("ESCAPE") || 
            key.equals("U") || key.equals("J") || key.equals("ENTER")) {
            currentState = MenuState.PARTY_VIEW;
        }
    }
    
    //=====================================
    // BAG VIEW
    //=====================================
    private void openBagView() {
        currentState = MenuState.BAG_VIEW;
        var categories = gp.playerTrainer.getBag().getCategories();
        bagCategories = categories.keySet().toArray(new String[0]);
        selectedCategory = 0;
    }
    
    private void handleBagViewInput(String key) {
        switch (key) {
            case "A":
                if (selectedCategory > 0) {
                    selectedCategory--;
                }
                break;
            case "D":
                if (selectedCategory < bagCategories.length - 1) {
                    selectedCategory++;
                }
                break;
            case "U":
            case "J":
            case "ENTER":
                openBagItems();
                break;
            case "I":
            case "K":
            case "ESCAPE":
                currentState = MenuState.MAIN_MENU;
                selectedOption = 1; // RETURN TO BAG OPTION
                break;
        }
    }
    
    private void openBagItems() {
        String category = bagCategories[selectedCategory];
        var items = gp.playerTrainer.getBag().getCategories().get(category);
        currentBagItems = new ArrayList<>(items.keySet());
        
        if (currentBagItems.isEmpty()) {
            // STAY IN BAG VIEW IF EMPTY
            return;
        }
        
        selectedItem = 0;
        currentState = MenuState.BAG_ITEMS;
    }
    
    private void handleBagItemsInput(String key) {
        switch (key) {
            case "W":
                if (selectedItem > 0) {
                    selectedItem--;
                }
                break;
            case "S":
                if (selectedItem < currentBagItems.size() - 1) {
                    selectedItem++;
                }
                break;
            case "U":
            case "J":
            case "ENTER":
                useSelectedItem();
                break;
            case "I":
            case "K":
            case "ESCAPE":
                currentState = MenuState.BAG_VIEW;
                break;
        }
    }
    
    //=====================================
    // SAVE GAME
    //=====================================
    private void saveGame() {
        // CHECK IF PLAYER IS INSIDE A BUILDING
        if (gp.buildingManager.isInBuilding()) {
            gp.dialogueManager.showMessage("You can't save inside a building!");
            closeMenu();
            return;
        }
        
        // CREATE SAVE MANAGER AND SAVE
        com.game.logic.SaveManager saveManager = new com.game.logic.SaveManager(gp);
        boolean success = saveManager.saveGame();
        
        if (success) {
            gp.dialogueManager.showMessage("Game saved successfully!");
        } else {
            gp.dialogueManager.showMessage("Failed to save game!");
        }
        closeMenu();
    }
    
    //=====================================
    // SAVE AND EXIT
    //=====================================
    private void saveAndExit() {
        // CHECK IF PLAYER IS INSIDE A BUILDING
        if (gp.buildingManager.isInBuilding()) {
            gp.dialogueManager.showMessage("You can't save inside a building! Exit the building first.");
            closeMenu();
            return;
        }
        
        // CREATE SAVE MANAGER AND SAVE
        com.game.logic.SaveManager saveManager = new com.game.logic.SaveManager(gp);
        boolean success = saveManager.saveGame();
        
        if (success) {
            // SHOW SAVING MESSAGE BRIEFLY
            gp.dialogueManager.showMessage("Saving game...");
            
            // CLOSE THE GAME AFTER A SHORT DELAY
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // WAIT 1 SECOND TO SHOW MESSAGE
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Game saved. Exiting...");
                System.exit(0); // EXIT THE APPLICATION
            }).start();
        } else {
            gp.dialogueManager.showMessage("Failed to save game! Exit cancelled.");
            closeMenu();
        }
    }
    
    //=====================================
    // ITEM USAGE
    //=====================================
    private void useSelectedItem() {
        String itemName = currentBagItems.get(selectedItem);
        Item item = gp.playerTrainer.getBag().getItemObject(itemName);
        
        // CHECK IF ITEM CAN BE USED (ONLY POTIONS IN OVERWORLD)
        if (item instanceof com.game.items.Potion) {
            // OPEN POKEMON SELECTION FOR TARGET
            itemToUse = itemName;
            selectedPokemon = 0;
            currentState = MenuState.ITEM_USE_TARGET;
        } else {
            // POKEBALLS CAN'T BE USED OUTSIDE OF BATTLE
            gp.dialogueManager.showMessage("This item can only be used in battle!");
        }
    }
    
    private void handleItemUseTargetInput(String key) {
        int partySize = gp.playerTrainer.getParty().size();
        
        switch (key) {
            case "W":
                if (selectedPokemon > 0) {
                    selectedPokemon--;
                }
                break;
            case "S":
                if (selectedPokemon < partySize - 1) {
                    selectedPokemon++;
                }
                break;
            case "U":
            case "J":
            case "ENTER":
                useItemOnPokemon();
                break;
            case "I":
            case "K":
            case "ESCAPE":
                currentState = MenuState.BAG_ITEMS;
                itemToUse = null;
                break;
        }
    }
    
    private void useItemOnPokemon() {
        Pokemon targetPokemon = gp.playerTrainer.getParty().get(selectedPokemon);
        Item item = gp.playerTrainer.getBag().getItemObject(itemToUse);
        
        // CHECK IF POKEMON CAN USE THE ITEM
        if (item instanceof com.game.items.Potion) {
            if (targetPokemon.getHp() >= targetPokemon.getMaxHp()) {
                gp.dialogueManager.showMessage(targetPokemon.getName() + "'s HP is already full!");
                return;
            }
            
            if (targetPokemon.isFainted()) {
                gp.dialogueManager.showMessage("This won't work on a fainted Pokemon!");
                return;
            }
            
            // USE THE ITEM
            int oldHp = targetPokemon.getHp();
            item.use(targetPokemon);
            int recovered = targetPokemon.getHp() - oldHp;
            
            // REMOVE ITEM FROM BAG
            gp.playerTrainer.getBag().removeItem(itemToUse, 1);
            
            // SHOW MESSAGE
            gp.dialogueManager.showMessage(targetPokemon.getName() + " recovered " + recovered + " HP!");
            
            // CLOSE MENU AND REFRESH BAG
            itemToUse = null;
            openBagView(); // REFRESH BAG TO UPDATE QUANTITIES
        }
    }
    
    //=====================================
    // DRAW
    //=====================================
    public void draw(Graphics2D g2) {
        if (!menuActive) return;
        
        switch (currentState) {
            case MAIN_MENU:
                drawMainMenu(g2);
                break;
            case PARTY_VIEW:
                drawPartyView(g2);
                break;
            case BAG_VIEW:
                drawBagView(g2);
                break;
            case PARTY_DETAIL:
                drawPartyDetail(g2);
                break;
            case BAG_ITEMS:
                drawBagItems(g2);
                break;
            case ITEM_USE_TARGET:
                drawItemUseTarget(g2);
                break;
        }
    }
    
    //=====================================
    // DRAW MAIN MENU
    //=====================================
    private void drawMainMenu(Graphics2D g2) {
        // SEMI-TRANSPARENT OVERLAY
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // MENU BOX
        int boxWidth = 300;
        int boxHeight = 270;
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        // OUTER BORDER (DARK BLUE)
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);
        
        // INNER BOX (WHITE BACKGROUND)
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // TITLE
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(32, 56, 136));
        String title = "MENU";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 40);
        
        // MENU OPTIONS
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        int optionY = boxY + 80;
        int optionSpacing = 45;
        
        for (int i = 0; i < mainMenuOptions.length; i++) {
            if (i == selectedOption) {
                // HIGHLIGHT SELECTED OPTION
                g2.setColor(new Color(255, 200, 0));
                g2.fillRoundRect(boxX + 20, optionY + i * optionSpacing - 25, boxWidth - 40, 35, 5, 5);
                g2.setColor(new Color(32, 56, 136));
                g2.drawString("> " + mainMenuOptions[i], boxX + 35, optionY + i * optionSpacing);
            } else {
                g2.setColor(new Color(88, 88, 88));
                g2.drawString(mainMenuOptions[i], boxX + 50, optionY + i * optionSpacing);
            }
        }
    }
    
    //=====================================
    // DRAW PARTY VIEW
    //=====================================
    private void drawPartyView(Graphics2D g2) {
        // SEMI-TRANSPARENT OVERLAY
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // PARTY BOX
        int boxWidth = 650;
        int boxHeight = 500;
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // TITLE
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(32, 56, 136));
        String title = "POKEMON PARTY";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 40);
        
        // DRAW POKEMON CARDS
        List<Pokemon> party = gp.playerTrainer.getParty();
        int cardY = boxY + 70;
        int cardSpacing = 70;
        
        for (int i = 0; i < party.size(); i++) {
            Pokemon p = party.get(i);
            drawPokemonCard(g2, p, boxX + 20, cardY + i * cardSpacing, boxWidth - 40, 60, i == selectedPokemon);
        }
    }
    
    //=====================================
    // DRAW POKEMON CARD
    //=====================================
    private void drawPokemonCard(Graphics2D g2, Pokemon pokemon, int x, int y, int width, int height, boolean selected) {
        // CARD BACKGROUND
        if (selected) {
            g2.setColor(new Color(255, 220, 100));
            g2.fillRoundRect(x - 3, y - 3, width + 6, height + 6, 10, 10);
        }
        
        // CARD BODY
        if (pokemon.isFainted()) {
            g2.setColor(new Color(200, 200, 200));
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.fillRoundRect(x, y, width, height, 10, 10);
        
        // CARD BORDER
        g2.setColor(new Color(100, 100, 100));
        g2.drawRoundRect(x, y, width, height, 10, 10);
        
        // POKEMON SPRITE
        BufferedImage sprite = gp.battleScreen.getPokemonSprite(pokemon.getName(), false);
        if (sprite != null) {
            int spriteSize = 50;
            g2.drawImage(sprite, x + 10, y + 5, spriteSize, spriteSize, null);
        }
        
        // POKEMON NAME
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(pokemon.isFainted() ? new Color(150, 0, 0) : new Color(32, 56, 136));
        g2.drawString(pokemon.getName(), x + 75, y + 20);
        
        // LEVEL
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(88, 88, 88));
        g2.drawString("Lv. " + pokemon.getLevel(), x + 75, y + 38);
        
        // HP TEXT
        int hpTextX = x + 200;
        int hpTextY = y + 25;
        
        // HP LABEL
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(88, 88, 88));
        g2.drawString("HP:", hpTextX, hpTextY);
        
        // HP VALUES WITH COLOR CODING
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        float hpPercentage = (float) pokemon.getHp() / pokemon.getMaxHp();
        
        if (hpPercentage > 0.5f) {
            g2.setColor(new Color(0, 150, 0));
        } else if (hpPercentage > 0.25f) {
            g2.setColor(new Color(200, 150, 0));
        } else {
            g2.setColor(new Color(200, 0, 0));
        }
        
        String hpText = pokemon.getHp() + " / " + pokemon.getMaxHp();
        g2.drawString(hpText, hpTextX + 35, hpTextY);
        
        // STATUS (FAINTED)
        if (pokemon.isFainted()) {
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(new Color(200, 0, 0));
            g2.drawString("FAINTED", x + 200, y + 50);
        }
    }
    
    //=====================================
    // DRAW PARTY DETAIL
    //=====================================
    private void drawPartyDetail(Graphics2D g2) {
        Pokemon pokemon = gp.playerTrainer.getParty().get(selectedPokemon);
        
        // SEMI-TRANSPARENT OVERLAY
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // DETAIL BOX
        int boxWidth = 600;
        int boxHeight = 500;
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // POKEMON SPRITE
        BufferedImage sprite = gp.battleScreen.getPokemonSprite(pokemon.getName(), false);
        if (sprite != null) {
            int spriteSize = 150;
            g2.drawImage(sprite, boxX + 30, boxY + 30, spriteSize, spriteSize, null);
        }
        
        // POKEMON NAME
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(new Color(32, 56, 136));
        g2.drawString(pokemon.getName(), boxX + 220, boxY + 50);
        
        // LEVEL
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(new Color(88, 88, 88));
        g2.drawString("Level: " + pokemon.getLevel(), boxX + 220, boxY + 80);
        
        // TYPE
        g2.drawString("Type: " + pokemon.getType1().toString(), boxX + 220, boxY + 110);
        if (pokemon.getType2() != null) {
            g2.drawString("/ " + pokemon.getType2().toString(), boxX + 220, boxY + 135);
        }
        
        // HP BAR
        int hpBarX = boxX + 30;
        int hpBarY = boxY + 200;
        int hpBarWidth = 540;
        int hpBarHeight = 30;
        
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.BLACK);
        g2.drawString("HP", hpBarX, hpBarY - 5);
        
        // HP BAR BACKGROUND
        g2.setColor(new Color(200, 200, 200));
        g2.fillRoundRect(hpBarX + 40, hpBarY - 20, hpBarWidth - 40, hpBarHeight, 10, 10);
        
        // HP BAR FILL
        float hpPercentage = (float) pokemon.getHp() / pokemon.getMaxHp();
        int fillWidth = (int) ((hpBarWidth - 40) * hpPercentage);
        
        if (hpPercentage > 0.5f) {
            g2.setColor(new Color(0, 200, 0));
        } else if (hpPercentage > 0.25f) {
            g2.setColor(new Color(255, 200, 0));
        } else {
            g2.setColor(new Color(255, 0, 0));
        }
        g2.fillRoundRect(hpBarX + 40, hpBarY - 20, fillWidth, hpBarHeight, 10, 10);
        
        // HP TEXT
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.BLACK);
        String hpText = pokemon.getHp() + " / " + pokemon.getMaxHp();
        g2.drawString(hpText, hpBarX + 40, hpBarY + 30);
        
        // STATS
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(32, 56, 136));
        g2.drawString("STATS", boxX + 30, boxY + 270);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(Color.BLACK);
        int statX = boxX + 30;
        int statY = boxY + 300;
        int statSpacing = 30;
        
        g2.drawString("Attack:     " + pokemon.getAtk(), statX, statY);
        g2.drawString("Defense:    " + pokemon.getDef(), statX, statY + statSpacing);
        g2.drawString("Sp. Attack: " + pokemon.getSpAtk(), statX, statY + statSpacing * 2);
        g2.drawString("Sp. Defense:" + pokemon.getSpDef(), statX, statY + statSpacing * 3);
        g2.drawString("Speed:      " + pokemon.getSpeed(), statX, statY + statSpacing * 4);
        
        // MOVES
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(32, 56, 136));
        g2.drawString("MOVES", boxX + 320, boxY + 270);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(Color.BLACK);
        int moveX = boxX + 320;
        int moveY = boxY + 300;
        
        var moves = pokemon.getMoves();
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] != null) {
                g2.drawString((i + 1) + ". " + moves[i].moveName, moveX, moveY + i * statSpacing);
            }
        }
    }
    
    //=====================================
    // DRAW BAG VIEW
    //=====================================
    private void drawBagView(Graphics2D g2) {
        // SEMI-TRANSPARENT OVERLAY
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // BAG BOX
        int boxWidth = 600;
        int boxHeight = 400;
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // TITLE
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(32, 56, 136));
        String title = "BAG";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 40);
        
        // CATEGORY TABS
        int tabWidth = boxWidth / bagCategories.length;
        int tabHeight = 40;
        int tabY = boxY + 60;
        
        for (int i = 0; i < bagCategories.length; i++) {
            int tabX = boxX + i * tabWidth;
            
            if (i == selectedCategory) {
                // SELECTED TAB
                g2.setColor(new Color(255, 200, 0));
                g2.fillRect(tabX, tabY, tabWidth, tabHeight);
                g2.setColor(new Color(32, 56, 136));
            } else {
                // UNSELECTED TAB
                g2.setColor(new Color(200, 200, 200));
                g2.fillRect(tabX, tabY, tabWidth, tabHeight);
                g2.setColor(new Color(88, 88, 88));
            }
            
            // TAB BORDER
            g2.setColor(new Color(100, 100, 100));
            g2.drawRect(tabX, tabY, tabWidth, tabHeight);
            
            // TAB TEXT
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            if (i == selectedCategory) {
                g2.setColor(new Color(32, 56, 136));
            } else {
                g2.setColor(new Color(88, 88, 88));
            }
            String categoryName = bagCategories[i];
            int textWidth = g2.getFontMetrics().stringWidth(categoryName);
            g2.drawString(categoryName, tabX + (tabWidth - textWidth) / 2, tabY + 25);
        }
        
        // CATEGORY CONTENT AREA
        int contentY = tabY + tabHeight + 20;
        
        // GET ITEMS IN SELECTED CATEGORY
        String category = bagCategories[selectedCategory];
        var items = gp.playerTrainer.getBag().getCategories().get(category);
        
        if (items.isEmpty()) {
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.setColor(new Color(150, 150, 150));
            String emptyText = "This pocket is empty.";
            int emptyWidth = g2.getFontMetrics().stringWidth(emptyText);
            g2.drawString(emptyText, boxX + (boxWidth - emptyWidth) / 2, contentY + 100);
        } else {
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            g2.setColor(Color.BLACK);
            
            int itemY = contentY;
            int itemNum = 1;
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                String itemText = itemNum + ". " + entry.getKey() + " x" + entry.getValue();
                g2.drawString(itemText, boxX + 30, itemY);
                itemY += 30;
                itemNum++;
            }
        }
    }
    
    //=====================================
    // DRAW BAG ITEMS
    //=====================================
    private void drawBagItems(Graphics2D g2) {
        // SEMI-TRANSPARENT OVERLAY
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // ITEMS BOX
        int boxWidth = 500;
        int boxHeight = 450;
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // TITLE
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(32, 56, 136));
        String title = bagCategories[selectedCategory];
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 35);
        
        // ITEM LIST
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        int itemY = boxY + 70;
        int itemSpacing = 40;
        
        String category = bagCategories[selectedCategory];
        var items = gp.playerTrainer.getBag().getCategories().get(category);
        
        for (int i = 0; i < currentBagItems.size(); i++) {
            String itemName = currentBagItems.get(i);
            int quantity = items.get(itemName);
            
            if (i == selectedItem) {
                // HIGHLIGHT SELECTED ITEM
                g2.setColor(new Color(255, 220, 100));
                g2.fillRoundRect(boxX + 15, itemY + i * itemSpacing - 25, boxWidth - 30, 35, 5, 5);
                g2.setColor(new Color(32, 56, 136));
                g2.drawString("> " + itemName, boxX + 30, itemY + i * itemSpacing);
            } else {
                g2.setColor(new Color(88, 88, 88));
                g2.drawString(itemName, boxX + 45, itemY + i * itemSpacing);
            }
            
            // QUANTITY
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(new Color(88, 88, 88));
            String qtyText = "x" + quantity;
            int qtyWidth = g2.getFontMetrics().stringWidth(qtyText);
            g2.drawString(qtyText, boxX + boxWidth - qtyWidth - 30, itemY + i * itemSpacing);
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
        }
        
        // ITEM DESCRIPTION (IF SELECTED)
        if (currentBagItems.size() > 0) {
            String selectedItemName = currentBagItems.get(selectedItem);
            Item item = gp.playerTrainer.getBag().getItemObject(selectedItemName);
            
            if (item != null) {
                int descY = boxY + boxHeight - 100;
                g2.setColor(new Color(200, 200, 200));
                g2.fillRoundRect(boxX + 20, descY, boxWidth - 40, 70, 10, 10);
                
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.setColor(Color.BLACK);
                g2.drawString("Description:", boxX + 30, descY + 20);
                g2.drawString(item.getDescription(), boxX + 30, descY + 45);
            }
        }
    }
    
    //=====================================
    // DRAW ITEM USE TARGET SELECTION
    //=====================================
    private void drawItemUseTarget(Graphics2D g2) {
        // SEMI-TRANSPARENT OVERLAY
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // SELECTION BOX
        int boxWidth = 650;
        int boxHeight = 500;
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // TITLE
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(32, 56, 136));
        String title = "Use " + itemToUse + " on which Pokemon?";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        
        // IF TITLE IS TOO LONG, USE SMALLER FONT
        if (titleWidth > boxWidth - 40) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            titleWidth = g2.getFontMetrics().stringWidth(title);
        }
        
        g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 40);
        
        // DRAW POKEMON CARDS
        List<Pokemon> party = gp.playerTrainer.getParty();
        int cardY = boxY + 70;
        int cardSpacing = 70;
        
        for (int i = 0; i < party.size(); i++) {
            Pokemon p = party.get(i);
            drawPokemonCard(g2, p, boxX + 20, cardY + i * cardSpacing, boxWidth - 40, 60, i == selectedPokemon);
        }
    }
}
