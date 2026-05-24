# Battle UI System - Complete Guide

## 🎮 Overview

I've created a **fully functional GUI battle system** that mimics the original Pokémon games! The system uses **WASD for navigation** and **UIJK as action buttons** (like XYAB on an Xbox controller).

## 🕹️ Controls

### Navigation (WASD)
- **W** - Move Up
- **A** - Move Left  
- **S** - Move Down
- **D** - Move Right

### Action Buttons (UIJK = Xbox XYAB)
- **U** - Confirm/Select (X button)
- **I** - Back/Cancel (Y button)
- **J** - Alternative Confirm (A button)
- **K** - Alternative Back (B button)

### Additional
- **SPACE** - Advance dialogue
- **ENTER** - Advance dialogue

## 📋 Battle Flow

### 1. **Encounter Start**
```
Walk on tall grass → Encounter triggers → Screen fades
→ "A wild [Pokemon] appeared!" → Press U to continue
```

### 2. **Main Menu** (2x2 Grid)
```
┌─────────────┬─────────────┐
│   FIGHT     │    BAG      │
├─────────────┼─────────────┤
│  POKEMON    │    RUN      │
└─────────────┴─────────────┘
```

**Controls:**
- WASD to navigate
- U to select
- Selected option highlighted in yellow

### 3. **Fight Menu** (Move Selection)
Shows your Pokémon's 4 moves in a 2x2 grid:
```
┌──────────────────┬──────────────────┐
│  Tackle          │  Ember           │
│  PP: 35 | NORMAL │  PP: 25 | FIRE   │
├──────────────────┼──────────────────┤
│  Scratch         │  Growl           │
│  PP: 35 | NORMAL │  PP: 40 | NORMAL │
└──────────────────┴──────────────────┘
```

**Controls:**
- WASD to navigate moves
- U to select move
- I to go back to main menu

### 4. **Bag Menu** (Item Categories)
Shows item categories:
- Pokeballs
- Medicine
- Battle Items
- etc.

**Controls:**
- WS to navigate categories
- U to select category
- I to go back

### 5. **Bag Items** (Items in Category)
Shows items in selected category with quantities:
```
Pokeball (x5)
Great Ball (x2)
Ultra Ball (x1)
```

**Controls:**
- WS to navigate items
- U to use item
- I to go back to categories

### 6. **Pokemon Menu** (Party Selection)
Shows your Pokémon party:
```
Charmander Lv5 HP: 20/20
Pidgey Lv3 HP: 15/18
Rattata Lv4 (FAINTED)
```

**Controls:**
- WS to navigate party
- U to switch Pokémon
- I to go back

### 7. **Battle Action** (Move Execution)
Shows battle messages:
```
"Charmander used Ember!"
"It's super effective!"
"Wild Pidgey fainted!"
```

**Controls:**
- U or SPACE to advance dialogue

### 8. **Victory/Defeat**
Shows result and EXP gained:
```
"Wild Pidgey fainted!"
"Charmander gained 42 EXP!"
```

**Controls:**
- U or SPACE to return to overworld

## 🎨 UI Elements

### Main Battle Screen
```
┌─────────────────────────────────────────┐
│  [Enemy Info Box]                       │
│  Name: Pidgey    Lv: 5                  │
│  HP: ████████░░ 16/20                   │
│                                         │
│                    [Enemy Sprite]       │
│                                         │
│─────────────────────────────────────────│
│                                         │
│  [Your Sprite]                          │
│                                         │
│                  [Your Info Box]        │
│                  Name: Charmander Lv: 5 │
│                  HP: ████████████ 20/20 │
│                  EXP: ████░░░░░░ 50/100 │
│                                         │
│  ┌─────────────────────────────────────┐│
│  │  [Battle Menu/Dialogue Box]        ││
│  │                                     ││
│  │  What will Charmander do?          ││
│  │                                     ││
│  │  WASD: Navigate | U: Confirm       ││
│  └─────────────────────────────────────┘│
└─────────────────────────────────────────┘
```

### Info Boxes
- **White background** with black border
- **Pokemon name** and level
- **HP bar** with color coding:
  - Green: > 50% HP
  - Yellow: 20-50% HP
  - Red: < 20% HP
- **EXP bar** (player only) in blue

### Menu Boxes
- **White background** with black border
- **Yellow highlight** on selected option
- **Controls hint** at bottom

### Dialogue Box
- **Large box** at bottom of screen
- **Text wrapping** for long messages
- **Continue indicator** (▼) when more dialogue

## 🔧 Technical Details

### Files Created/Modified

**New Files:**
1. **`BattleUI.java`** - Complete GUI battle system
   - Menu navigation
   - Battle logic integration
   - Dialogue system
   - Input handling

**Modified Files:**
2. **`KeyHandler.java`** - Added battle controls
   - UIJK action buttons
   - Key press queue system
   - Single-press detection

