package com.game.ui;

import com.game.main.GamePanel;
import com.game.items.*;
import java.awt.*;
import java.util.*;

public class ShopUI {
    
    private GamePanel gp;
    private boolean shopActive = false;
    private boolean sellMode = false;
    private int selectedItemIndex = 0;
    private int quantity = 1;
    private boolean selectingQuantity = false;
    
    // SHOP INVENTORY WITH PRICES
    private ArrayList<ShopItem> shopInventory;
    private ArrayList<ShopItem> playerInventory;
    
    public ShopUI(GamePanel gp) {
        this.gp = gp;
        
        //========================
        // INITIALIZE SHOP INVENTORY
        //========================
        shopInventory = new ArrayList<>();
        shopInventory.add(new ShopItem("Pokeball", 200, new Pokeball()));
        shopInventory.add(new ShopItem("Potion", 300, new Potion()));
    }
    
    public void openShop() {
        shopActive = true;
        sellMode = false;
        selectedItemIndex = 0;
        quantity = 1;
        selectingQuantity = false;
    }
    
    public void openSellMenu() {
        shopActive = true;
        sellMode = true;
        selectedItemIndex = 0;
        quantity = 1;
        selectingQuantity = false;
        updatePlayerInventory();
    }
    
    public void openBuyMenu() {
        shopActive = true;
        sellMode = false;
        selectedItemIndex = 0;
        quantity = 1;
        selectingQuantity = false;
    }
    
    private void updatePlayerInventory() {
        playerInventory = new ArrayList<>();
        Map<String, Map<String, Integer>> categories = gp.playerTrainer.getBag().getCategories();
        
        for (Map.Entry<String, Map<String, Integer>> category : categories.entrySet()) {
            for (Map.Entry<String, Integer> itemEntry : category.getValue().entrySet()) {
                String itemName = itemEntry.getKey();
                int qty = itemEntry.getValue();
                Item itemObj = gp.playerTrainer.getBag().getItemObject(itemName);
                
                // PRICE WHEN SELLING IS 50% OF BUYING PRICE
                int sellPrice = getItemBuyPrice(itemName) / 2;
                playerInventory.add(new ShopItem(itemName, sellPrice, itemObj, qty));
            }
        }
    }
    
    private int getItemBuyPrice(String itemName) {
        for (ShopItem item : shopInventory) {
            if (item.name.equals(itemName)) {
                return item.price;
            }
        }
        return 100; // DEFAULT PRICE
    }
    
    public void handleInput(String key) {
        if (!shopActive) return;
        
        ArrayList<ShopItem> currentInventory = sellMode ? playerInventory : shopInventory;
        
        if (selectingQuantity) {
            // GET MAX QUANTITY BASED ON MODE
            int maxQuantity = 99; // DEFAULT FOR BUYING
            if (sellMode && !currentInventory.isEmpty()) {
                // WHEN SELLING, LIMIT TO WHAT PLAYER OWNS
                maxQuantity = currentInventory.get(selectedItemIndex).currentQuantity;
            }
            
            // HANDLE QUANTITY SELECTION
            if (key.equals("W")) {
                // W = INCREASE QUANTITY BY 1
                quantity = Math.min(quantity + 1, maxQuantity);
            } else if (key.equals("S")) {
                // S = DECREASE QUANTITY BY 1
                quantity = Math.max(quantity - 1, 1);
            } else if (key.equals("D")) {
                // D = INCREASE QUANTITY BY 10
                quantity = Math.min(quantity + 10, maxQuantity);
            } else if (key.equals("A")) {
                // A = DECREASE QUANTITY BY 10
                quantity = Math.max(quantity - 10, 1);
            } else if (key.equals("U") || key.equals("ENTER") || key.equals("J")) {
                // U / J / ENTER = CONFIRM PURCHASE
                confirmTransaction(currentInventory.get(selectedItemIndex));
                selectingQuantity = false;
                quantity = 1;
            } else if (key.equals("I") || key.equals("K")) {
                // I / K = CANCEL QUANTITY SELECTION (BACK TO ITEM LIST)
                selectingQuantity = false;
                quantity = 1;
            }
        } else {
            // HANDLE ITEM SELECTION
            if (key.equals("W") || key.equals("A")) {
                // W or A = PREVIOUS ITEM
                selectedItemIndex = Math.max(0, selectedItemIndex - 1);
            } else if (key.equals("S") || key.equals("D")) {
                // S / D = NEXT ITEM
                selectedItemIndex = Math.min(currentInventory.size() - 1, selectedItemIndex + 1);
            } else if (key.equals("U") || key.equals("ENTER") || key.equals("J")) {
                // U /J / ENTER = SELECT ITEM FOR TRANSACTION
                if (!currentInventory.isEmpty()) {
                    selectingQuantity = true;
                    quantity = 1;
                }
            } else if (key.equals("I") || key.equals("K")) {
                // I / K = BACK/EXIT SHOP
                closeShop();
            }
        }
    }
    
