package com.game.ui;

import com.game.main.GamePanel;
import com.game.entity.NPC;
import com.game.entity.NurseJoy;
import java.awt.*;
import java.util.ArrayList;

public class DialogueManager {

    private GamePanel gp;
    private boolean dialogueActive = false;
    private NPC currentNPC;
    private ArrayList<String> dialogueQueue;
    private int currentDialogueIndex = 0;
    private String currentMessage = "";

    // FOR SPECIAL PROMPTS
    private boolean waitingForYesNo = false;
    private boolean waitingForShopMenu = false;
    private int selectedOption = 0; // 0 = YES/BUY, 1 = NO/SELL

    // DIALOGUE BOX DIMENSIONS
    private int boxX;
    private int boxY;
    private int boxWidth;
    private int boxHeight;

    public DialogueManager(GamePanel gp) {
        this.gp = gp;
        this.dialogueQueue = new ArrayList<>();

        // POSITION DIALOGUE BOX AT BOTTOM OF SCREEN
        boxX = gp.tileSize;
        boxY = gp.screenHeight - gp.tileSize * 3;
        boxWidth = gp.screenWidth - gp.tileSize * 2;
        boxHeight = gp.tileSize * 2;
    }

    public void startDialogue(NPC npc, String[] messages) {
        this.currentNPC = npc;
        this.dialogueQueue.clear();
        this.currentDialogueIndex = 0;
        this.selectedOption = 0;
        this.waitingForYesNo = false;
        this.waitingForShopMenu = false;

        for (String msg : messages) {
            dialogueQueue.add(msg);
        }

        if (!dialogueQueue.isEmpty()) {
            currentMessage = dialogueQueue.get(0);
            dialogueActive = true;
        }
    }

    public void showMessage(String message) {
        dialogueQueue.clear();
        dialogueQueue.add(message);
        currentDialogueIndex = 0;
        currentMessage = message;
        dialogueActive = true;
        waitingForYesNo = false;
        waitingForShopMenu = false;
    }

    public void handleInput(String key) {
        if (!dialogueActive) {
            return;
        }

        //========================
        // HANDLE YES/NO PROMPT
        //========================
        if (waitingForYesNo) {
            if (key.equals("A") || key.equals("D")) {
                // A / D TO TOGGLE SELECTION
                selectedOption = (selectedOption == 0) ? 1 : 0;
            } else if (key.equals("U") || key.equals("ENTER") || key.equals("J")) {
                // U / J / ENTER = CONFIRM SELECTION
                if (selectedOption == 0) {
                    // YES SELECTED
                    if (currentNPC instanceof NurseJoy) {
                        ((NurseJoy) currentNPC).healPokemon();
                        waitingForYesNo = false;
                    }
                } else {
                    // NO SELECTED
                    endDialogue();
                }
            } else if (key.equals("I") || key.equals("K")) {
                // I / K = CANCEL (SAME AS NO)
                endDialogue();
            }
            return;
        }

        //========================
        // HANDLE SHOP MENU
        //========================
        if (waitingForShopMenu) {
            if (key.equals("W") || key.equals("S") || key.equals("A") || key.equals("D")) {
                // TOGGLE SELECTION
                selectedOption = (selectedOption == 0) ? 1 : 0;
            } else if (key.equals("U") || key.equals("ENTER") | key.equals("J")) {
                // U / J / ENTER = CONFIRM SELECTION
                endDialogue();
                if (selectedOption == 0) {
                    // BUY SELECTED
                    gp.shopUI.openBuyMenu();
                } else {
                    // SELL SELECTED
                    gp.shopUI.openSellMenu();
                }
            } else if (key.equals("I") || key.equals("I")) {
                // I / K = CANCEL
                endDialogue();
            }
            return;
        }

        //======================================
        // HANDLE NORMAL DIALOGUE PROGRESSION
        //======================================
        if (key.equals("U") || key.equals("ENTER") || key.equals("SPACE") || key.equals("J")) {
            currentDialogueIndex++;

            if (currentDialogueIndex < dialogueQueue.size()) {
                currentMessage = dialogueQueue.get(currentDialogueIndex);

                // CHECK FOR SPECIAL COMMANDS
                if (currentMessage.equals("[HEAL_PROMPT]")) {
                    currentMessage = "Would you like me to heal your Pokemon?";
                    waitingForYesNo = true;
                    selectedOption = 0;
                } else if (currentMessage.equals("[OPEN_SHOP]")) {
                    // OPEN SHOP AND END DIALOGUE
                    endDialogue();
                    gp.shopUI.openShop();
                    return;
                } else if (currentMessage.equals("[SHOP_MENU]")) {
                    currentMessage = "Would you like to BUY or SELL?";
                    waitingForShopMenu = true;
                    selectedOption = 0;
                }
            } else {
                endDialogue();
            }
        } else if (key.equals("I") || key.equals("K")) {
            // CANCEL DIALOGUE
            endDialogue();
        }
    }

    public void endDialogue() {
        dialogueActive = false;
        currentNPC = null;
        dialogueQueue.clear();
        currentDialogueIndex = 0;
        waitingForYesNo = false;
        waitingForShopMenu = false;
    }

    public void draw(Graphics2D g2) {
        if (!dialogueActive) {
            return;
        }

        // DRAW DIALOGUE BOX BACKGROUND
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

        // DRAW BORDER
        g2.setColor(Color.WHITE);
        g2.setStroke(new Stroke() {
            @Override
            public Shape createStrokedShape(Shape p) {
                return new BasicStroke(3).createStrokedShape(p);
            }
        });
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

        // DRAW NPC NAME IF AVAILABLE
        if (currentNPC != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.YELLOW);
            g2.drawString(currentNPC.getName(), boxX + 20, boxY + 30);
        }

        // DRAW MESSAGE
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        drawWrappedText(g2, currentMessage, boxX + 20, boxY + 60, boxWidth - 40);

        // DRAW YES/NO OPTIONS
        if (waitingForYesNo) {
            int optionY = boxY + boxHeight - 40;
            int yesX = boxX + boxWidth - 200;
            int noX = boxX + boxWidth - 100;

            // DRAW YES
            if (selectedOption == 0) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> YES", yesX, optionY);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("YES", yesX + 15, optionY);
            }

            // DRAW NO
            if (selectedOption == 1) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> NO", noX, optionY);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("NO", noX + 15, optionY);
            }
        }

        // DRAW SHOP MENU OPTIONS
        if (waitingForShopMenu) {
            int optionY = boxY + boxHeight - 40;
            int buyX = boxX + boxWidth - 220;
            int sellX = boxX + boxWidth - 100;

            // DRAW BUY
            if (selectedOption == 0) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> BUY", buyX, optionY);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("BUY", buyX + 15, optionY);
            }

            // DRAW SELL
            if (selectedOption == 1) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> SELL", sellX, optionY);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("SELL", sellX + 15, optionY);
            }
        }

        // DRAW CONTINUE INDICATOR (IF NOT WAITING FOR INPUT)
        if (!waitingForYesNo && !waitingForShopMenu) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
        }
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            int testWidth = fm.stringWidth(testLine);

            if (testWidth > maxWidth && line.length() > 0) {
                g2.drawString(line.toString(), x, lineY);
                line = new StringBuilder(word);
                lineY += fm.getHeight();
            } else {
                line = new StringBuilder(testLine);
            }
        }

        if (line.length() > 0) {
            g2.drawString(line.toString(), x, lineY);
        }
    }

    public boolean isDialogueActive() {
        return dialogueActive;
    }
}