3. **`GamePanel.java`** - Integrated battle UI
   - BattleUI instance
   - Input routing
   - UI rendering

4. **`Player.java`** - Initialize battle UI
   - Trigger battle with UI
   - Pass trainer and Pokémon data

### Battle State Machine

```
INTRO → MAIN_MENU → FIGHT_MENU → BATTLE_ACTION → MAIN_MENU
                  ↓
                BAG_MENU → BAG_ITEMS → BATTLE_ACTION
                  ↓
                POKEMON_MENU → BATTLE_ACTION
                  ↓
                RUN → OVERWORLD
```

### Key Features

✅ **Full Menu System** - Fight, Bag, Pokemon, Run  
✅ **Move Selection** - 4 moves with PP and type display  
✅ **Item System** - Categories and item usage  
✅ **Pokemon Switching** - Party management  
✅ **Battle Logic** - Turn order, damage calculation  
✅ **Dialogue System** - Queue-based messages  
✅ **Enemy AI** - Smart move selection  
✅ **Catching Mechanic** - Pokeball usage  
✅ **EXP System** - Gain EXP after victory  
✅ **Type Effectiveness** - "Super effective!" messages  
✅ **Critical Hits** - Random critical damage  
✅ **STAB Bonus** - Same-type attack bonus  

## 🎯 How to Use

### Starting a Battle
1. Walk on tall grass (tile 2)
2. After 3-8 steps, encounter triggers
3. Screen fades to battle
4. "A wild X appeared!" message
5. Press U to start

### Fighting
1. Select **FIGHT** from main menu
2. Choose a move with WASD
3. Press U to use move
4. Watch battle animation
5. Enemy attacks (if alive)
6. Return to main menu

### Using Items
1. Select **BAG** from main menu
2. Choose category (Pokeballs, Medicine, etc.)
3. Select item
4. Press U to use
5. Enemy attacks (if battle continues)

### Switching Pokemon
1. Select **POKEMON** from main menu
2. Choose Pokemon from party
3. Press U to switch
4. Enemy attacks after switch

### Running Away
1. Select **RUN** from main menu
2. Press U to confirm
3. Return to overworld (wild battles only)

## 🎮 Example Battle Session

```
1. Encounter: "A wild Pidgey appeared!"
   → Press U

2. Main Menu appears
   → Navigate to FIGHT with WASD
   → Press U

3. Move selection appears
   → Navigate to "Ember" with D
   → Press U

4. "Charmander used Ember!"
   "It's super effective!"
   "Wild Pidgey fainted!"
   → Press U

5. "Charmander gained 42 EXP!"
   → Press U

6. Return to overworld
```

## 🐛 Troubleshooting

### Controls Not Working
- Make sure game window has focus
- Check that you're in BATTLE state
- Verify KeyHandler is receiving input

### Menu Not Appearing
- Check that BattleUI is initialized
- Verify battle state is correct
- Check console for errors

### Moves Not Executing
- Ensure Pokemon has moves learned
- Check PP is not 0
- Verify BattleEngine is working

### Items Not Showing
- Check that Bag has items
- Verify item categories exist
- Ensure items are properly added

## 💡 Customization

### Change Controls
Edit `KeyHandler.java`:
```java
if (code == KeyEvent.VK_U) {  // Change VK_U to another key
    uPressed = true;
    addKeyPress("U");
}
```

### Modify Menu Colors
Edit `BattleUI.java` in draw methods:
```java
g2.setColor(new Color(255, 200, 0));  // Yellow highlight
g2.setColor(new Color(248, 248, 248)); // White background
```

### Add More Dialogue
Edit `BattleUI.java`:
```java
addDialogue("Your custom message here!");
```

### Change Menu Layout
Modify grid positions in draw methods:
```java
int optX = boxX + 30 + (col * (boxWidth / 2));
int optY = boxY + 50 + (row * 70);
```

## 🚀 Future Enhancements

Possible additions:
1. **Animations** - Move animations, Pokemon sprites moving
2. **Sound Effects** - Battle sounds, move sounds
3. **Status Effects** - Poison, burn, paralysis
4. **Weather Effects** - Rain, sun, sandstorm
5. **Abilities** - Pokemon abilities
6. **Battle Backgrounds** - Different backgrounds per location
7. **Trainer Battles** - Full trainer battle support
8. **Double Battles** - 2v2 battles
9. **Battle Transitions** - More elaborate transitions
10. **Move Animations** - Visual effects for moves

## 📝 Summary

You now have a **complete, fully functional GUI battle system** that:
- ✅ Uses WASD + UIJK controls
- ✅ Has all battle menus (Fight, Bag, Pokemon, Run)
- ✅ Integrates with your existing battle logic
- ✅ Shows proper UI with info boxes and HP bars
- ✅ Handles turn order and battle flow
- ✅ Supports items, switching, and running
- ✅ Displays battle messages and dialogue
- ✅ Returns to overworld after battle

**Just walk on grass and start battling!** 🎮✨
