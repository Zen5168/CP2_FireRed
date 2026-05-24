# SQLite Driver Fix - Complete Solution

## 🐛 The Problem

```
SQLite Database Error: No suitable driver found for jdbc:sqlite:...
```

The SQLite JDBC driver is not being loaded at runtime, even though it's in the lib folder.

## 🔧 Solution Applied

### 1. **Updated MoveDatabase.java**
- Added automatic database path detection
- Tries multiple possible paths
- Shows exactly where it's looking
- Better error messages

### 2. **Updated project.properties**
- Added SQLite JAR to **runtime classpath**
- Previously it was only in compile classpath

```properties
run.classpath=\
    ${javac.classpath}:\
    ${build.classes.dir}:\
    ${file.reference.sqlite-jdbc-3.53.0.0-natives-all.jar}
```

## 🎯 How to Apply

### Step 1: Clean and Rebuild
1. In NetBeans: **Clean and Build Project** (Shift+F11)
2. Wait for build to complete

### Step 2: Check Console Output
When you run, you should see:
```
✅ SQLite JDBC Driver loaded successfully!
✅ Found database at: src/db/moveDB/movedatabase
   Absolute path: C:\Users\...\CP2_FireRed\src\db\moveDB\movedatabase
```

### Step 3: If Still Not Working

#### Option A: Manually Add JAR to Libraries (NetBeans)
1. Right-click project → **Properties**
2. Go to **Libraries** → **Compile** tab
3. Click **Add JAR/Folder**
4. Navigate to `lib/sqlite-jdbc-3.53.0.0-natives-all.jar`
5. Click **Open**
6. Go to **Run** tab
7. Click **Add JAR/Folder**
8. Add the same JAR again
9. Click **OK**
10. **Clean and Build**

#### Option B: Copy Database to Project Root
If the database path detection fails:
1. Copy `src/db/moveDB/movedatabase` 
2. Paste it in project root (same level as `src/` folder)
3. Run again

#### Option C: Use Absolute Path (Temporary)
Edit `MoveDatabase.java` and add your absolute path:
```java
String[] possiblePaths = {
    "C:\\Users\\Asus\\Downloads\\CP2_FireRed\\src\\db\\moveDB\\movedatabase", // Your absolute path
    "src/db/moveDB/movedatabase",
    "movedatabase",
    // ... rest
};
```

## 📊 Expected Console Output

### Success:
```
✅ SQLite JDBC Driver loaded successfully!
✅ Found database at: src/db/moveDB/movedatabase
   Absolute path: C:\Users\Asus\Downloads\CP2_FireRed\src\db\moveDB\movedatabase
✅ Loaded move: Tackle (Type: NORMAL, PP: 35)
✅ Loaded move: Ember (Type: FIRE, PP: 25)

=== BATTLE INIT DEBUG ===
Player Pokemon: Charmander
Player moves:
  Move 0: Tackle (PP: 35/35, Type: NORMAL)
  Move 1: Ember (PP: 25/25, Type: FIRE)
  Move 2: null
  Move 3: null
========================
```

### If Driver Not Found:
```
❌ SQLite JDBC Driver not found!
Make sure sqlite-jdbc JAR is in your classpath.
Check: lib/sqlite-jdbc-3.53.0.0-natives-all.jar
```
**Solution:** Add JAR to libraries (Option A above)

### If Database Not Found:
```
✅ SQLite JDBC Driver loaded successfully!
❌ Database file not found! Tried:
   - src/db/moveDB/movedatabase
   - movedatabase
   - src\db\moveDB\movedatabase
   - ./src/db/moveDB/movedatabase
   - .\src\db\moveDB\movedatabase
Current working directory: C:\Users\Asus\Downloads\CP2_FireRed
```
**Solution:** Copy database to project root (Option B above)

## 🔍 Debugging Steps

### 1. Check if JAR exists:
```
lib/sqlite-jdbc-3.53.0.0-natives-all.jar
```
Should be present in your project.

### 2. Check if database exists:
```
src/db/moveDB/movedatabase
```
Should be present in your project.

### 3. Check NetBeans Libraries:
- Right-click project → Properties → Libraries
- Look for `sqlite-jdbc-3.53.0.0-natives-all.jar` in both:
  - Compile tab
  - Run tab

### 4. Check Console Output:
Look for the ✅ or ❌ messages to see exactly what's failing.

## 💡 Why This Happens

### Issue 1: Driver Not in Runtime Classpath
- JAR was in **compile** classpath only
- Not in **runtime** classpath
- **Fix:** Added to `run.classpath` in project.properties

### Issue 2: Database Path
- Working directory might be different at runtime
- **Fix:** Try multiple possible paths automatically

### Issue 3: NetBeans Configuration
- Sometimes NetBeans doesn't pick up classpath changes
- **Fix:** Clean and rebuild, or manually add JAR

## 🎮 Alternative: Hardcode Moves (Temporary)

If database still won't work, you can temporarily hardcode moves in Pokemon classes:

**Charmander.java:**
```java
public Charmander(int level) {
    super("Charmander", Type.FIRE, Type.NONE, level, 39, 52, 60, 43, 50, 65);

    // TEMPORARY: Hardcoded moves instead of database
    this.learnMove(new Moves("Tackle", "Physical", Type.NORMAL, 40, 100, 35), 0);
    if (level >= 7) {
        this.learnMove(new Moves("Ember", "Special", Type.FIRE, 40, 100, 25), 1);
    }
}
```

This bypasses the database entirely and creates moves directly.

## 📝 Summary

**Problem:** SQLite driver not loading at runtime  
**Cause:** JAR not in runtime classpath  
**Fix 1:** Added JAR to run.classpath  
**Fix 2:** Auto-detect database path  
**Fix 3:** Better error messages  

**Next Steps:**
1. Clean and Build
2. Run and check console
3. If still failing, manually add JAR to libraries
4. If desperate, use hardcoded moves temporarily

---

**The driver should now load correctly!** 🔥✨
