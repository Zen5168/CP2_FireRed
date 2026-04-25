package com.game.items;

import java.util.*;

public class Bag {

    // CATEGORY -> (ITEM NAME -> QUANTITY)
    private Map<String, Map<String, Integer>> categories;
    private Map<String, Item> itemPrototypes;

    public Bag() {
        categories = new HashMap<>();
        itemPrototypes = new HashMap<>();

        // INITIALIZE BASIC CATEGORIES
        categories.put("Potions", new HashMap<>());
        categories.put("Pokeballs", new HashMap<>());
    }

    public void addItem(Item item, int qty) {
        String category = (item instanceof Pokeball) ? "Pokeballs" : "Potions";
        String itemName = item.getName();

        // ADD ITEM TO THE SPECIFIC CATEGORY
        Map<String, Integer> contents = categories.get(category);
        contents.put(itemName, contents.getOrDefault(itemName, 0) + qty);

        itemPrototypes.putIfAbsent(itemName, item);
    }

    public void removeItem(String itemName, int qty) {
        for (Map<String, Integer> contents : categories.values()) {
            if (contents.containsKey(itemName)) {
                int currentQty = contents.get(itemName);
                if (currentQty > qty) {
                    contents.put(itemName, currentQty - qty);
                } else {
                    contents.remove(itemName);
                }
                return;
            }
        }
    }

    // ======================================
    // GETTERS
    // ======================================
    public Map<String, Map<String, Integer>> getCategories() {
        return categories;
    }

    public Item getItemObject(String name) {
        return itemPrototypes.get(name);
    }
}
