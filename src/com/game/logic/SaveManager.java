package com.game.logic;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;
import com.game.items.Item;
import java.io.*;
import java.util.*;

public class SaveManager {
    
    private static final String SAVE_DIR = "src/res/save";
    private static final String SAVE_FILE = SAVE_DIR + "/pokemon_save.dat";
    private GamePanel gp;
    
    public SaveManager(GamePanel gp) {
        this.gp = gp;
        // ENSURE SAVE DIRECTORY EXISTS
        createSaveDirectory();
    }
    
    //=====================================
    // STATIC METHOD TO CHECK SAVE FILE EXISTS
    //=====================================
    public static boolean hasSaveFile() {
        return new File(SAVE_FILE).exists();
    }
    
    //=====================================
    // CREATE SAVE DIRECTORY
    //=====================================
    private void createSaveDirectory() {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            boolean created = saveDir.mkdirs();
            if (created) {
                System.out.println("Save directory created: " + SAVE_DIR);
            } else {
                System.err.println("Failed to create save directory: " + SAVE_DIR);
            }
        }
    }
    
    //=====================================
    // SAVE GAME
    //=====================================
    public boolean saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            
            // SAVE PLAYER DATA
            writer.println("[PLAYER_NAME]");
            writer.println(gp.playerTrainer.getName());
            
            writer.println("[PLAYER_POSITION]");
            writer.println(gp.player.worldX);
            writer.println(gp.player.worldY);
            writer.println(gp.player.direction);
            
            writer.println("[PLAYER_MONEY]");
            writer.println(gp.playerTrainer.getMoney());
            
            // SAVE POKEMON PARTY
            writer.println("[PARTY_SIZE]");
            writer.println(gp.playerTrainer.getParty().size());
            
            for (Pokemon pokemon : gp.playerTrainer.getParty()) {
                writer.println("[POKEMON]");
                writer.println(pokemon.getClass().getName()); // FULL CLASS NAME
                writer.println(pokemon.getLevel());
                writer.println(pokemon.getHp());
                writer.println(pokemon.getMaxHp());
                writer.println(pokemon.getAtk());
                writer.println(pokemon.getDef());
                writer.println(pokemon.getSpAtk());
                writer.println(pokemon.getSpDef());
                writer.println(pokemon.getSpeed());
                
                // SAVE MOVES
                var moves = pokemon.getMoves();
                writer.println("[MOVES]");
                for (int i = 0; i < moves.length; i++) {
                    if (moves[i] != null) {
                        writer.println(moves[i].moveName);
                        writer.println(moves[i].pp);
                    } else {
                        writer.println("NULL");
                        writer.println("0");
                    }
                }
            }
            
            // SAVE BAG ITEMS
            writer.println("[BAG]");
            var categories = gp.playerTrainer.getBag().getCategories();
            for (Map.Entry<String, Map<String, Integer>> categoryEntry : categories.entrySet()) {
                String category = categoryEntry.getKey();
                Map<String, Integer> items = categoryEntry.getValue();
                
                writer.println("[CATEGORY]");
                writer.println(category);
                writer.println(items.size());
                
                for (Map.Entry<String, Integer> itemEntry : items.entrySet()) {
                    writer.println(itemEntry.getKey()); // ITEM NAME
                    writer.println(itemEntry.getValue()); // QUANTITY
                }
            }
            
            writer.println("[END]");
            return true;
            
        } catch (IOException e) {
            System.err.println("ERROR SAVING GAME: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //=====================================
    // LOAD GAME
    //=====================================
    public boolean loadGame() {
        File saveFile = new File(SAVE_FILE);
        
        if (!saveFile.exists()) {
            System.out.println("NO SAVE FILE FOUND");
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case "[PLAYER_NAME]":
                        // NAME IS READ-ONLY
                        reader.readLine();
                        break;
                        
                    case "[PLAYER_POSITION]":
                        gp.player.worldX = Integer.parseInt(reader.readLine());
                        gp.player.worldY = Integer.parseInt(reader.readLine());
                        gp.player.direction = reader.readLine();
                        break;
                        
                    case "[PLAYER_MONEY]":
                        int money = Integer.parseInt(reader.readLine());
                        // SET MONEY (SUBTRACT CURRENT, ADD SAVED)
                        gp.playerTrainer.removeMoney(gp.playerTrainer.getMoney());
                        gp.playerTrainer.addMoney(money);
                        break;
                        
                    case "[PARTY_SIZE]":
                        int partySize = Integer.parseInt(reader.readLine());
                        
                        // CLEAR CURRENT PARTY
                        gp.playerTrainer.getParty().clear();
                        
                        // LOAD POKEMON
                        for (int i = 0; i < partySize; i++) {
                            reader.readLine(); // [POKEMON]
                            
                            String className = reader.readLine();
                            int level = Integer.parseInt(reader.readLine());
                            int hp = Integer.parseInt(reader.readLine());
                            int maxHp = Integer.parseInt(reader.readLine());
                            int atk = Integer.parseInt(reader.readLine());
                            int def = Integer.parseInt(reader.readLine());
                            int spAtk = Integer.parseInt(reader.readLine());
                            int spDef = Integer.parseInt(reader.readLine());
                            int speed = Integer.parseInt(reader.readLine());
                            
                            // CREATE POKEMON INSTANCE
                            Pokemon pokemon = createPokemonInstance(className, level);
                            
                            if (pokemon != null) {
                                // SET STATS
                                pokemon.setHp(hp);
                                // SET OTHER STATS IF NEEDED
                                
                                // LOAD MOVES
                                reader.readLine(); // [MOVES]
                                var moves = pokemon.getMoves();
                                for (int j = 0; j < moves.length; j++) {
                                    String moveName = reader.readLine();
                                    int pp = Integer.parseInt(reader.readLine());
                                    
                                    if (!moveName.equals("NULL") && moves[j] != null) {
                                        moves[j].pp = pp;
                                    }
                                }
                                
                                gp.playerTrainer.addPokemon(pokemon);
                            }
                        }
                        break;
                        
                    case "[BAG]":
                        // CLEAR CURRENT BAG
                        var categories = gp.playerTrainer.getBag().getCategories();
                        for (Map<String, Integer> items : categories.values()) {
                            items.clear();
                        }
                        
                        // LOAD BAG ITEMS
                        while ((line = reader.readLine()) != null) {
                            if (line.equals("[END]")) {
                                break;
                            }
                            
                            if (line.equals("[CATEGORY]")) {
                                String category = reader.readLine();
                                int itemCount = Integer.parseInt(reader.readLine());
                                
                                for (int i = 0; i < itemCount; i++) {
                                    String itemName = reader.readLine();
                                    int quantity = Integer.parseInt(reader.readLine());
                                    
                                    // ADD ITEMS TO BAG
                                    Item item = createItemInstance(itemName);
                                    if (item != null) {
                                        gp.playerTrainer.getBag().addItem(item, quantity);
                                    }
                                }
                            }
                        }
                        break;
                }
            }
            
            return true;
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERROR LOADING GAME: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //=============================================
    // CREATE POKEMON INSTANCE FROM CLASS NAME
    //=============================================
    private Pokemon createPokemonInstance(String className, int level) {
        try {
            Class<?> pokemonClass = Class.forName(className);
            
            // CREATE INSTANCE WITH LEVEL CONSTRUCTOR
            return (Pokemon) pokemonClass.getConstructor(int.class).newInstance(level);
            
        } catch (Exception e) {
            System.err.println("ERROR CREATING POKEMON: " + className);
            e.printStackTrace();
            return null;
        }
    }
    
    //=====================================
    // CREATE ITEM INSTANCE FROM NAME
    //=====================================
    private Item createItemInstance(String itemName) {
        try {
            // MAP ITEM NAMES TO CLASSES
            switch (itemName) {
                case "Pokeball":
                    return new com.game.items.Pokeball();
                case "Potion":
                    return new com.game.items.Potion();
                default:
                    System.err.println("UNKNOWN ITEM: " + itemName);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("ERROR CREATING ITEM: " + itemName);
            e.printStackTrace();
            return null;
        }
    }
    
    //=====================================
    // CHECK IF SAVE FILE EXISTS
    //=====================================
    public boolean saveFileExists() {
        return new File(SAVE_FILE).exists();
    }
}
