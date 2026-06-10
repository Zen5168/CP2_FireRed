package com.game.items;

import com.game.pokemons.*;

public abstract class Item {

    protected String name;
    protected String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void use(Pokemon target);

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description != null ? description : "No description available.";
    }
}