    private void confirmTransaction(ShopItem item) {
        int totalPrice = item.price * quantity;
        
        if (sellMode) {
            //=================
            // SELLING ITEMS
            //=================
            if (item.currentQuantity >= quantity) {
                gp.playerTrainer.getBag().removeItem(item.name, quantity);
                gp.playerTrainer.addMoney(totalPrice);
                System.out.println("Sold " + quantity + "x " + item.name + " for $" + totalPrice);
                gp.dialogueManager.showMessage("Sold " + quantity + "x " + item.name + " for $" + totalPrice + "!");
                updatePlayerInventory();
                if (playerInventory.isEmpty()) {
                    closeShop();
                } else {
                    selectedItemIndex = Math.min(selectedItemIndex, playerInventory.size() - 1);
                }
            } else {
                gp.dialogueManager.showMessage("You don't have enough " + item.name + "!");
            }
        } else {
            //=================
            // BUYING ITEMS
            //=================
            if (gp.playerTrainer.hasMoney(totalPrice)) {
                gp.playerTrainer.removeMoney(totalPrice);
                gp.playerTrainer.getBag().addItem(item.itemObject, quantity);
                System.out.println("Bought " + quantity + "x " + item.name + " for $" + totalPrice);
                gp.dialogueManager.showMessage("Bought " + quantity + "x " + item.name + " for $" + totalPrice + "!");
            } else {
                gp.dialogueManager.showMessage("Not enough money! You need $" + totalPrice + ".");
            }
        }
    }
    
    public void closeShop() {
        shopActive = false;
        sellMode = false;
        selectedItemIndex = 0;
        quantity = 1;
        selectingQuantity = false;
    }
    
    
    public void draw(Graphics2D g2) {
        if (!shopActive) return;
        
        ArrayList<ShopItem> currentInventory = sellMode ? playerInventory : shopInventory;
        
        int padding = 20;
        
        //=============================
        // MONEY BOX (TOP RIGHT)
        //=============================
        int moneyBoxWidth = 200;
        int moneyBoxHeight = 70;
        int moneyBoxX = gp.screenWidth - moneyBoxWidth - padding;
        int moneyBoxY = padding;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(moneyBoxX - 4, moneyBoxY - 4, moneyBoxWidth + 8, moneyBoxHeight + 8, 8, 8);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(moneyBoxX, moneyBoxY, moneyBoxWidth, moneyBoxHeight, 8, 8);
        
        // MONEY TEXT
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(88, 88, 88));
        g2.drawString("MONEY", moneyBoxX + 15, moneyBoxY + 25);
        
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String moneyText = "$" + gp.playerTrainer.getMoney();
        int moneyTextWidth = g2.getFontMetrics().stringWidth(moneyText);
        g2.drawString(moneyText, moneyBoxX + moneyBoxWidth - moneyTextWidth - 15, moneyBoxY + 55);
        
        //=============================
        // ITEM LIST BOX (LEFT)
        //=============================
        int itemBoxWidth = 350;
        int itemBoxHeight = 400;
        int itemBoxX = padding;
        int itemBoxY = padding;
        
        // OUTER BORDER
        g2.setColor(new Color(32, 56, 136));
        g2.fillRoundRect(itemBoxX - 4, itemBoxY - 4, itemBoxWidth + 8, itemBoxHeight + 8, 8, 8);
        
        // INNER BOX
        g2.setColor(new Color(248, 248, 248));
        g2.fillRoundRect(itemBoxX, itemBoxY, itemBoxWidth, itemBoxHeight, 8, 8);
        
