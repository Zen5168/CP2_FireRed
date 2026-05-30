package com.game.logic;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, ctrlPressed;
    
    // BATTLE CONTROLS
    public boolean uPressed, iPressed, jPressed, kPressed;
    
    // KEY PRESS EVENTS (FOR SINGLE PRESS DETECTION)
    private List<String> keyPressQueue = new ArrayList<>();

    @Override
    public void keyTyped(KeyEvent e) { // UNUSED BUT NEC
    }

    @Override
    public void keyPressed(KeyEvent e) { // RUNS WHEN A KEY IS PRESSED

        int code = e.getKeyCode();

        // MOVEMENT KEYS
        if (code == KeyEvent.VK_W) {
            if (!upPressed) {
                upPressed = true;
                addKeyPress("W");
            }
        }
        if (code == KeyEvent.VK_A) {
            if (!leftPressed) {
                leftPressed = true;
                addKeyPress("A");
            }
        }
        if (code == KeyEvent.VK_S) {
            if (!downPressed) {
                downPressed = true;
                addKeyPress("S");
            }
        }
        if (code == KeyEvent.VK_D) {
            if (!rightPressed) {
                rightPressed = true;
                addKeyPress("D");
            }
        }
        
        // ACTION KEYS (UIJK = AXYB on Xbox controller)
        if (code == KeyEvent.VK_U) {
            if (!uPressed) {
                uPressed = true;
                addKeyPress("U");
            }
        }
        if (code == KeyEvent.VK_I) {
            if (!iPressed) {
                iPressed = true;
                addKeyPress("I");
            }
        }
        if (code == KeyEvent.VK_J) {
            if (!jPressed) {
                jPressed = true;
                addKeyPress("J");
            }
        }
        if (code == KeyEvent.VK_K) {
            if (!kPressed) {
                kPressed = true;
                addKeyPress("K");
            }
        }
        
        // SPECIAL KEYS
        if (code == KeyEvent.VK_CONTROL) {
            ctrlPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            addKeyPress("SPACE");
        }
        if (code == KeyEvent.VK_ENTER) {
            addKeyPress("ENTER");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // RUNS WHEN A KEY IS RELEASED

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_U) {
            uPressed = false;
        }
        if (code == KeyEvent.VK_I) {
            iPressed = false;
        }
        if (code == KeyEvent.VK_J) {
            jPressed = false;
        }
        if (code == KeyEvent.VK_K) {
            kPressed = false;
        }
        if (code == KeyEvent.VK_CONTROL) {
            ctrlPressed = false;
        }
    }
    
    //=====================================
    // KEY PRESS QUEUE (FOR SINGLE PRESS EVENTS)
    //=====================================
    private void addKeyPress(String key) {
        keyPressQueue.add(key);
    }
    
    public boolean hasKeyPress() {
        return !keyPressQueue.isEmpty();
    }
    
    public String getNextKeyPress() {
        if (!keyPressQueue.isEmpty()) {
            return keyPressQueue.remove(0);
        }
        return null;
    }
    
    public void clearKeyPresses() {
        keyPressQueue.clear();
    }

}
