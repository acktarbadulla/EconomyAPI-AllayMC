# EconomyAPI for AllayMC Server Software
Supporting both SQLITE3 and MongoDB
Because MongoDB is the GOAT

![Banner](https://github.com/acktarbadulla/EconomyAPI-AllayMC/blob/master/20250227_210202.jpg)

# Commands
Command | Aliases | Description | use
--- | --- | --- | ---
`/setmoney` | `/sm` | Set A Players Total Balance | op
`/addmoney` | `/am` | Add Money To A Players Balance | op
`/reducemoney` | `/rm` | Reduce Money From A Players Balance | op
`/balance` | `/bal /money` | See Your / Other Players Balance | all

# Repository
```xml
<repositories>
        <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
        </repository>
</repositories>
```

# Dependency
```xml
<dependency>
	    <groupId>com.github.acktarbadulla</groupId>
	    <artifactId>EconomyAPI-AllayMC</artifactId>
	    <version>AllayMC</version>
</dependency>
```

# For Developer
```java
/**
 * GET A PLAYERS BALANCE
 * @param (string) playername
 * @return (double) balance
*/
EconomyAPI.getInstance().getDatabase().getBalance("playername");

/**
 * SET A PLAYERS BALANCE
 * @param (string) playername | (double) balance
 * @return void
*/
EconomyAPI.getInstance().getDatabase().setBalance("playername", 100000.0);

/**
 * ADD MONEY TO A PLAYERS BALANCE
 * @param (string) playername | (double) amount
 * @return void
*/
EconomyAPI.getInstance().getDatabase().addBalance("playername", 500.0);

/**
 * REDUCE A PLAYERS BALANCE
 * @param (string) playername | (double) amount
 * @return (boolean) true if successfull | false if not enough money
*/
EconomyAPI.getInstance().getDatabase().subtractBalance("playername", 500.0);

/**
 * CHECK IF PLAYER HAS ACCOUNT
 * @param (string) playername
 * @return (boolean) true if has account | false if no account
*/
EconomyAPI.getInstance().getDatabase().hasAccount("playername");
```
