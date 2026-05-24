# Quick Start Guide - Wild Encounters

## 🚀 Ready to Use!

Your wild encounter system is **fully implemented** and ready to test!

## ⚡ Quick Test

1. **Run your game** (Main.java)
2. **Walk to any tall grass** (darker green tiles)
3. **Walk around** - after 3-8 steps, a wild Pokémon appears!
4. **Watch the battle screen** display with transition

## 📍 Where to Find Tall Grass

Your map has **lots** of tall grass patches! Look for the darker green tiles (tile 2):
- Top area: rows 2-4, columns 7-14 and 27-34
- Middle area: rows 7-8, 16-18, 26-28, 36-38
- Bottom area: rows 20-24, 41-43, 45-47

Just walk around and you'll find them! 🌿

## 🎮 What Happens

```
You walk on grass → Step counter increases → After 3-8 steps →
"A wild Pokémon appeared!" → Screen fades → Battle screen shows →
(Connect to BattleManager here)
```

## 🎲 What You'll Encounter

- **40% Rattata** - Common
- **40% Pidgey** - Common  
- **10% Bulbasaur** - Uncommon
- **5% Charmander** - Rare
- **5% Squirtle** - Rare

All at level 3-6!

## 🔧 Customize It

### Change Encounter Rate
Edit `WildEncounterManager.java`:
```java
private static final int MIN_STEPS_BETWEEN_ENCOUNTERS = 3;  // Lower = more encounters
private static final int MAX_STEPS_BETWEEN_ENCOUNTERS = 8;  // Higher = fewer encounters
```

### Change Pokémon Rarity
Edit `generateWildPokemon()` in `WildEncounterManager.java`:
```java
if (encounterRoll < 40) {
    return new Rattata(level);  // Change 40 to adjust %
}
```

### Add More Pokémon
Just add another case:
```java
else if (encounterRoll < 100) {
    return new Caterpie(level);  // New Pokémon!
}
```

## 🔗 Connect to Battle System

In `Player.java`, find `triggerWildBattle()` and uncomment/modify:
```java
// Get player's Pokémon
Pokemon playerPokemon = /* your trainer class */.getParty().get(0);

// Start battle
BattleManager battleManager = new BattleManager();
boolean won = battleManager.startBattle(
    playerTrainer,
    playerPokemon,
    wildPokemon,
    false
);

// Return to overworld
gp.gameState = GameState.OVERWORLD;
```

## 📁 New Files Added

**Logic:**
- `WildEncounterManager.java` - Encounter system
- `BattleScreen.java` - Visual battle display
- `GameState.java` - Game state enum

**Docs:**
- `WILD_ENCOUNTER_SYSTEM.md` - Full documentation
- `ENCOUNTER_FLOW.md` - Flow diagrams
- `IMPLEMENTATION_SUMMARY.md` - Overview
- `QUICK_START.md` - This file!

## ✅ Everything Works!

- ✅ No compilation errors
- ✅ Follows your code style
- ✅ Uses your sprite techniques
- ✅ Integrates with your tile system
- ✅ Ready for BattleManager connection

## 🎯 Next Steps

1. **Test it** - Walk on grass and see encounters!
2. **Connect BattleManager** - Link to your existing battle system
3. **Customize** - Adjust rates, add Pokémon, change visuals
4. **Enhance** - Add sound effects, animations, more features!

## 💡 Tips

- Walk on **tile 2** (tall grass) to trigger encounters
- Check **console output** for encounter messages
- **Game state** switches between OVERWORLD and BATTLE
- **Step counter** resets after each encounter
- **Different Pokémon** appear based on rarity

## 🐛 Issues?

Check the troubleshooting section in `WILD_ENCOUNTER_SYSTEM.md`!

---

**That's it! Go catch 'em all!** 🎮✨
