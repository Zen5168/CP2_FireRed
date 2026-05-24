# Wild Encounter System - Flow Diagram

## System Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    PLAYER MOVEMENT                          │
│                  (Player.update())                          │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│              CHECK IF MOVED TO NEW TILE                     │
│           (currentTileX != lastTileX OR                     │
│            currentTileY != lastTileY)                       │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│           GET TILE AT CURRENT POSITION                      │
│      tileNum = gp.tileM.mapTileNum[x][y]                   │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│         IS IT A TALL GRASS TILE?                            │
│    (gp.tileM.tile[tileNum].hasWildEncounter)               │
└────────┬────────────────────────────────────────┬───────────┘
         │ YES                                     │ NO
         ▼                                         ▼
┌──────────────────────────┐              ┌──────────────────┐
│  INCREMENT STEP COUNTER  │              │  CONTINUE GAME   │
│  (stepCounter++)         │              └──────────────────┘
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│     HAS STEP COUNTER REACHED THRESHOLD?                      │
│  (stepCounter >= stepsUntilNextEncounter)                    │
└──────────┬───────────────────────────────────────┬───────────┘
           │ YES                                    │ NO
           ▼                                        ▼
┌──────────────────────────┐              ┌──────────────────┐
│  TRIGGER ENCOUNTER!      │              │  CONTINUE GAME   │
│  - Reset counter         │              └──────────────────┘
│  - Generate wild Pokemon │
│  - Switch to BATTLE      │
└──────────┬───────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│              GENERATE WILD POKEMON                          │
│         (WildEncounterManager.generateWildPokemon())        │
│                                                             │
│  Roll 0-99:                                                 │
│    0-39:  Rattata (40%)                                     │
│   40-79:  Pidgey (40%)                                      │
│   80-89:  Bulbasaur (10%)                                   │
│   90-94:  Charmander (5%)                                   │
│   95-99:  Squirtle (5%)                                     │
│                                                             │
│  Level: Random 3-6                                          │
└──────────┬──────────────────────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│                  BATTLE TRANSITION                          │
│  - Set gameState = BATTLE                                   │
│  - Store wild Pokemon in gp.currentWildPokemon              │
│  - Start fade-in transition                                 │
│  - Print "A wild X appeared!"                               │
└──────────┬──────────────────────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│                  BATTLE SCREEN                              │
│  - Draw background (grass/ground)                           │
│  - Draw enemy Pokemon sprite (top right)                    │
│  - Draw player Pokemon sprite (bottom left)                 │
│  - Draw info boxes with HP bars                             │
│  - Show transition effect                                   │
└──────────┬──────────────────────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│              BATTLE MANAGER (TODO)                          │
│  - Handle battle logic                                      │
│  - Process player input                                     │
│  - Execute moves                                            │
│  - Check win/lose conditions                                │
└──────────┬──────────────────────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│              RETURN TO OVERWORLD                            │
│  - Set gameState = OVERWORLD                                │
│  - Resume player movement                                   │
└─────────────────────────────────────────────────────────────┘
```

## Key Components

### 1. Player.java
```java
// Tracks tile position
private int lastTileX = -1;
private int lastTileY = -1;

// Called every frame during movement
private void checkWildEncounter() {
    // Get current tile
    // Check if it's grass
    // Trigger encounter if conditions met
}

private void triggerWildBattle() {
    // Generate wild Pokemon
    // Switch game state
    // Start transition
}
```

### 2. WildEncounterManager.java
```java
// Step counter system
private int stepCounter;
private int stepsUntilNextEncounter;

// Check if encounter should trigger
public boolean checkForEncounter() {
    stepCounter++;
    return stepCounter >= stepsUntilNextEncounter;
}

// Generate random wild Pokemon
public Pokemon generateWildPokemon() {
    // Roll for species (rarity-based)
    // Roll for level (3-6)
    // Return new Pokemon instance
}
```

### 3. GamePanel.java
```java
// Game state management
public GameState gameState = GameState.OVERWORLD;

// Update loop
public void update() {
    if (gameState == GameState.OVERWORLD) {
        player.update();  // Movement + encounter checks
    } else if (gameState == GameState.BATTLE) {
        battleScreen.updateTransition();
    }
}

// Render loop
public void paintComponent(Graphics g) {
    if (gameState == GameState.OVERWORLD) {
        // Draw tiles and player
    } else if (gameState == GameState.BATTLE) {
        // Draw battle screen
    }
}
```

### 4. BattleScreen.java
```java
// Visual battle renderer
public void draw(Graphics2D g2, Pokemon player, Pokemon enemy) {
    // Draw background
    // Draw Pokemon sprites
    // Draw info boxes with HP bars
    // Draw transition effect
}
```

### 5. TileManager.java
```java
// Mark grass tiles for encounters
tile[2].hasWildEncounter = true;  // Tall grass
```

## Tile Types

| Index | Type        | Collision | Wild Encounter |
|-------|-------------|-----------|----------------|
| 0     | Light Grass | No        | No             |
| 1     | Path        | No        | No             |
| 2     | Tall Grass  | No        | **YES**        |
| 3     | Bush        | Yes       | No             |

## Encounter Probability

### Steps Until Encounter
- Minimum: 3 steps on grass
- Maximum: 8 steps on grass
- Random each time

### Example Scenario:
```
Step 1 on grass: Counter = 1, Threshold = 5 → No encounter
Step 2 on grass: Counter = 2, Threshold = 5 → No encounter
Step 3 on grass: Counter = 3, Threshold = 5 → No encounter
Step 4 on grass: Counter = 4, Threshold = 5 → No encounter
Step 5 on grass: Counter = 5, Threshold = 5 → ENCOUNTER!
```

After encounter, new threshold is randomly set (e.g., 7), and counter resets to 0.

## Pokemon Encounter Rates

```
Common (80%):
├─ Rattata (40%)
└─ Pidgey (40%)

Uncommon (10%):
└─ Bulbasaur (10%)

Rare (10%):
├─ Charmander (5%)
└─ Squirtle (5%)
```

## Integration Points

### Current Status: ✅ Visual System Complete
- ✅ Tile detection working
- ✅ Step counter implemented
- ✅ Wild Pokemon generation
- ✅ Battle screen rendering
- ✅ Game state management
- ✅ Transition effects

### TODO: Connect to BattleManager
```java
// In Player.triggerWildBattle()
Pokemon playerPokemon = /* Get from trainer class */;
BattleManager battleManager = new BattleManager();
boolean won = battleManager.startBattle(
    playerTrainer,
    playerPokemon,
    wildPokemon,
    false  // isTrainerBattle
);

// After battle
gp.gameState = GameState.OVERWORLD;
```

## Testing Checklist

- [ ] Walk on tall grass (tile 2)
- [ ] Verify console message "A wild X appeared!"
- [ ] Check battle screen displays
- [ ] Verify Pokemon sprites show
- [ ] Check HP bars render correctly
- [ ] Test transition effect
- [ ] Verify game returns to overworld after battle
- [ ] Test different encounter rates (walk on grass multiple times)
- [ ] Verify step counter resets after encounter
- [ ] Test that non-grass tiles don't trigger encounters
