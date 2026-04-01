package com.game.logic;

import com.game.world.Tile;
import com.game.world.Map;
import com.game.trainers.*;
import com.game.pokemons.*;
import java.util.*;

public class Controller {

    private Scanner scanner = new Scanner(System.in);
    private Map gameMap;
    private Trainer player;
    private BattleManager battleManager;
    private boolean isRunning = true;

    public Controller() {
        this.gameMap = new Map(10, 10);
        this.battleManager = new BattleManager();
    }

    public void start() {

        System.out.println("Welcome to the world of Pokemon!");
        System.out.print("First, tell me your name: ");
        String name = scanner.nextLine();

        this.player = new Trainer(name, 1, 1);

        player.addPokemon(new Bulbasaur(5)); // PLACE HOLDER FOR BETA TESTING

        System.out.println("\nHello " + name + "! Use W, A, S, D to move. Good luck!\n");

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

    // MOVEMENT
    //
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

            Pokemon wildPoke = gameMap.getRandomWildPokemon();

            battleManager.startBattle(player.getParty().get(0), wildPoke, false);
        }
    }
}
