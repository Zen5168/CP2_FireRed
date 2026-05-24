# Quick Sprite Setup Example

## Your Current Code (BattleScreen.java)

```java
// INITIALIZE SPRITE ARRAY
pokemonSprites = new BufferedImage[20][2]; // [POKEMON_ID][0=FRONT, 1=BACK]

// MAP EACH POKEMON SPRITE TO ITS LOCATION
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
};
```

## How to Customize

### Example 1: Different Sprite Sizes

```java
Rectangle[][] spriteMap = {
    // BULBASAUR - 40x40 pixels
    {new Rectangle(0, 0, 40, 40), new Rectangle(40, 0, 40, 40)},
    
    // CHARMANDER - 35x35 pixels  
    {new Rectangle(0, 40, 35, 35), new Rectangle(40, 40, 35, 35)},
};
```

### Example 2: Sprites at Random Positions

```java
Rectangle[][] spriteMap = {
    // BULBASAUR - found at (100, 50)
    {new Rectangle(100, 50, 32, 32), new Rectangle(150, 50, 32, 32)},
    
    // CHARMANDER - found at (200, 150)
    {new Rectangle(200, 150, 32, 32), new Rectangle(250, 150, 32, 32)},
};
```

### Example 3: Using Your FMC Sprite Sheet

```java
// Change the file path
BufferedImage spriteSheet = ImageIO.read(
    getClass().getResourceAsStream("/res/image/FMC_Battle_intro_sprite_sheet.png")
);

// Map to wherever your sprites are in that sheet
Rectangle[][] spriteMap = {
    // Find the exact pixel positions in your sheet
    {new Rectangle(10, 20, 50, 50), new Rectangle(70, 20, 50, 50)},
};
```

## Visual Guide

```
Your Sprite Sheet: pokemon_battle_sprites.png

Pixel coordinates:
     0    32   64   96
   ┌────┬────┬────┬────┐
 0 │ B  │ B  │    │    │  B = Bulbasaur
   │FRNT│BACK│    │    │
   ├────┼────┼────┼────┤
32 │ C  │ C  │    │    │  C = Charmander
   │FRNT│BACK│    │    │
   ├────┼────┼────┼────┤
64 │ S  │ S  │    │    │  S = Squirtle
   │FRNT│BACK│    │    │
   └────┴────┴────┴────┘

Rectangle(x, y, width, height):
- Bulbasaur Front: (0, 0, 32, 32)
- Bulbasaur Back:  (32, 0, 32, 32)
- Charmander Front: (0, 32, 32, 32)
- Charmander Back:  (32, 32, 32, 32)
```

## Adding a New Pokémon (Step by Step)

### 1. Add to spriteMap array:

```java
Rectangle[][] spriteMap = {
    // ... existing pokemon ...
    
    // CATERPIE [5] - NEW!
    {new Rectangle(0, 160, 32, 32), new Rectangle(32, 160, 32, 32)},
    //              ↑ X=0, Y=160 (row 5)  ↑ X=32, Y=160
};
```

### 2. Add to switch statement:

```java
switch (pokemonName.toLowerCase()) {
    // ... existing cases ...
    
    case "caterpie":  // NEW!
        spriteIndex = 5;
        break;
}
```

### 3. Done! ✅

## Common Sprite Sheet Layouts

### Layout 1: Two Columns (Current)
```
[Front] [Back]
[Front] [Back]
[Front] [Back]
```

### Layout 2: All Fronts, Then All Backs
```
[Front] [Front] [Front]
[Back]  [Back]  [Back]
```
Change to:
```java
{new Rectangle(0, 0, 32, 32), new Rectangle(0, 32, 32, 32)},  // Col 0, Row 0 & 1
{new Rectangle(32, 0, 32, 32), new Rectangle(32, 32, 32, 32)}, // Col 1, Row 0 & 1
```

### Layout 3: Single Row
```
[Front][Back][Front][Back][Front][Back]
```
Change to:
```java
{new Rectangle(0, 0, 32, 32), new Rectangle(32, 0, 32, 32)},   // Bulbasaur
{new Rectangle(64, 0, 32, 32), new Rectangle(96, 0, 32, 32)},  // Charmander
{new Rectangle(128, 0, 32, 32), new Rectangle(160, 0, 32, 32)},// Squirtle
```

## Finding Sprite Coordinates

### Method 1: Image Editor
1. Open sprite sheet in Paint/Photoshop/GIMP
2. Hover over sprite corner
3. Note the X, Y coordinates
4. Measure width and height

### Method 2: Calculator
If sprites are in a grid:
```
X = column_number × sprite_width
Y = row_number × sprite_height
```

Example: 32x32 sprites, want sprite at column 3, row 2:
```
X = 3 × 32 = 96
Y = 2 × 32 = 64
Rectangle(96, 64, 32, 32)
```

## Testing

Run your game and check console:
```
✅ "Battle sprites loaded successfully!" = Working!
❌ Error message = Check file path and coordinates
```

If you see colored circles instead of sprites:
- 🔴 Red = Enemy (sprite not found)
- 🔵 Blue = Player (sprite not found)

This means the sprite loading failed - check your Rectangle coordinates!

## Summary

**Same as TileManager:**
- ✅ Rectangle arrays for precise cropping
- ✅ Easy to add new sprites
- ✅ Complete control over positions
- ✅ Support for any sprite size

**Just update the Rectangle coordinates to match YOUR sprite sheet layout!** 🎨
