# Wild Pokémon Encounter System

## Overview
This system implements wild Pokémon encounters when the player walks on tall grass tiles, similar to the classic Pokémon games.

## How It Works

### 1. **Tile Detection**
- Tall grass tiles (tile index `2`) are marked with `hasWildEncounter = true`
- The system tracks when the player moves to a new tile
- Only triggers encounters when stepping on grass tiles

### 2. **Step Counter System**
- Uses a random step counter (3-8 steps) before triggering an encounter
- Each time you step on grass, the counter increments
- When the counter reaches the threshold, a wild Pokémon appears
- Counter resets after each encounter

### 3. **Wild Pokémon Generation**
The encounter system generates random Pokémon with varying rarity:

| Pokémon    | Encounter Rate | Level Range |
|------------|----------------|-------------|
| Rattata    | 40%            | 3-6         |
| Pidgey     | 40%            | 3-6         |
| Bulbasaur  | 10%            | 3-6         |
| Charmander | 5%             | 3-6         |
| Squirtle   | 5%             | 3-6         |

### 4. **Battle Transition**
- When an encounter triggers, the game state switches from `OVERWORLD` to `BATTLE`
- A visual transition effect fades in
- The battle screen displays both Pokémon with HP bars and info boxes
- Battle is handled by your existing `BattleManager` class

## Files Created/Modified

### New Files:
1. **`WildEncounterManager.java`** - Manages encounter logic and wild Pokémon generation
2. **`GameState.java`** - Enum for game states (OVERWORLD, BATTLE, MENU, DIALOGUE)
3. **`BattleScreen.java`** - Visual battle screen renderer with Pokémon sprites and UI
4. **`WILD_ENCOUNTER_SYSTEM.md`** - This documentation file

### Modified Files:
1. **`Player.java`**
   - Added tile position tracking (`lastTileX`, `lastTileY`)
   - Added `checkWildEncounter()` method
   - Added `triggerWildBattle()` method
   - Detects when player moves to new tiles

2. **`GamePanel.java`**
   - Added `WildEncounterManager` instance
   - Added `GameState` tracking
   - Added `BattleScreen` instance
   - Modified `update()` to handle different game states
   - Modified `paintComponent()` to render battle screen

3. **`TileManager.java`**
   - Marked tile[2] (tall grass) with `hasWildEncounter = true`

4. **`Tile.java`**
   - Already had `hasWildEncounter` boolean field

## How to Use

### Testing the System:
1. Run your game
2. Walk around on the tall grass tiles (the darker green tiles marked as `2` in the map)
3. After 3-8 steps on grass, a wild Pokémon will appear
4. The screen will transition to the battle view

### Customizing Encounter Rates:
Edit `WildEncounterManager.java` in the `generateWildPokemon()` method:

```java
if (encounterRoll < 40) {
    return new Rattata(level);  // 40% chance
} else if (encounterRoll < 80) {
    return new Pidgey(level);   // 40% chance
}
// ... etc
```

### Customizing Step Count:
Edit these constants in `WildEncounterManager.java`:

```java
private static final int MIN_STEPS_BETWEEN_ENCOUNTERS = 3;
private static final int MAX_STEPS_BETWEEN_ENCOUNTERS = 8;
```

### Adding More Wild Pokémon:
1. Create the Pokémon class (e.g., `Caterpie.java`)
2. Add it to the encounter table in `generateWildPokemon()`

```java
else if (encounterRoll < 95) {
    return new Caterpie(level);  // 5% chance
}
```

## Integration with BattleManager

The system is designed to work with your existing `BattleManager` class. To fully integrate:

1. **Create a Player/Trainer class** that holds the player's Pokémon party
2. **Connect to BattleManager** in `Player.java`'s `triggerWildBattle()` method:

```java
// Get player's active Pokémon
Pokemon playerPokemon = playerTrainer.getParty().get(0);

// Start battle
BattleManager battleManager = new BattleManager();
boolean won = battleManager.startBattle(
    playerTrainer, 
    playerPokemon, 
    wildPokemon, 
    false  // not a trainer battle
);

// Return to overworld after battle
gp.gameState = GameState.OVERWORLD;
```

## Visual Features

### Battle Screen Components:
- **Background**: Green sky with brown ground
- **Pokémon Sprites**: Front and back sprites from your sprite sheet
- **Info Boxes**: Display name, level, HP bar, and EXP bar
- **HP Bar Colors**:
  - Green: > 50% HP
  - Yellow: 20-50% HP
  - Red: < 20% HP
- **Transition Effect**: Smooth fade-in when battle starts

### Sprite Mapping:
The `BattleScreen` class maps Pokémon names to sprite positions in `pokemon_battle_sprites.png`:
- Row 0: Bulbasaur (front/back)
- Row 1: Charmander (front/back)
- Row 2: Squirtle (front/back)
- Row 3: Pidgey (front/back)
- Row 4: Rattata (front/back)

Adjust the sprite mapping in `getPokemonSprite()` to match your sprite sheet layout.

## Technical Details

### Sprite System:
- Uses the same sprite loading technique as your player sprites
- Loads from a sprite sheet and extracts individual frames
- Supports both front (enemy) and back (player) sprites

### Logic Pattern:
- Follows your existing pattern from the `logic` package
- Uses managers for different game systems
- Separates concerns (encounter logic, battle rendering, game state)

### Performance:
- Only checks for encounters when player moves to a new tile
- Efficient tile lookup using array indices
- Battle rendering only active during battle state

## Future Enhancements

Possible additions to the system:
1. **Repel Items**: Prevent encounters for a certain number of steps
2. **Different Encounter Tables**: Different Pokémon in different areas
3. **Time-based Encounters**: Different Pokémon at different times
4. **Shiny Pokémon**: Rare color variants
5. **Encounter Animations**: More elaborate transition effects
6. **Sound Effects**: Battle music and encounter sounds (you already have audio system)
7. **Fleeing Mechanic**: Allow player to run from battles (already in BattleManager)

## Troubleshooting

### Encounters not triggering:
- Check that you're walking on tile index `2` (tall grass)
- Verify `tile[2].hasWildEncounter = true` in TileManager
- Check console for "A wild X appeared!" messages

### Battle screen not showing:
- Verify `pokemon_battle_sprites.png` exists in `/res/image/`
- Check that sprite sheet dimensions are correct
- Ensure GameState switches to BATTLE

### Sprites not displaying:
- Adjust sprite mapping in `BattleScreen.getPokemonSprite()`
- Verify sprite sheet layout matches the code expectations
- Check sprite dimensions (currently expects 16x16 sprites)

## Notes

- The system uses your existing sprite loading technique
- Integrates seamlessly with your tile-based movement
- Follows your code style and naming conventions
- Ready to connect with your BattleManager for full functionality
- The visual battle screen is a placeholder - you can enhance it further with animations, move effects, etc.
