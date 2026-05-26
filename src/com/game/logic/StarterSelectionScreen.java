package com.game.logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.game.pokemons.*;

public class StarterSelectionScreen extends JDialog {
    
    private String playerName;
    private Pokemon selectedStarter;
    private boolean selectionComplete = false;
    private int selectedIndex = 0; // 0=CHARMANDER, 1=BULBASAUR, 2=SQUIRTLE
    
    private JTextField nameField;
    private JPanel charmanderPanel;
    private JPanel bulbasaurPanel;
    private JPanel squirtlePanel;
    private JButton confirmButton;
    private JLabel selectedLabel;
    private JLabel instructionLabel;
    
    private BufferedImage[][] pokemonSprites; // [POKEMON_ID][0=FRONT, 1=BACK]
    
    public StarterSelectionScreen(JFrame parent) {
        super(parent, "Welcome to Pokemon!", true);
        
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        loadPokemonSprites();
        initComponents();
        setupKeyboardNavigation();
    }
    
    private void loadPokemonSprites() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/image/pokemon_battle_sprites.png"));
            
            // INITIALIZE SPRITE ARRAY
            pokemonSprites = new BufferedImage[3][2]; // [POKEMON_ID][0=FRONT, 1=BACK]
            
            // MAP EACH POKEMON SPRITE TO ITS LOCATION ON THE SHEET
            Rectangle[][] spriteMap = {
                // BULBASAUR [0]
                {new Rectangle(12, 46, 63, 63)},
                
                // CHARMANDER [1]
                {new Rectangle(401, 46, 63, 63)},
                
                // SQUIRTLE [2]
                {new Rectangle(792, 46, 63, 63)},
            };
            
            // EXTRACT SPRITES USING THE MAP
            for (int i = 0; i < spriteMap.length; i++) {
                // FRONT SPRITE
                Rectangle frontRect = spriteMap[i][0];
                pokemonSprites[i][0] = spriteSheet.getSubimage(
                    frontRect.x, 
                    frontRect.y, 
                    frontRect.width, 
                    frontRect.height
                );
            }
            
