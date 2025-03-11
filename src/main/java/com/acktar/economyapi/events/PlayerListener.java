package com.acktar.economyapi.events;

import org.allaymc.api.eventbus.event.player.PlayerJoinEvent;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.entity.interfaces.EntityPlayer;

import com.acktar.economyapi.EconomyAPI;
import com.acktar.economyapi.database.DatabaseHandler;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;

@Slf4j
public class PlayerListener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EntityPlayer player = event.getPlayer();
        String playerId = player.getDisplayName();

        try {
        DatabaseHandler database = EconomyAPI.INSTANCE.getDatabase();
        // Check if the player has an account
        if (!database.hasAccount(playerId)) {
        
            double defaultMoney = EconomyAPI.INSTANCE.config.defaultMoney();
            
            if (defaultMoney == 0.0) {
                log.info("DEFAULT-MONEY is not set. Please configure ECONOMY-SETTINGS");
            return;
            }
            
            // Get the message format from the config
            String formatTemplate = EconomyAPI.INSTANCE.config.newPlayerNotify();

            if (formatTemplate == null || formatTemplate.isEmpty()) {
                  log.info("NEW PLAYER NOTIFY OUTPUT format not set in the config");
                  return;
            }

            // Replace placeholders with actual values
            String broadcastMessage = formatTemplate
                  .replace("PLAYER", playerId)
                  .replace("BALANCE", String.valueOf(defaultMoney));  // Convert balance to String
                   
            
            // Create the account with a default balance
            database.createAccount(playerId, defaultMoney);
            player.sendText(broadcastMessage);
            }
          } catch (Exception e) {
            log.error("An error occurred while processing player join event for player {}: {}", playerId, e.getMessage());
        }
      }
}
