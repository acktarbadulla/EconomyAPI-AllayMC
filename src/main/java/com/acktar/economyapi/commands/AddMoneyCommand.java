package com.acktar.economyapi.commands;

import org.allaymc.api.command.SimpleCommand;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.command.SenderType;
import org.allaymc.api.entity.interfaces.EntityPlayer;

import com.acktar.economyapi.EconomyAPI;
import com.acktar.economyapi.database.DatabaseHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddMoneyCommand extends SimpleCommand {
    public AddMoneyCommand() {
        super("addmoney", "Add money to a player's balance!");
    }

    @Override
public void prepareCommandTree(CommandTree tree) {
    tree.getRoot()
            .str("player")
            .doubleNum("money")
            .exec((context, sender) -> {
                String player = context.getResult(0);
                Double money = context.getResult(1);
                
                // Prevent negative input
                if (money < 0) {
                    sender.sendText("§l§7[§bEconomyAPI§7] §r§c Money cannot be < 0");
                    return context.fail();
                }

                String moneyStr = String.valueOf(money);
                DatabaseHandler database = EconomyAPI.getInstance().getDatabase();

                try {
                    // Handle player balance
                    if (!database.hasAccount(player)) {
                        String playerNotFound = EconomyAPI.getInstance().config.playerNotFound();
                        if (playerNotFound == null || playerNotFound.isEmpty()) {
                            log.info("PLAYER NOT FOUND OUTPUT format not set in the config");
                            sender.sendText("§l§7[§bEconomyAPI§7] §r§cA Configuration issue was detected! Please report to a server admin.");
                            return context.fail();
                        }
                        sender.sendText(playerNotFound);
                        return context.fail();
                    }

                    // Get the message format from the config
                    String formatTemplate = EconomyAPI.getInstance().config.addMoneyOutput();
                    if (formatTemplate == null || formatTemplate.isEmpty()) {
                        log.info("ADD MONEY OUTPUT format not set in the config");
                        sender.sendText("§l§7[§bEconomyAPI§7] §r§cA Configuration issue was detected! Please report to a server admin.");
                        return context.fail();
                    }

                    // Replace placeholders with actual values
                    String broadcastMessage = formatTemplate
                            .replace("PLAYER", player)
                            .replace("AMOUNT", moneyStr);

                    // Set the player's balance
                    database.addBalance(player, money);
                    sender.sendText(broadcastMessage);
                    return context.success();
                } catch (Exception e) {
                    log.error("An error occurred while trying to update the player's balance", e);
                    sender.sendText("§l§7[§bEconomyAPI§7] §r§cAn internal error occurred while trying to update the user's balance!");
                    return context.fail();
                }
            }, SenderType.PLAYER);
}
