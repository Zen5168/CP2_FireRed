# Pokémon Sprite Mapping Guide

## Overview

The battle sprite system now uses **Rectangle arrays** just like your TileManager! This gives you precise control over which sprites to crop from your sprite sheet.

## How It Works

### Same Pattern as TileManager ✅

**Your TileManager:**
```java
Rectangle[] tileSet1 = {
    new Rectangle(1, 52, 16, 16),   // Tile[0] - GRASS PATH
    new Rectangle(103, 1, 16, 16),  // Tile[1] - GRASS PATH VIBRANT
    new Rectangle(120, 1, 16, 16),  // Tile[2] - TALL GRASS
};
```

**BattleScreen (Same Pattern!):**
```java
Rectangle[][] spriteMap = {
    // BULBASAUR [0]
    {new Rectangle(0, 0, 32, 32), new Rectangle(32, 0, 32, 32)},
    //            ↑ FRONT SPRITE    ↑ BACK SPRITE
    
    // CHARMANDER [1]
    {new Rectangle(0, 32, 32, 32), new Rectangle(32, 32, 32, 32)},
};
```

## Sprite Sheet Format

### Structure:
```
pokemon_battle_sprites.png

┌─────────────┬─────────────┐
│ Bulbasaur   │ Bulbasaur   │  Row 0 (y=0)
│ FRONT       │ BACK        │
│ (0,0,32,32) │ (32,0,32,32)│
├─────────────┼─────────────┤
│ Charmander  │ Charmander  │  Row 1 (y=32)
│ FRONT       │ BACK        │
│ (0,32,32,32)│(32,32,32,32)│
├─────────────┼─────────────┤
│ Squirtle    │ Squirtle    │  Row 2 (y=64)
│ FRONT       │ BACK        │
├─────────────┼─────────────┤
│ Pidgey      │ Pidgey      │  Row 3 (y=96)
│ FRONT       │ BACK        │
├─────────────┼─────────────┤
│ Rattata     │ Rattata     │  Row 4 (y=128)
│ FRONT       │ BACK        │
└─────────────┴─────────────┘
```

### Sprite Types:
- **FRONT (Column 0)**: Enemy Pokémon facing right
- **BACK (Column 1)**: Player Pokémon facing left (back view)

## Adding New Pokémon

### Step 1: Add to Sprite Map

In `BattleScreen.java`, find the `spriteMap` array and add your Pokémon:

```java
Rectangle[][] spriteMap = {
    // BULBASAUR [0]
    {new Rectangle(0, 0, 32, 32), new Rectangle(32, 0, 32, 32)},
    
    // CHARMANDER [1]
    {new Rectangle(0, 32, 32, 32), new Rectangle(32, 32, 32, 32)},
    
    // SQUIRTLE [2]
    {new Rectangle(0, 64, 32, 32), new Rectangle(32, 64, 32, 32)},
    
    // PIDGEY [3]
    {new Rectangle(0, 96, 32, 32), new Rectangle(32, 96, 32, 32)},
    
    // RATTATA [4]
    {new Rectangle(0, 128, 32, 32), new Rectangle(32, 128, 32, 32)},
    
    // CATERPIE [5] - NEW!
    {new Rectangle(0, 160, 32, 32), new Rectangle(32, 160, 32, 32)},
    //              ↑ X, Y, Width, Height
};
```

### Step 2: Add to Name Mapping

In the `getPokemonSprite()` method, add the case:

```java
switch (pokemonName.toLowerCase()) {
    case "bulbasaur":
        spriteIndex = 0;
        break;
    case "charmander":
        spriteIndex = 1;
        break;
    case "squirtle":
        spriteIndex = 2;
        break;
    case "pidgey":
        spriteIndex = 3;
        break;
    case "rattata":
        spriteIndex = 4;
        break;
    case "caterpie":  // NEW!
        spriteIndex = 5;
        break;
}
```

### Step 3: Done! ✅

The system will automatically load and display your new Pokémon sprites.

## Rectangle Parameters

```java
new Rectangle(x, y, width, height)
```

- **x**: Horizontal position (pixels from left)
- **y**: Vertical position (pixels from top)
- **width**: Sprite width in pixels
- **height**: Sprite height in pixels

### Example Calculations:

**If your sprites are 32x32 and arranged in 2 columns:**

| Pokémon    | Index | Front Sprite (x, y)  | Back Sprite (x, y)   |
|------------|-------|----------------------|----------------------|
| Bulbasaur  | 0     | (0, 0)               | (32, 0)              |
| Charmander | 1     | (0, 32)              | (32, 32)             |
| Squirtle   | 2     | (0, 64)              | (32, 64)             |
| Pidgey     | 3     | (0, 96)              | (32, 96)             |
| Rattata    | 4     | (0, 128)             | (32, 128)            |
| Caterpie   | 5     | (0, 160)             | (32, 160)            |

**Formula:**
- Front X = 0
- Front Y = index × sprite_height
- Back X = sprite_width
- Back Y = index × sprite_height

## Different Sprite Sizes

### If your sprites are different sizes:

