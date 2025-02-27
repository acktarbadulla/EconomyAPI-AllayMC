package com.acktar.economyapi.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class MongoDBHandler implements DatabaseHandler {
    private final MongoDatabase database;

    public MongoDBHandler(String uri, String dbName) {
        MongoClient client = MongoClients.create(uri);
        database = client.getDatabase(dbName);
    }
    
    // Check if user has an economy account
    public boolean hasAccount(String playerId) {
        Document document = database.getCollection("economy_data")
                .find(Filters.eq("player_id", playerId))
                .first();
        return document != null;
    }

    // Create a new economy account with a specified default balance
    public void createAccount(String playerId, double defaultBalance) {
      if (!hasAccount(playerId)) {
        Document document = new Document("player_id", playerId)
                .append("amount", defaultBalance);
        database.getCollection("economy_data").insertOne(document);
      }
    }

    // Get the balance of a player
    public double getBalance(String playerId) {
        Document document = database.getCollection("economy_data")
                .find(Filters.eq("player_id", playerId))
                .first();
        return document != null ? document.getDouble("amount") : 0;
    }

    // Set the balance of a player
    public void setBalance(String playerId, double amount) {
        Document document = new Document("player_id", playerId)
                .append("amount", amount);
        database.getCollection("economy_data")
                .replaceOne(Filters.eq("player_id", playerId), document, new ReplaceOptions().upsert(true));
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
