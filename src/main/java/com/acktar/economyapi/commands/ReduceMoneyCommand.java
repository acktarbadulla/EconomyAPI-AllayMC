package com.acktar.economyapi.commands;

import org.allaymc.api.command.SimpleCommand;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.server.Server;
import org.allaymc.api.entity.interfaces.EntityPlayer;

import com.acktar.economyapi.EconomyAPI;
import com.acktar.economyapi.database.DatabaseHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReduceMoneyCommand extends SimpleCommand {
    public ReduceMoneyCommand() {
        super("reducemoney", "Reduces money from a players balance!");
    }

    @Override
    public void prepareCommandTree(CommandTree tree) {
        tree.getRoot()
                .str("player")
                .doubleNum("money")
                .exec(context -> {
                    String player = context.getResult(0);
                    Double money = context.getResult(1);
                    String moneyyy = String.valueOf(money);
                    DatabaseHandler database = EconomyAPI.INSTANCE.getDatabase();
      
                    // Ensure sender is a player
                    if (!(context.getSender() instanceof EntityPlayer)) {
                        context.getSender().sendText("Usage: /setmoney <player> <balance>");
                        return context.fail();
                    }                     
                       
                    EntityPlayer sender = (EntityPlayer) context.getSender(); // Cast CommandSender to EntityPlayer
                        
                    // Handle player balance
                    if (!database.hasAccount(player)) {
                        String playerNotFound = EconomyAPI.INSTANCE.CONFIG.playerNotFound();
                        if (playerNotFound == null || playerNotFound.isEmpty()) {
                             log.info("PLAYER NOT FOUND OUTPUT format not set in the config");
                             return context.fail();
                        }
                        sender.sendText(playerNotFound);
                        return context.fail();
                    }
                    
                    // Get the message format from the config
                    String formatTemplate = EconomyAPI.INSTANCE.CONFIG.reduceMoneyOutput();

                    if (formatTemplate == null || formatTemplate.isEmpty()) {
                         log.info("PLAYER BALANCE OUTPUT format not set in the config");
                         return context.fail();
                    }

                    // Replace placeholders with actual values
                    String broadcastMessage = formatTemplate
                            .replace("PLAYER", player)
                            .replace("AMOUNT", moneyyy);  // Convert balance to String
                     
                    try {
                        // Set the player's balance
                        boolean success = database.subtractBalance(player, money);
                        if (success) {
                        sender.sendText(broadcastMessage);
                        } else {
                        sender.sendText("Player does not have enough money to reduce");
                        }
                        return context.success();
                    } catch (Exception e) {
                        sender.sendText("An internal error occured while trying to update users balance!");
                        return context.fail();
                    }
                });
    }
}