            System.out.println("Starter sprites loaded successfully!");
            
        } catch (Exception e) {
            System.out.println("Could not load starter sprites: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        // TITLE PANEL
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(220, 50, 50));
        JLabel titleLabel = new JLabel("Welcome to the World of Pokemon!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // NAME INPUT PANEL
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        
        // STARTER SELECTION PANEL
        JPanel starterPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        starterPanel.setBackground(Color.WHITE);
        starterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // CHARMANDER PANEL
        charmanderPanel = createStarterPanel(
            "Charmander",
            "Fire Type",
            new Color(255, 100, 50),
            1 // sprite index
        );
        
        // BULBASAUR PANEL
        bulbasaurPanel = createStarterPanel(
            "Bulbasaur",
            "Grass Type",
            new Color(100, 200, 100),
            0 // sprite index
        );
        
        // SQUIRTLE PANEL
        squirtlePanel = createStarterPanel(
            "Squirtle",
            "Water Type",
            new Color(100, 150, 255),
            2 // sprite index
        );
        
        starterPanel.add(charmanderPanel);
        starterPanel.add(bulbasaurPanel);
        starterPanel.add(squirtlePanel);
        
        // SELECTION INFO PANEL
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        instructionLabel = new JLabel("Use arrow keys or 1/2/3 keys to select", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setForeground(new Color(100, 100, 100));
        
        selectedLabel = new JLabel("Choose your starter Pokemon!");
        selectedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectedLabel.setForeground(new Color(50, 50, 150));
        selectedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        infoPanel.add(instructionLabel);
        infoPanel.add(selectedLabel);
        
        // CONFIRM BUTTON PANEL
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        confirmButton = new JButton("Start Adventure! Press ENTER)");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setBackground(new Color(50, 150, 50));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> confirmSelection());
        buttonPanel.add(confirmButton);
        
        // COMBINE NAME AND INFO PANELS
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(namePanel, BorderLayout.CENTER);
        
        // COMBINE INFO AND BUTTON PANELS
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // ADD ALL PANELS TO DIALOG
        add(topPanel, BorderLayout.NORTH);
        add(starterPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // SELECT CHARMANDER BY DEFAULT
        selectStarter(0);
    }
    
    private JPanel createStarterPanel(String name, String type, Color color, int spriteIndex) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // SPRITE PANEL
        JPanel spritePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (pokemonSprites != null && spriteIndex < pokemonSprites.length) {
                    BufferedImage sprite = pokemonSprites[spriteIndex][0]; // FRONT SPRITE
                    if (sprite != null) {
                        // CENTER THE SPRITE
                        int x = (getWidth() - sprite.getWidth() * 2) / 2;
                        int y = (getHeight() - sprite.getHeight() * 2) / 2;
                        g.drawImage(sprite, x, y, sprite.getWidth() * 2, sprite.getHeight() * 2, null);
                    }
                }
            }
        };
        spritePanel.setOpaque(false);
        spritePanel.setPreferredSize(new Dimension(150, 150));
        
        // TEXT PANEL
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        
        JLabel typeLabel = new JLabel(type, SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setForeground(Color.WHITE);
        
        textPanel.add(nameLabel);
        textPanel.add(typeLabel);
        
        panel.add(spritePanel, BorderLayout.CENTER);
        panel.add(textPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupKeyboardNavigation() {
        // ADD KEY LISTENER TO THE DIALOG
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        
        // ADD KEY LISTENER TO NAME FIELD
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    confirmSelection();
                } else {
                    handleKeyPress(e);
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                checkConfirmButton();
            }
        });
        
        setFocusable(true);
        requestFocusInWindow();
    }
    
    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                selectedIndex = (selectedIndex - 1 + 3) % 3;
                selectStarter(selectedIndex);
                break;
                
            case KeyEvent.VK_RIGHT:
                selectedIndex = (selectedIndex + 1) % 3;
                selectStarter(selectedIndex);
                break;
                
            case KeyEvent.VK_1:
                selectedIndex = 0;
                selectStarter(selectedIndex);
                break;
                
            case KeyEvent.VK_2:
                selectedIndex = 1;
                selectStarter(selectedIndex);
                break;
                
            case KeyEvent.VK_3:
                selectedIndex = 2;
                selectStarter(selectedIndex);
                break;
                
            case KeyEvent.VK_ENTER:
                confirmSelection();
                break;
        }
    }
    
    private void selectStarter(int index) {
        selectedIndex = index;
        
        // RESET ALL PANEL BORDERS
        charmanderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        bulbasaurPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        squirtlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // HIGHLIGHT SELECTED PANEL AND CREATE POKEMON
        JPanel selectedPanel = null;
        String starterName = "";
        
        switch (index) {
            case 0: // CHARMANDER
                selectedPanel = charmanderPanel;
                starterName = "Charmander";
                selectedStarter = new Charmander(5);
                break;
            case 1: // BULBASAUR
                selectedPanel = bulbasaurPanel;
                starterName = "Bulbasaur";
                selectedStarter = new Bulbasaur(5);
                break;
            case 2: // SQUIRTLE
                selectedPanel = squirtlePanel;
                starterName = "Squirtle";
                selectedStarter = new Squirtle(5);
                break;
        }
        
        if (selectedPanel != null) {
            selectedPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
        }
        
        selectedLabel.setText("You selected " + starterName + "!");
        
        // ENABLE CONFIRM BUTTON IF NAME IS ENTERED
        checkConfirmButton();
    }
    
    private void checkConfirmButton() {
        String name = nameField.getText().trim();
        confirmButton.setEnabled(!name.isEmpty() && selectedStarter != null);
    }
    
    private void confirmSelection() {
        playerName = nameField.getText().trim();
        
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your name!", 
                "Name Required", 
                JOptionPane.WARNING_MESSAGE);
            nameField.requestFocusInWindow();
            return;
        }
        
        if (selectedStarter == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a starter Pokemon!", 
                "Starter Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        selectionComplete = true;
        dispose();
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public Pokemon getSelectedStarter() {
        return selectedStarter;
    }
    
    public boolean isSelectionComplete() {
        return selectionComplete;
    }
    
    public void showDialog() {
        setVisible(true);
    }
}
