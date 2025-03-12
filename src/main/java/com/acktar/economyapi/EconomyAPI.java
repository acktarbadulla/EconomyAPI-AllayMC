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

    private DatabaseHandler database;
    private static EconomyAPI instance;
    public Config config;
    private boolean enableAscii = true;
    
    @Override
    public void onLoad() {
        instance = this;
        log.info("Loading Configuration file..!");
        config = ConfigManager.create(Config.class, config -> {
            config.withConfigurer(new YamlSnakeYamlConfigurer());
            config.withBindFile(pluginContainer.dataFolder().resolve("config.yml"));
            config.withRemoveOrphans(true);
            config.saveDefaults();
            config.load(true);
        });
    }

    @Override
    public void onEnable() {
        if (enableAscii) displayASCII();
        try {
            initializeDatabase();
            registerCommandsAndEvents();
            log.info("EconomyAPI has been enabled!");
        } catch (Exception e) {
            log.error("Failed to enable EconomyAPI: ", e);
        }
    }

    private void initializeDatabase() {
        String databaseType = config.databaseType();
        switch (databaseType.toUpperCase()) {
            case "SQLITE":
                database = new SQLiteHandler(pluginContainer.dataFolder() + "/economy.db");
                log.info("Using SQLITE as default Database-Provider...");
                break;
            case "MONGODB":
                String mongoURI = config.mongoURI();
                String mongoDBName = config.mongoDBName();
                if (mongoURI.isEmpty() || mongoDBName.isEmpty()) {
                    throw new IllegalArgumentException("MongoURI or MongoDBName is not configured properly. Please configure MongoDB or use SQLITE.");
                }
                database = new MongoDBHandler(mongoURI, mongoDBName);
                log.info("Using MONGODB as Database-Provider...");
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Database Provider detected! Please choose between SQLITE or MONGODB.");
        }
    }

    private void registerCommandsAndEvents() {
        Server.getInstance().getEventBus().registerListener(new PlayerListener());
        Registries.COMMANDS.register(new BalanceCommand());
        Registries.COMMANDS.register(new SetMoneyCommand());
        Registries.COMMANDS.register(new AddMoneyCommand());
        Registries.COMMANDS.register(new ReduceMoneyCommand());
    }

    public void displayASCII() {
        log.info(".   _____  _________  ____  __.________________ __________ ");
        log.info("  /  _  \\ \\_   ___ \\|    |/ _|\\__    ___/  _  \\\\______   \\");
        log.info(" /  /_\\  \\/    \\  \\/|      <  |    | /  /_\\   \\|       _/");
        log.info("/    |    \\     \\___|    |\\    |    |/    |     \\   |   \\");
        log.info("\\____|__  /\\______  /____|__\\  |____| \\____|__   /____|_  /");
        log.info("         \\/        \\/      \\/              \\/      \\/ ");
    }
    
    public static EconomyAPI getInstance(){
        return instance;
    }
    
    public DatabaseHandler getDatabase() {
        return database;
    }
}
