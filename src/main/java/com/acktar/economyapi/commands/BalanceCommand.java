package com.acktar.economyapi.commands;

import org.allaymc.api.command.SimpleCommand;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.command.SenderType
import org.allaymc.api.server.Server;
import org.allaymc.api.entity.interfaces.EntityPlayer;

import com.acktar.economyapi.EconomyAPI;
import com.acktar.economyapi.database.DatabaseHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BalanceCommand extends SimpleCommand {
    public BalanceCommand() {
        super("balance", "Display yours or another players balance");
    }

    @Override
    public void prepareCommandTree(CommandTree tree) {
        tree.getRoot()
                .str("player")
                .optional()
                .exec(context -> {
                    String player = context.getResult(0);
                    DatabaseHandler database = EconomyAPI.INSTANCE.getDatabase();
                    
                    if (player.isBlank()) {
                        // Get the balance for the sender
                        double balance = database.getBalance(sender.getDisplayName());
                        
                        // Get the message format from the config
                        String formatTemplate = EconomyAPI.INSTANCE.CONFIG.selfBalanceOutput();

                        if (formatTemplate == null || formatTemplate.isEmpty()) {
                            log.info("SELF BALANCE OUTPUT format not set in the config");
                            return context.fail();
                        }

                        // Replace placeholders with actual values
                        String broadcastMessage = formatTemplate
                                .replace("BALANCE", String.valueOf(balance));  // Convert balance to String
                        
                        sender.sendText(broadcastMessage);
                        return context.success();
                    } else {
                        // Handle player balance
                        if (!database.hasAccount(player)) {
                            String playerNotFound = EconomyAPI.INSTANCE.CONFIG.playerNotFound();
                            if (playerNotFound == null || playerNotFound.isEmpty()) {
                                log.info("PLAYER NOT FOUND OUTPUT Format not set in the config");
                                return context.fail();
                            }
                            sender.sendText(playerNotFound);
                            return context.fail();
                        }
                       
                        double balance = database.getBalance(player);
                        // Get the message format from the config
                        String formatTemplate = EconomyAPI.INSTANCE.CONFIG.playerBalanceOutput();

                        if (formatTemplate == null || formatTemplate.isEmpty()) {
                            log.info("PLAYER BALANCE OUTPUT format not set in the config");
                            return context.fail();
                        }

                        // Replace placeholders with actual values
                        String broadcastMessage = formatTemplate
                                .replace("PLAYER", player)
                                .replace("BALANCE", String.valueOf(balance));  // Convert balance to String
                     
                        sender.sendText(broadcastMessage);
                        return context.success();
                    }
                }, SenderType.PLAYER);
    }
}
