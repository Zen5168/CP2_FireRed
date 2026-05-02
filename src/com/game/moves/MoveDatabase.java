package com.game.moves;

import java.sql.*;
import com.game.logic.*;

public class MoveDatabase {

    // SQLite uses a local file path. "jdbc:sqlite:filename.db"
    // Since your file is 'movedatabase', we point directly to it.
    private static final String URL = "jdbc:sqlite:movedatabase";

    public static Moves getMoveFromDB(String moveName) {
        
        String query = "SELECT * FROM movedatabase WHERE move_name = ?";

        // SQLite does not require USER or PASSWORD
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, moveName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String typeString = rs.getString("move_type");
                
                // Ensure we handle potential nulls or casing from the DB
                Type moveTypeEnum = Type.valueOf(typeString.toUpperCase().trim());

                return new Moves(
                    rs.getString("move_name"),
                    rs.getString("move_category"),
                    moveTypeEnum,
                    rs.getInt("move_power"),
                    rs.getInt("move_accuracy"),
                    rs.getInt("move_pp")
                );
            }
        } catch (SQLException e) {
            System.err.println("SQLite Database Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Enum Error: The type in the DB does not match any Type Enum constants.");
        }
        
        return null; 
    }
}