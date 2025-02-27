package com.acktar.economyapi;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Config extends OkaeriConfig {

@Comment("--------------------------------------")
@Comment("DATABASE CONFIGURATION")
@Comment("--------------------------------------")

@Comment("DATABASE TYPE | SQLITE / MONGODB")
@CustomKey("databaseType")
private String databaseType = "SQLITE";

@Comment("MONGODB URI - IF SET TO MONGODB")
@CustomKey("mongoURI")
private String mongoURI = " ";

@Comment("MONGODB DATABASE NAME - IF SET TO MONGODB")
@Comment("If you face errors like plugin not responding make the database and the collection manually")
@Comment("Collection name should be economy_data")
@CustomKey("mongoDBName")
private String mongoDBName = "economy";

@Comment("--------------------------------------")
@Comment("ECONOMY CONFIGURATION")
@Comment("--------------------------------------")

@Comment("DEFAULT MONEY")
@CustomKey("defaultMoney")
private Double defaultMoney = 1000.0;

@Comment("--------------------------------------")
@Comment("TRANSLATION CONFIGURATION")
@Comment("--------------------------------------")

@Comment("NEW PLAYER NOTIFY OUTPUT")
@CustomKey("newPlayerNotify")
private String newPlayerNotify = "§l§7[§bEconomyAPI§7] §rWelcome to the server §ePLAYER! You have been granted §eBALANCE$§r as a starting bonus!";

@Comment("/balance SELF COMMAND OUTPUT")
@CustomKey("selfBalanceOutput")
private String selfBalanceOutput = "§l§7[§bEconomyAPI§7] §rYou have §eBALANCE$§r available!";

@Comment("/balance PLAYER COMMAND OUTPUT")
@CustomKey("playerBalanceOutput")
private String playerBalanceOutput = "§l§7[§bEconomyAPI§7] §r§ePLAYER§r have §eBALANCE$§r available!";

@Comment("/setmoney PLAYER COMMAND OUTPUT")
@CustomKey("setMoneyOutput")
private String setMoneyOutput = "§l§7[§bEconomyAPI§7] §r§ePLAYER's§r balance has been updated to §eBALANCE!";

@Comment("/addmoney PLAYER COMMAND OUTPUT")
@CustomKey("addMoneyOutput")
private String addMoneyOutput = "§l§7[§bEconomyAPI§7] Successfully added §eAMOUNT$§r to §r§ePLAYER's§r balance!";

@Comment("/reducemoney PLAYER COMMAND OUTPUT")
@CustomKey("reduceMoneyOutput")
private String reduceMoneyOutput = "§l§7[§bEconomyAPI§7] Successfully removed §eAMOUNT$§r from §r§ePLAYER's§r balance!";

@Comment("PLAYER NOT FOUND COMMAND OUTPUT")
@CustomKey("playerNotFound")
private String playerNotFound = "§l§7[§bEconomyAPI§7] §r§cMentioned player was not found in the database!";

}
