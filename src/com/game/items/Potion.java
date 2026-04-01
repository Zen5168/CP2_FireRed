package com.game.items;

import com.game.pokemons.Pokemon;

public class Potion extends Item {

    public Potion() {
        super("Potion"); 
    }

    @Override
    public void use(Pokemon target) {
        int healAmount = 20;
        int oldHp = target.getHp();

        target.setHp(target.getHp() + healAmount);
        
        int recovered = target.getHp() - oldHp;
        System.out.println(target.getName() + " recovered " + recovered + " HP!");
    }
}