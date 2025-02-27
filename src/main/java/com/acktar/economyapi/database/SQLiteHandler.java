package com.acktar.economyapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLiteHandler implements DatabaseHandler {
    private Connection connection;

    public SQLiteHandler(String dbPath) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            // Ensure the table exists
            String createTable = "CREATE TABLE IF NOT EXISTS economy_data (" +
                                 "player_id TEXT PRIMARY KEY, " +
                                 "amount DOUBLE NOT NULL DEFAULT 0)";
            connection.prepareStatement(createTable).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if user has an economy account
    public boolean hasAccount(String playerId) {
        try {
            String query = "SELECT 1 FROM economy_data WHERE player_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Create a new economy account with a specified default balance
    public void createAccount(String playerId, double defaultBalance) {
      if (!hasAccount(playerId)) {
        try {
            String query = "INSERT INTO economy_data (player_id, amount) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerId);
            statement.setDouble(2, defaultBalance);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
    }

    // Get the balance of a player
    public double getBalance(String playerId) {
        try {
            String query = "SELECT amount FROM economy_data WHERE player_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Default balance if not found
    }

    // Set the balance of a player
    public void setBalance(String playerId, double amount) {
        try {
            String query = "INSERT INTO economy_data (player_id, amount) VALUES (?, ?) " +
                           "ON CONFLICT(player_id) DO UPDATE SET amount = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerId);
            statement.setDouble(2, amount);
            statement.setDouble(3, amount);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Subtract from a player's balance
    public boolean subtractBalance(String playerId, double amount) {
        double balance = getBalance(playerId);
        if (balance >= amount) {
            setBalance(playerId, balance - amount);
            return true;
        }
        return false; // Not enough balance
    }

    // Add to a player's balance
    public void addBalance(String playerId, double amount) {
        double balance = getBalance(playerId);
        setBalance(playerId, balance + amount);
    }
}