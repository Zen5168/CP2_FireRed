package com.game.moves;

import java.sql.*;
import com.game.logic.*;
import java.io.File;

public class MoveDatabase {

    // SQLite database path - try multiple possible locations
    private static String URL = null;
    
    // Static block to load SQLite JDBC driver and find database
    static {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC Driver loaded successfully!");
            
            // Try to find the database file
            String[] possiblePaths = {
                "src/db/moveDB/movedatabase",
                "movedatabase",
                "src\\db\\moveDB\\movedatabase",
                "./src/db/moveDB/movedatabase",
                ".\\src\\db\\moveDB\\movedatabase"
            };
            
            for (String path : possiblePaths) {
                File dbFile = new File(path);
                if (dbFile.exists()) {
                    URL = "jdbc:sqlite:" + path;
                    System.out.println("✅ Found database at: " + path);
                    System.out.println("   Absolute path: " + dbFile.getAbsolutePath());
                    break;
                }
            }
            
            if (URL == null) {
                System.err.println("❌ Database file not found! Tried:");
                for (String path : possiblePaths) {
                    System.err.println("   - " + path);
                }
                System.err.println("Current working directory: " + new File(".").getAbsolutePath());
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQLite JDBC Driver not found!");
            System.err.println("Make sure sqlite-jdbc JAR is in your classpath.");
            System.err.println("Check: lib/sqlite-jdbc-3.53.0.0-natives-all.jar");
            e.printStackTrace();
        }
    }

    public static Moves getMoveFromDB(String moveName) {
        
        if (URL == null) {
            System.err.println("❌ Cannot load move '" + moveName + "' - database not initialized!");
            return null;
        }
        
        String query = "SELECT * FROM movedatabase WHERE move_name = ?";

        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, moveName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String typeString = rs.getString("move_type");
                
                // Ensure we handle potential nulls or casing from the DB
                Type moveTypeEnum = Type.valueOf(typeString.toUpperCase().trim());

                Moves move = new Moves(
                    rs.getString("move_name"),
                    rs.getString("move_category"),
                    moveTypeEnum,
                    rs.getInt("move_power"),
                    rs.getInt("move_accuracy"),
                    rs.getInt("move_pp")
                );
                
                System.out.println("✅ Loaded move: " + move.moveName + " (Type: " + move.moveType + ", PP: " + move.pp + ")");
                return move;
            } else {
                System.err.println("❌ Move not found in database: " + moveName);
            }
        } catch (SQLException e) {
            System.err.println("❌ SQLite Database Error: " + e.getMessage());
            System.err.println("Database URL: " + URL);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Enum Error: The type in the DB does not match any Type Enum constants.");
            System.err.println("Move name: " + moveName);
            e.printStackTrace();
        }
        
        return null; 
    }
}