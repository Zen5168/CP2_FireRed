package com.game.moves;

import java.sql.*;
import com.game.moves.*;
import com.game.logic.*;

import java.sql.*;

public class MoveDatabase {

    private static final String URL = "jdbc:mysql://localhost:3306/movedatabase?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Moves getMoveFromDB(String moveName) {
        
        String query = "SELECT * FROM move_database WHERE move_name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, moveName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String typeString = rs.getString("move_type");
                
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
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Enum Error: The type in the DB does not match any Type Enum constants.");
        }
        
        return null; 
    }
}