package com.game.entity;

import com.game.main.GamePanel;

public class ShopClerk extends NPC {
    
    public ShopClerk(GamePanel gp, int worldX, int worldY) {
        super(gp, "Shop Clerk", "CLERK", worldX, worldY);
    }
    
    @Override
    public void interact() {
        // SHOW WELCOME MESSAGE THEN OPEN SHOP
        gp.dialogueManager.startDialogue(this, getDialogueSequence());
    }
    
    private String[] getDialogueSequence() {
        return new String[] {
            "Hi! May I help you?",
            "[SHOP_MENU]" // SPECIAL COMMAND TO SHOW BUY/SELL MENU
        };
    }
}