        // TITLE
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(88, 88, 88));
        String title = sellMode ? "SELL" : "BUY";
        g2.drawString(title, itemBoxX + 15, itemBoxY + 25);
        
        // DRAW ITEMS
        int itemY = itemBoxY + 55;
        int itemHeight = 35;
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        
        // CALCULATE VISIBLE ITEMS
        int maxVisibleItems = 8;
        int scrollOffset = Math.max(0, selectedItemIndex - maxVisibleItems + 1);
        int endIndex = Math.min(currentInventory.size(), scrollOffset + maxVisibleItems);
        
        for (int i = scrollOffset; i < endIndex; i++) {
            ShopItem item = currentInventory.get(i);
            
            // DRAW CURSOR FOR SELECTED ITEM
            if (i == selectedItemIndex) {
                g2.setColor(new Color(248, 56, 0));
                int[] xPoints = {itemBoxX + 10, itemBoxX + 20, itemBoxX + 10};
                int[] yPoints = {itemY - 15, itemY - 8, itemY - 1};
                g2.fillPolygon(xPoints, yPoints, 3);
            }
            
            // DRAW ITEM NAME
            g2.setColor(new Color(88, 88, 88));
            g2.drawString(item.name, itemBoxX + 35, itemY);
            
            // DRAW "OWN: X" FOR SELLING
            if (sellMode) {
                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                String ownText = "x" + item.currentQuantity;
                int ownWidth = g2.getFontMetrics().stringWidth(ownText);
                g2.drawString(ownText, itemBoxX + itemBoxWidth - ownWidth - 15, itemY);
                g2.setFont(new Font("Arial", Font.PLAIN, 18));
            }
            
            itemY += itemHeight;
        }
        
        //===============================
        // QUANTITY / INFO BOX (Bottom Right)
        //===============================
        if (selectingQuantity && !currentInventory.isEmpty()) {
            ShopItem selectedItem = currentInventory.get(selectedItemIndex);
            
            int infoBoxWidth = 350;
            int infoBoxHeight = 150;
            int infoBoxX = gp.screenWidth - infoBoxWidth - padding;
            int infoBoxY = moneyBoxY + moneyBoxHeight + 20; // POSITION BELOW MONEY BOX
            
            // OUTER BORDER
            g2.setColor(new Color(32, 56, 136));
            g2.fillRoundRect(infoBoxX - 4, infoBoxY - 4, infoBoxWidth + 8, infoBoxHeight + 8, 8, 8);
            
            // INNER BOX
            g2.setColor(new Color(248, 248, 248));
            g2.fillRoundRect(infoBoxX, infoBoxY, infoBoxWidth, infoBoxHeight, 8, 8);
            
            // ITEM NAME WITH QUANTITY
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(88, 88, 88));
            g2.drawString(selectedItem.name + " x" + String.format("%02d", quantity), infoBoxX + 15, infoBoxY + 35);
            
            // PRICE PER UNIT
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.drawString("Price: $" + selectedItem.price, infoBoxX + 15, infoBoxY + 65);
            
            // TOTAL PRICE
            int totalPrice = selectedItem.price * quantity;
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Total:", infoBoxX + 15, infoBoxY + 100);
            String totalText = "$" + totalPrice;
            int totalWidth = g2.getFontMetrics().stringWidth(totalText);
            g2.drawString(totalText, infoBoxX + infoBoxWidth - totalWidth - 15, infoBoxY + 100);
            
            // CONTROLS HINT
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(new Color(128, 128, 128));
        } else if (!currentInventory.isEmpty() && selectedItemIndex >= 0) {
            // SHOW ITEM DESCRIPTION BOX
            ShopItem selectedItem = currentInventory.get(selectedItemIndex);
            
            int infoBoxWidth = 350;
            int infoBoxHeight = 120;
            int infoBoxX = gp.screenWidth - infoBoxWidth - padding;
            int infoBoxY = moneyBoxY + moneyBoxHeight + 20; // POSITION BELOW MONEY BOX
            
            // OUTER BORDER
            g2.setColor(new Color(32, 56, 136));
            g2.fillRoundRect(infoBoxX - 4, infoBoxY - 4, infoBoxWidth + 8, infoBoxHeight + 8, 8, 8);
            
            // INNER BOX
            g2.setColor(new Color(248, 248, 248));
            g2.fillRoundRect(infoBoxX, infoBoxY, infoBoxWidth, infoBoxHeight, 8, 8);
            
            // ITEM INFO
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(88, 88, 88));
            g2.drawString(selectedItem.name, infoBoxX + 15, infoBoxY + 35);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            String priceLabel = sellMode ? "Sell for:" : "Price:";
            g2.drawString(priceLabel + " $" + selectedItem.price, infoBoxX + 15, infoBoxY + 65);
            
            // CONTROLS HINT
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(new Color(128, 128, 128));
        }
        
        // DRAW EMPTY INVENTORY MESSAGE
        if (currentInventory.isEmpty() && sellMode) {
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.setColor(new Color(128, 128, 128));
            String emptyMsg = "You have no items to sell!";
            int msgWidth = g2.getFontMetrics().stringWidth(emptyMsg);
            g2.drawString(emptyMsg, itemBoxX + (itemBoxWidth - msgWidth) / 2, itemBoxY + itemBoxHeight / 2);
        }
    }
    
    public boolean isShopActive() {
        return shopActive;
    }
    
    // INNER CLASS TO REPRESENT SHOP ITEMS
    private class ShopItem {
        String name;
        int price;
        Item itemObject;
        int currentQuantity; // FOR SELLING
        
        ShopItem(String name, int price, Item itemObject) {
            this.name = name;
            this.price = price;
            this.itemObject = itemObject;
            this.currentQuantity = 0;
        }
        
        ShopItem(String name, int price, Item itemObject, int currentQuantity) {
            this.name = name;
            this.price = price;
            this.itemObject = itemObject;
            this.currentQuantity = currentQuantity;
        }
    }
}
