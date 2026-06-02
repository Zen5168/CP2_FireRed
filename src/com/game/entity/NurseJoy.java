package com.game.entity;

import com.game.main.GamePanel;
import com.game.pokemons.Pokemon;

public class NurseJoy extends NPC {
    
    public NurseJoy(GamePanel gp, int worldX, int worldY) {
        super(gp, "Nurse Joy", "NURSE", worldX, worldY);
    }
    
    @Override
    public void interact() {
        // START DIALOGUE WITH HEALING OPTION
        gp.dialogueManager.startDialogue(this, getDialogueSequence());
    }
    
    private String[] getDialogueSequence() {
        return new String[] {
            "Welcome to the Pokemon Center!",
            "Would you like me to heal your Pokemon?",
            "[HEAL_PROMPT]" // SPECIAL COMMAND FOR HEALING
        };
    }
    
    public void healPokemon() {
        System.out.println("\n========================================");
        System.out.println("   Healing your Pokemon...");
        System.out.println("========================================");
        
        boolean anyHealed = false;
        for (Pokemon pokemon : gp.playerTrainer.getParty()) {
            if (pokemon.getHp() < pokemon.getMaxHp() || pokemon.isFainted()) {
                pokemon.heal(pokemon.getMaxHp());
                System.out.println(pokemon.getName() + " has been fully healed!");
                anyHealed = true;
            }
        }
        
        if (!anyHealed) {
            System.out.println("Your Pokemon are already in perfect health!");
        }
        
        // SHOW CONFIRMATION MESSAGE
        gp.dialogueManager.showMessage("Your Pokemon are now in perfect health! \nWe hope to see you again!");
    }
}
