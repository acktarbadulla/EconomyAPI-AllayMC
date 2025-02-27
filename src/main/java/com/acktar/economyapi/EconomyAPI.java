package com.acktar.economyapi;

import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.server.Server;
import org.allaymc.api.registry.Registries;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;

import com.acktar.economyapi.database.DatabaseHandler;
import com.acktar.economyapi.database.SQLiteHandler;
import com.acktar.economyapi.database.MongoDBHandler;
import com.acktar.economyapi.events.PlayerListener;
import com.acktar.economyapi.commands.BalanceCommand;
import com.acktar.economyapi.commands.SetMoneyCommand;
import com.acktar.economyapi.commands.AddMoneyCommand;
import com.acktar.economyapi.commands.ReduceMoneyCommand;

@Slf4j
public class EconomyAPI extends Plugin {

    public DatabaseHandler DATABASE;
    public static EconomyAPI INSTANCE;
    public Config CONFIG;
    
    @Override
    public void onLoad() {
        INSTANCE = this;
        log.info("Loading Configuration file..!");
        CONFIG = ConfigManager.create(Config.class, config -> {
            config.withConfigurer(new YamlSnakeYamlConfigurer());
            config.withBindFile(pluginContainer.dataFolder().resolve("config.yml"));
            config.withRemoveOrphans(true);
            config.saveDefaults();
            config.load(true);
        });
    }

    @Override
    public void onEnable() {
        // Initialize database
        if (CONFIG.databaseType().equals("SQLITE")) {
            DATABASE = new SQLiteHandler(pluginContainer.dataFolder() + "/economy.db");
            log.info("Using SQLITE as default Database-Provider...");
        } else if (CONFIG.databaseType().equals("MONGODB")) {
        
            String mongoURI = CONFIG.mongoURI();
            if (mongoURI.isEmpty()) {
                log.info("MongoURI is not set. Please configure MongoDB or use SQLITE");
            return;
            }
            
            String mongoDBName = CONFIG.mongoDBName();
            if (mongoDBName.isEmpty()) {
                log.info("MongoDBName is not set. Please configure MongoDB or use SQLITE");
            return;
            }
            
            DATABASE = new MongoDBHandler(mongoURI, mongoDBName);
            log.info("Using MONGODB as Database-Provider...");
        } else {
            log.info("Unsupported Database Provider detected! Please choose between SQLITE or MONGODB");
        }
        
        Server.getInstance().getEventBus().registerListener(new PlayerListener());
        
        Registries.COMMANDS.register(new BalanceCommand());
        Registries.COMMANDS.register(new SetMoneyCommand());
        Registries.COMMANDS.register(new AddMoneyCommand());
        Registries.COMMANDS.register(new ReduceMoneyCommand());
       
        log.info("EconomyAPI has been enabled!");
    }
    
    public static EconomyAPI getInstance(){
        return INSTANCE;
    }
    
    public DatabaseHandler getDatabase() {
        return DATABASE;
    }
}