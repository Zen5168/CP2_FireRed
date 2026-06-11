package com.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.game.logic.SaveManager;

public class MainMenu extends JDialog {

    private static final int MENU_WIDTH = 600;
    private static final int MENU_HEIGHT = 400;

    private boolean newGameSelected = false;
    private boolean continueSelected = false;
    private boolean cancelled = false;
    private int selectedOption = 0; // 0 = NEW GAME, 1 = CONTINUE
    private boolean hasSaveFile = false;

    public MainMenu(JFrame parent) {
        super(parent, "Pokemon Java", true);

        // CHECK IF SAVE FILE EXISTS
        hasSaveFile = SaveManager.hasSaveFile();

        // SET DEFAULT SELECTED OPTION
        if (hasSaveFile) {
            selectedOption = 1; // DEFAULT TO CONTINUE IF SAVE EXISTS
        } else {
            selectedOption = 0; // DEFAULT TO NEW GAME IF NO SAVE
        }

        setupDialog();
    }

    private void setupDialog() {
        setSize(MENU_WIDTH, MENU_HEIGHT);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // ADD WINDOW LISTENER TO HANDLE CLOSE BUTTON
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelled = true;
            }
        });

        // MAIN PANEL
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // GRADIENT BACKGROUND
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(70, 130, 180)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // TITLE
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 48));
                String title = "POKEMON JAVA";
                int titleWidth = g2.getFontMetrics().stringWidth(title);
                g2.drawString(title, (getWidth() - titleWidth) / 2, 80);

                // SUBTITLE
                g2.setFont(new Font("Arial", Font.ITALIC, 18));
                String subtitle = "A Classic Adventure";
                int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
                g2.drawString(subtitle, (getWidth() - subtitleWidth) / 2, 110);

                // MENU OPTIONS
                int optionY = 180;
                int optionSpacing = 70;

                // NEW GAME OPTION
                drawMenuOption(g2, "NEW GAME", optionY, selectedOption == 0);

                // CONTINUE OPTION
                if (hasSaveFile) {
                    drawMenuOption(g2, "CONTINUE", optionY + optionSpacing, selectedOption == 1);
                } else {
                    // DRAW GRAYED OUT CONTINUE OPTION
                    g2.setColor(new Color(180, 180, 180));
                    g2.setFont(new Font("Arial", Font.BOLD, 28));
                    String text = "CONTINUE";
                    int textWidth = g2.getFontMetrics().stringWidth(text);
                    g2.drawString(text, (getWidth() - textWidth) / 2, optionY + optionSpacing);

                    g2.setFont(new Font("Arial", Font.PLAIN, 14));
                    g2.setColor(new Color(200, 200, 200));
                    String noSave = "(No save file found)";
                    int noSaveWidth = g2.getFontMetrics().stringWidth(noSave);
                    g2.drawString(noSave, (getWidth() - noSaveWidth) / 2, optionY + optionSpacing + 25);
                }
            }
        };

        mainPanel.setLayout(null);
        add(mainPanel);

        // KEY LISTENER FOR NAVIGATION
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        setFocusable(true);
    }

    private void drawMenuOption(Graphics2D g2, String text, int y, boolean selected) {
        if (selected) {
            // SELECTED OPTION - DRAW BOX AND ARROW
            int boxWidth = 300;
            int boxHeight = 50;
            int boxX = (MENU_WIDTH - boxWidth) / 2;
            int boxY = y - 35;

            // OUTER GLOW
            g2.setColor(new Color(255, 215, 0, 100));
            g2.fillRoundRect(boxX - 5, boxY - 5, boxWidth + 10, boxHeight + 10, 15, 15);

            // SELECTION BOX
            g2.setColor(new Color(255, 215, 0));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

            // TEXT
            g2.setColor(new Color(50, 50, 50));
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, (MENU_WIDTH - textWidth) / 2, y);

            // ARROW
            g2.setColor(new Color(50, 50, 50));
            int arrowSize = 15;
            int[] xPoints = {boxX - 30, boxX - 30, boxX - 15};
            int[] yPoints = {boxY + 10, boxY + boxHeight - 10, boxY + boxHeight / 2};
            g2.fillPolygon(xPoints, yPoints, 3);
        } else {
            // UNSELECTED OPTION
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, (MENU_WIDTH - textWidth) / 2, y);
        }
    }

    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                // MOVE UP
                if (selectedOption > 0) {
                    selectedOption--;
                    repaint();
                }
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                // MOVE DOWN
                if (hasSaveFile && selectedOption < 1) {
                    selectedOption++;
                    repaint();
                }
                break;

            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                // SELECT OPTION
                if (selectedOption == 0) {
                    newGameSelected = true;
                    dispose();
                } else if (selectedOption == 1 && hasSaveFile) {
                    continueSelected = true;
                    dispose();
                }
                break;

            case KeyEvent.VK_ESCAPE:
                // CANCEL
                cancelled = true;
                dispose();
                break;
        }
    }

    public void showMenu() {
        setVisible(true);
    }

    public boolean isNewGameSelected() {
        return newGameSelected;
    }

    public boolean isContinueSelected() {
        return continueSelected;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}