package com.game.world;

import com.game.trainers.Trainer;
import com.game.pokemons.*;
import java.util.*;

public class Map {

    private Tile[][] grid;
    private int width, height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[height][width];
        generateBasicMap();
    }

    //================================
    // BASIC MAP
    //================================
    private void generateBasicMap() {

        // SYMBOL, WALKABLE, HAS ENCOUNTER, NAME
        Tile grass = new Tile("#", true, true, "Tall Grass");
        Tile path = new Tile(".", true, false, "Path");
        Tile wall = new Tile("T", false, false, "Tree");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // A BORDER OF TREES
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    grid[y][x] = wall;
                } else if (x > 4 && y > 2 && y < 6) {
                    grid[y][x] = grass; // A PATCH OF GRASS
                } else {
                    grid[y][x] = path;
                }
            }
        }
        Tile center = new Tile("H", true, false, "Pokemon Center");
        grid[1][5] = center;
    }

    public void render(Trainer player) {
        System.out.println("\n==================== Region Map =======================");
        System.out.println("( p = player), (. = path), (T = Tree), (# = Tall Grass), (H = Poke Center)");
        System.out.println("=======================================================");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == player.getX() && y == player.getY()) {
                    System.out.print("P "); // REPRESENT PLAYER
                } else {
                    System.out.print(grid[y][x].getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }

    //================================
    // WILD ENCOUNTERS
    //================================
    public Pokemon getRandomWildPokemon() {
        Random r = new Random();
        int chance = r.nextInt(100);
        int wildLevel = r.nextInt(3) + 3;

        if (chance < 50) {
            return new Pidgey(wildLevel);
        }
        return new Rattata(wildLevel);
    }

    //================================
    // GETTERS
    //================================
    public Tile getTile(int x, int y) {
        return grid[y][x];
    }
}
