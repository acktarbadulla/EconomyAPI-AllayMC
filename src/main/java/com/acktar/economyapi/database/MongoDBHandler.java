package com.acktar.economyapi.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoDBHandler implements DatabaseHandler {
    private final MongoDatabase database;

    public MongoDBHandler(String uri, String dbName) {
        MongoClient client = MongoClients.create(uri);
        database = client.getDatabase(dbName);
    }
    
    @Override
    public boolean hasAccount(String playerId) {
        try {
            Document document = database.getCollection("economy_data")
                    .find(Filters.eq("player_id", playerId))
                    .first();
            return document != null;
        } catch (Exception e) {
            log.error("Failed to check account for player {}: {}", playerId, e.getMessage());
            return false;
        }
    }

    @Override
    public void createAccount(String playerId, double defaultBalance) {
        if (!hasAccount(playerId)) {
            try {
                Document document = new Document("player_id", playerId)
                        .append("amount", defaultBalance);
                database.getCollection("economy_data").insertOne(document);
            } catch (Exception e) {
                log.error("Failed to create account for player {}: {}", playerId, e.getMessage());
            }
        }
    }

    @Override
    public double getBalance(String playerId) {
        try {
            Document document = database.getCollection("economy_data")
                    .find(Filters.eq("player_id", playerId))
                    .first();
            return document != null ? document.getDouble("amount") : 0;
        } catch (Exception e) {
            log.error("Failed to get balance for player {}: {}", playerId, e.getMessage());
            return 0;
        }
    }

    @Override
    public void setBalance(String playerId, double amount) {
        try {
            Document document = new Document("player_id", playerId)
                    .append("amount", amount);
            database.getCollection("economy_data")
                    .replaceOne(Filters.eq("player_id", playerId), document, new ReplaceOptions().upsert(true));
        } catch (Exception e) {
            log.error("Failed to set balance for player {}: {}", playerId, e.getMessage());
        }
    }

    @Override
    public boolean subtractBalance(String playerId, double amount) {
        try {
            double balance = getBalance(playerId);
            if (balance >= amount) {
                setBalance(playerId, balance - amount);
                return true;
            }
            return false; // Not enough balance
        } catch (Exception e) {
            log.error("Failed to subtract balance for player {}: {}", playerId, e.getMessage());
            return false;
        }
    }

    @Override
    public void addBalance(String playerId, double amount) {
        try {
            double balance = getBalance(playerId);
            setBalance(playerId, balance + amount);
        } catch (Exception e) {
            log.error("Failed to add balance for player {}: {}", playerId, e.getMessage());
        }
    }
}
