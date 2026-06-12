package com.game.moves;

import java.sql.*;
import com.game.logic.*;
import java.io.*;
import java.nio.file.*;

public class MoveDatabase {

    private static String URL = null;

    static {
        try {
            // LOAD THE SQLITE JDBC DRIVER
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver loaded successfully!");

            // EXTRACT THE DATABASE FILE FROM THE JAR'S RESOURCES TO A TEMP FILE
            InputStream in = MoveDatabase.class.getResourceAsStream("/db/movedatabase");

            if (in == null) {
                System.err.println("Database resource not found in jar at /db/movedatabase.db");
                System.err.println("Make sure the file is placed at src/main/resources/db/movedatabase.db");
            } else {
                File tempDb = File.createTempFile("movedatabase", ".db");
                tempDb.deleteOnExit();

                Files.copy(in, tempDb.toPath(), StandardCopyOption.REPLACE_EXISTING);
                in.close();

                URL = "jdbc:sqlite:" + tempDb.getAbsolutePath();
                System.out.println("Database extracted to: " + tempDb.getAbsolutePath());
            }

        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found!");
            System.err.println("Make sure sqlite-jdbc JAR is in your classpath.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to extract database from jar!");
            e.printStackTrace();
        }
    }

    public static Moves getMoveFromDB(String moveName) {

        if (URL == null) {
            System.err.println("Cannot load move '" + moveName + "' - database not initialized!");
            return null;
        }

        String query = "SELECT * FROM movedatabase WHERE move_name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, moveName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String typeString = rs.getString("move_type");

                // HANDLES POTENTIAL NULLS OR CASING FROM THE DB
                Type moveTypeEnum = Type.valueOf(typeString.toUpperCase().trim());

                Moves move = new Moves(
                    rs.getString("move_name"),
                    rs.getString("move_category"),
                    moveTypeEnum,
                    rs.getInt("move_power"),
                    rs.getInt("move_accuracy"),
                    rs.getInt("move_pp")
                );

                System.out.println("Loaded move: " + move.moveName + " (Type: " + move.moveType + ", PP: " + move.pp + ")");
                return move;

            } else {
                System.err.println("Move not found in database: " + moveName);
            }

        } catch (SQLException e) {
            System.err.println("SQLite Database Error: " + e.getMessage());
            System.err.println("Database URL: " + URL);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Enum Error: The type in the DB does not match any Type Enum constants.");
            System.err.println("Move name: " + moveName);
            e.printStackTrace();
        }

        return null;
    }
}