```java
Rectangle[][] spriteMap = {
    // BULBASAUR - 40x40 pixels
    {new Rectangle(0, 0, 40, 40), new Rectangle(40, 0, 40, 40)},
    
    // CHARMANDER - 35x38 pixels
    {new Rectangle(0, 40, 35, 38), new Rectangle(40, 40, 35, 38)},
    
    // SQUIRTLE - 32x32 pixels
    {new Rectangle(0, 78, 32, 32), new Rectangle(40, 78, 32, 32)},
};
```

### If sprites are scattered randomly:

```java
Rectangle[][] spriteMap = {
    // BULBASAUR - anywhere on the sheet!
    {new Rectangle(150, 200, 32, 32), new Rectangle(300, 450, 32, 32)},
    
    // CHARMANDER - different location
    {new Rectangle(50, 100, 32, 32), new Rectangle(200, 100, 32, 32)},
};
```

**You have complete freedom!** Just like your tile system.

## Using Your Existing Sprite Sheet

If you want to use `FMC_Battle_intro_sprite_sheet.png` or any other sheet:

### Option 1: Change the File Path

In `loadBattleAssets()`:
```java
BufferedImage spriteSheet = ImageIO.read(
    getClass().getResourceAsStream("/res/image/FMC_Battle_intro_sprite_sheet.png")
);
```

### Option 2: Use Multiple Sprite Sheets

```java
private void loadBattleAssets() {
    try {
        // LOAD SHEET 1
        BufferedImage sheet1 = ImageIO.read(
            getClass().getResourceAsStream("/res/image/pokemon_battle_sprites.png")
        );
        
        // LOAD SHEET 2
        BufferedImage sheet2 = ImageIO.read(
            getClass().getResourceAsStream("/res/image/FMC_Battle_intro_sprite_sheet.png")
        );
        
        // MAP FROM SHEET 1
        Rectangle[][] map1 = {
            {new Rectangle(0, 0, 32, 32), new Rectangle(32, 0, 32, 32)},
        };
        
        // MAP FROM SHEET 2
        Rectangle[][] map2 = {
            {new Rectangle(10, 20, 40, 40), new Rectangle(60, 20, 40, 40)},
        };
        
        // EXTRACT FROM BOTH SHEETS
        // ... your extraction logic
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## Adjusting Array Size

If you have more than 20 Pokémon:

```java
pokemonSprites = new BufferedImage[50][2]; // 50 Pokémon, 2 sprites each
```

## Testing Your Sprites

### 1. Check Console Output

When the game starts, you should see:
```
Battle sprites loaded successfully!
```

If you see an error, check:
- File path is correct
- Sprite sheet exists in `/res/image/`
- Rectangle coordinates are within the image bounds

### 2. Trigger an Encounter

Walk on tall grass and check if sprites display correctly.

### 3. If Sprites Don't Show

The system will show **colored circles** as fallback:
- 🔴 Red circle = Enemy Pokémon
- 🔵 Blue circle = Your Pokémon

This means the sprite wasn't found. Check:
- Pokémon name matches the switch case
- Sprite index is within array bounds
- Rectangle coordinates are correct

## Example: Adding All Gen 1 Starters

```java
Rectangle[][] spriteMap = {
    // BULBASAUR [0]
    {new Rectangle(0, 0, 32, 32), new Rectangle(32, 0, 32, 32)},
    
    // IVYSAUR [1]
    {new Rectangle(0, 32, 32, 32), new Rectangle(32, 32, 32, 32)},
    
    // VENUSAUR [2]
    {new Rectangle(0, 64, 32, 32), new Rectangle(32, 64, 32, 32)},
    
    // CHARMANDER [3]
    {new Rectangle(0, 96, 32, 32), new Rectangle(32, 96, 32, 32)},
    
    // CHARMELEON [4]
    {new Rectangle(0, 128, 32, 32), new Rectangle(32, 128, 32, 32)},
    
    // CHARIZARD [5]
    {new Rectangle(0, 160, 32, 32), new Rectangle(32, 160, 32, 32)},
    
    // SQUIRTLE [6]
    {new Rectangle(0, 192, 32, 32), new Rectangle(32, 192, 32, 32)},
    
    // WARTORTLE [7]
    {new Rectangle(0, 224, 32, 32), new Rectangle(32, 224, 32, 32)},
    
    // BLASTOISE [8]
    {new Rectangle(0, 256, 32, 32), new Rectangle(32, 256, 32, 32)},
};
```

Then add all cases to the switch statement!

## Tips

1. **Use a sprite sheet viewer** to find exact pixel coordinates
2. **Keep sprites organized** in your sheet (by type, evolution, etc.)
3. **Use consistent sizes** when possible (easier to calculate)
4. **Test one Pokémon at a time** when adding new sprites
5. **Comment your mappings** so you remember what's where

## Summary

✅ **Same pattern as TileManager** - Rectangle arrays for precise control  
✅ **Easy to add new Pokémon** - Just add Rectangle and switch case  
✅ **Flexible sprite sizes** - Each sprite can be different dimensions  
✅ **Multiple sprite sheets** - Can load from different files  
✅ **Fallback graphics** - Shows circles if sprites not found  

You now have complete control over your battle sprites, just like your tiles! 🎨
