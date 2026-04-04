package com.game.pokemons;

import com.game.logic.Type;
import com.game.moves.Moves;
import java.util.*;

public abstract class Pokemon {

    //================================
    // POKEMON IDENTITY
    //================================
    protected String pokeName;
    protected Type type1, type2;
    protected int level;
    protected int exp = 0;
    protected int nextLevelExp = 100;
    protected int evolutionLevel = -1;
    protected String evolutionName;
    protected Map<Integer, String> moveLevelUpTable = new HashMap<>();

    //================================
    // CALCULATED STATS
    //================================
    protected int hp, maxHp, atk, def, spAtk, spDef, speed;

    //================================
    // BASE STATS
    //================================
    protected int baseHp, baseAtk, baseDef, baseSpAtk, baseSpDef, baseSpeed;

    //================================
    // MOVE MANAGEMENT (4-SLOT LIMIT)
    //================================
    protected Moves[] moves = new Moves[4];

    public Pokemon(String name, Type type1, Type type2, int level, int bHp, int bAtk, int bDef, int bSpAtk, int bSpDef, int bSpeed) {
        this.pokeName = name;
        this.type1 = type1;
        this.type2 = type2;
        this.level = level;
        this.baseHp = bHp;
        this.baseAtk = bAtk;
        this.baseDef = bDef;
        this.baseSpAtk = bSpAtk;
        this.baseSpDef = bSpDef;
        this.baseSpeed = bSpeed;

        calculateStats();
    }

    public void calculateStats() {
        // HP Formula
        this.maxHp = ((2 * baseHp) * level / 100) + level + 10;
        this.hp = maxHp;

        // Other Stats Formula
        this.atk = ((2 * baseAtk) * level / 100) + 5;
        this.def = ((2 * baseDef) * level / 100) + 5;
        this.spAtk = ((2 * baseSpAtk) * level / 100) + 5;
        this.spDef = ((2 * baseSpDef) * level / 100) + 5;
        this.speed = ((2 * baseSpeed) * level / 100) + 5;
    }

    //=====================================
    // EXP LOGIC
    //=====================================
    public void gainExp(int amount) {
        this.exp += amount;
        System.out.println(this.pokeName + " gained " + amount + " EXP!");

        while (this.exp >= this.nextLevelExp) {
            levelUp();
        }
    }

    //=====================================
    // LEVEL UP LOGIC
    //=====================================
    private void levelUp() {
        this.level++;
        this.exp -= this.nextLevelExp;
        this.nextLevelExp = (int) (this.nextLevelExp * 1.2);

        calculateStats();
        this.hp = this.maxHp;
        System.out.println("\n========================================");
        System.out.println("Congratulations! " + pokeName + " grew to Level " + level + "!");
        System.out.println("========================================\n");

        if (moveLevelUpTable.containsKey(this.level)) {
            String newMoveName = moveLevelUpTable.get(this.level);
            Moves newMove = com.game.moves.MoveDatabase.get(newMoveName);

            System.out.println(pokeName + " wants to learn " + newMoveName + "!");

            boolean learned = false;
            for (int i = 0; i < 4; i++) {
                if (moves[i] == null) {
                    learnMove(newMove, i);
                    System.out.println(pokeName + " learned " + newMoveName + "!");
                    learned = true;
                    break;
                }
            }

            if (!learned) {
                handleMoveForget(newMove);
            }
        }

        // CHECK EVOLUTION
        if (evolutionLevel != -1 && this.level >= evolutionLevel) {
            evolve();
        }
    }

    //=====================================
    // FORGET MOVE / REPLACE MOVE
    //=====================================
    private void handleMoveForget(Moves newMove) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(pokeName + " already knows four moves, but wants to learn " + newMove.getName() + ".");
        System.out.print("\nShould a move be forgotten and replaced with " + newMove.getName() + "? (y/n): ");

        String choice = scanner.nextLine().toLowerCase();

        if (choice.equals("y")) {
            System.out.println("\nWhich move should be forgotten?");
            for (int i = 0; i < 4; i++) {
                System.out.println((i + 1) + ": " + moves[i].getName());
            }
            System.out.println("5: Abandon " + newMove.getName());

            System.out.print("Selection (1-5): ");
            int indexChoice = scanner.nextInt();

            if (indexChoice >= 1 && indexChoice <= 4) {
                String oldMoveName = moves[indexChoice - 1].getName();
                learnMove(newMove, indexChoice - 1); // Overwrites chosen slot
                System.out.println("1, 2, and... Poof! " + pokeName + " forgot " + oldMoveName + " and...");
                System.out.println(pokeName + " learned " + newMove.getName() + "!");
            } else {
                System.out.println(pokeName + " did not learn " + newMove.getName() + ".");
            }
        } else {
            System.out.println(pokeName + " did not learn " + newMove.getName() + ".");
        }
    }

    //=====================================
    // EVOLUTION
    //=====================================
    private void evolve() {
        System.out.println("What? " + this.pokeName + " is evolving!");

        try {
            Class<?> evoClass = Class.forName(evolutionName);
            Pokemon evolvedForm = (Pokemon) evoClass.getConstructor(int.class).newInstance(this.level);

            System.out.println(this.pokeName + " evolved into " + evolvedForm.getName() + "!");

            // TRANSFER STATS WHEN EVOLVING
            this.pokeName = evolvedForm.getName();
            this.baseHp = evolvedForm.baseHp;
            this.baseAtk = evolvedForm.baseAtk;
            this.baseDef = evolvedForm.baseDef;
            this.baseSpAtk = evolvedForm.baseSpAtk;
            this.baseSpDef = evolvedForm.baseSpDef;
            this.baseSpeed = evolvedForm.baseSpeed;
            this.type1 = evolvedForm.type1;
            this.type2 = evolvedForm.type2;

            calculateStats();
            this.hp = this.maxHp;

        } catch (Exception e) {
            System.out.println("Error during evolution: " + e.getMessage());
        }
    }

    //=====================================
    // BASIC DAMAGE LOGIC
    //=====================================
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(pokeName + " took " + damage + " damage! (HP: " + hp + "/" + maxHp + ")");
    }

    public boolean isFainted() {
        return this.hp <= 0;
    }

    public void learnMove(Moves newMove, int slot) {
        if (slot >= 0 && slot < 4) {
            this.moves[slot] = newMove;
        }
    }

    //=====================================
    // GETTERS & SETTERS
    //=====================================
    public String getName() {
        return this.pokeName;
    }

    public Type getType1() {
        return this.type1;
    }

    public Type getType2() {
        return this.type2;
    }

    public int getLevel() {
        return this.level;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAtk() {
        return this.atk;
    }

    public int getDef() {
        return this.def;
    }

    public int getSpAtk() {
        return this.spAtk;
    }

    public int getSpDef() {
        return this.spDef;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Moves[] getMoves() {
        return this.moves;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public int getExp() {
        return this.exp;
    }

    public int getNextLevelExp() {
        return this.nextLevelExp;
    }

    public void setHp(int newHp) {
        if (newHp > this.maxHp) {
            this.hp = this.maxHp;
        } else if (newHp < 0) {
            this.hp = 0;
        } else {
            this.hp = newHp;
        }
    }
}
