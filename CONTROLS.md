# 🎮 Game Controls Reference

## Overworld (Walking Around)

| Key | Action |
|-----|--------|
| **W** | Move Up |
| **A** | Move Left |
| **S** | Move Down |
| **D** | Move Right |
| **CTRL** | Run (hold while moving) |

## Battle Controls

### Navigation
| Key | Action |
|-----|--------|
| **W** | Navigate Up |
| **A** | Navigate Left |
| **S** | Navigate Down |
| **D** | Navigate Right |

### Action Buttons (Xbox Controller Equivalent)
| Key | Xbox Button | Action |
|-----|-------------|--------|
| **U** | X | Confirm / Select |
| **I** | Y | Back / Cancel |
| **J** | A | Alternative Confirm |
| **K** | B | Alternative Back |
| **SPACE** | - | Advance Dialogue |
| **ENTER** | - | Advance Dialogue |

## Quick Reference

### In Battle:
```
┌─────────────────────────────────────┐
│  WASD = Navigate Menus              │
│  U    = Confirm Selection           │
│  I    = Go Back / Cancel            │
│  SPACE= Skip Dialogue               │
└─────────────────────────────────────┘
```

### Main Menu:
```
┌──────────┬──────────┐
│  FIGHT   │   BAG    │  ← Use WASD to navigate
├──────────┼──────────┤
│ POKEMON  │   RUN    │  ← Press U to select
└──────────┴──────────┘
```

### Move Selection:
```
┌──────────┬──────────┐
│  Move 1  │  Move 2  │  ← Use WASD to navigate
├──────────┼──────────┤
│  Move 3  │  Move 4  │  ← Press U to use move
└──────────┴──────────┘     Press I to go back
```

## Tips

💡 **Hold CTRL** while walking to run faster  
💡 **Press U or SPACE** to quickly advance dialogue  
💡 **Press I** to go back in any menu  
💡 **Yellow highlight** shows your current selection  
💡 **Walk on tall grass** (darker green) to find wild Pokémon  

## Control Scheme Comparison

| Action | Keyboard | Xbox Controller |
|--------|----------|-----------------|
| Navigate | WASD | D-Pad / Left Stick |
| Confirm | U | X Button |
| Back | I | Y Button |
| Alt Confirm | J | A Button |
| Alt Back | K | B Button |

## Customization

Want to change controls? Edit `KeyHandler.java`:
```java
// Find this section and change the key codes
if (code == KeyEvent.VK_U) {  // Change VK_U to your preferred key
    uPressed = true;
    addKeyPress("U");
}
```

Available key codes:
- `VK_A` through `VK_Z` - Letter keys
- `VK_0` through `VK_9` - Number keys
- `VK_SPACE` - Space bar
- `VK_ENTER` - Enter key
- `VK_SHIFT` - Shift key
- `VK_CONTROL` - Control key
- `VK_UP`, `VK_DOWN`, `VK_LEFT`, `VK_RIGHT` - Arrow keys

---

**Have fun playing!** 🎮✨
