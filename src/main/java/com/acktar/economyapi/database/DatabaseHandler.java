package com.acktar.economyapi.database;

public interface DatabaseHandler {
    boolean hasAccount(String playerId);
    void createAccount(String playerId, double defaultBalance);
    double getBalance(String playerId);
    void setBalance(String playerId, double amount);
    boolean subtractBalance(String playerId, double amount);
    void addBalance(String playerId, double amount);
}
