package com.game.items;

import com.game.pokemons.*;

public abstract class Item {

    protected String name;

    public Item(String name) {
        this.name = name;
    }

    public abstract void use(Pokemon target);

    public String getName() {
        return name;
    }
}
