package com.game.logic;

import com.game.world.Tile;
import com.game.world.Map;
import com.game.trainers.*;
import com.game.pokemons.*;
import com.game.audio.*;
import java.util.*;

public class OverworldManager {

    private AudioManager audio = new AudioManager();
    private Scanner scanner = new Scanner(System.in);
    private Map gameMap;
    private Trainer player;
    private BattleManager battleManager;
    private boolean isRunning = true;

    public OverworldManager() {
        this.gameMap = new Map(10, 10);
        this.battleManager = new BattleManager();
    }

    public void start() {
        System.out.println("Welcome to the world of Pokemon!");
        System.out.print("First, tell me your name: ");
        String name = scanner.nextLine();

        this.player = new Trainer(name, 1, 1);

        // STARTER POKEMON SELECTION
        System.out.println("\nProfessor Delamaine: 'It's time to choose your companion!'");
        System.out.println("1: Bulbasaur (Grass)");
        System.out.println("2: Charmander (Fire)");
        System.out.println("3: Squirtle (Water)");
        System.out.print("Choose your Pokemon (1-3): ");

        int choice = 0;
        // INPUT VALIDATION
        while (choice < 1 || choice > 3) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.print("Please choose 1, 2, or 3: ");
                }
            } else {
                System.out.print("Invalid input. Enter a number (1-3): ");
                scanner.next();
            }
        }
        scanner.nextLine();

        switch (choice) {
            case 1:
                player.addPokemon(new Bulbasaur(5));
                System.out.println("You chose Bulbasaur!");
                break;
            case 2:
                player.addPokemon(new Charmander(5));
                System.out.println("You chose Charmander!");
                break;
            case 3:
                player.addPokemon(new Squirtle(5));
                System.out.println("You chose Squirtle!");
                break;
        }

        System.out.println("\nHello " + name + "! Use W, A, S, D to move. Good luck!\n");

        audio.playWithLoop("BGM.wav", 0);
        gameLoop();
    }

    //================================
    // CONTROLLER LOOP
    //================================
    private void gameLoop() {
        while (isRunning) {
            gameMap.render(player);
            System.out.println("Controls: (W) Up, (S) Down, (A) Left, (D) Right, (Q) Quit");
            System.out.print("Action: ");

            String input = scanner.nextLine().toLowerCase();
            if (input.equals("q")) {
                isRunning = false;
                System.out.println("Thanks for playing!");
                break;
            }

            processMovement(input);
        }
    }

    //================================
    // MOVEMENT
    //================================
    private void processMovement(String input) {
        int nextX = player.getX();
        int nextY = player.getY();

        switch (input) {
            case "w":
                nextY--;
                break;
            case "s":
                nextY++;
                break;
            case "a":
                nextX--;
                break;
            case "d":
                nextX++;
                break;
            default:
                return;
        }

        Tile nextTile = gameMap.getTile(nextX, nextY);

        if (nextTile != null && nextTile.isWalkable()) {
            // UPDATE PLAYER POSITION
            player.setX(nextX);
            player.setY(nextY);

            //================================
            // WILD ENCOUNTERS
            //================================
            if (nextTile.hasEncounter()) {
                checkForEncounter();
            }
        } else {
            System.out.println("You bumped into something!");
        }

        //================================
        // POKEMON CENTER
        //================================
        if (nextTile.getName().equals("Pokemon Center")) {
            System.out.println("Welcome to the Pokemon Center! Healing your party...");
            for (Pokemon p : player.getParty()) {
                p.setHp(p.getMaxHp());
            }
            System.out.println("Your Pokemon are now healthy!!");
        }
    }

    //================================
    // ENCOUNTER MECHANIC
    //================================
    private void checkForEncounter() {
        if (Math.random() < 0.20) {
            System.out.println("\n!!! A wild Pokemon jumped out of the grass !!!");
            audio.playForDuration("Battle.wav", 0, 3600);
            Pokemon wildPoke = gameMap.getRandomWildPokemon();

            boolean won = battleManager.startBattle(player.getParty().get(0), wildPoke, false);

            if (!won) {
                teleportToCenter();
            }
            audio.playWithLoop("BGM.wav", 0);
        }
    }

    //================================
    // TELEPORT TO POKECENTER
    //================================
    private void teleportToCenter() {
        System.out.println("\nWhited out! Hurrying to the Pokemon Center...");

        player.setX(5);
        player.setY(1);

        for (Pokemon p : player.getParty()) {
            p.setHp(p.getMaxHp());
        }
        System.out.println("Your Pokemon are now healthy!!\n");
    }
}
