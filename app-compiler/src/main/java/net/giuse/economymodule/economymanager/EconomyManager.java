package net.giuse.economymodule.economymanager;

import net.giuse.economymodule.EconomyModule;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.inject.Inject;
import java.util.List;

public class EconomyManager implements Economy {
    private final EconomyModule economyModule;

    @Inject
    public EconomyManager(EconomyModule economyModule) {
        this.economyModule = economyModule;
    }

    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return "LifeServer";
    }

    public boolean hasBankSupport() {
        return false;
    }

    public int fractionalDigits() {
        return 0;
    }

    public String format(final double amount) {
        return null;
    }

    public String currencyNamePlural() {
        return "moneys";
    }

    public String currencyNameSingular() {
        return "money";
    }

    public boolean hasAccount(final String playerName) {
        return this.economyModule.getEconPlayerIsPresent(Bukkit.getPlayer(playerName).getUniqueId());
    }

    public boolean hasAccount(final OfflinePlayer player) {
        return this.economyModule.getEconPlayerIsPresent(player.getUniqueId());
    }

    public boolean hasAccount(final String playerName, final String worldName) {
        return this.economyModule.getEconPlayerIsPresent(Bukkit.getPlayer(playerName).getUniqueId());
    }

    public boolean hasAccount(final OfflinePlayer player, final String worldName) {
        return this.economyModule.getEconPlayerIsPresent(player.getUniqueId());
    }

    public double getBalance(final String playerName) {
        return this.economyModule.getBalancePlayer(Bukkit.getPlayer(playerName).getUniqueId());
    }

    public double getBalance(final OfflinePlayer player) {
        return this.getBalance(player.getName());
    }

    public double getBalance(final String playerName, final String world) {
        return this.getBalance(playerName);
    }

    public double getBalance(final OfflinePlayer player, final String world) {
        return this.getBalance(player.getName());
    }

    public boolean has(final String playerName, final double amount) {
        return economyModule.getBalancePlayer(Bukkit.getPlayer(playerName).getUniqueId()) >= amount;
    }

    public boolean has(final OfflinePlayer player, final double amount) {
        return this.has(player.getName(), amount);
    }

    public boolean has(final String playerName, final String worldName, final double amount) {
        return this.has(playerName, amount);
    }

    public boolean has(final OfflinePlayer player, final String worldName, final double amount) {
        return this.has(player.getName(), amount);
    }

    public EconomyResponse withdrawPlayer(final String playerName, final double amount) {
        economyModule.setBalance(Bukkit.getPlayer(playerName).getUniqueId(), economyModule.getBalancePlayer(Bukkit.getPlayer(playerName).getUniqueId()) - amount);
        return new EconomyResponse(amount, economyModule.getBalancePlayer(Bukkit.getPlayer(playerName).getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "not yet Supported");
    }

    public EconomyResponse withdrawPlayer(final OfflinePlayer player, final double amount) {
        return this.withdrawPlayer(player.getName(), amount);
    }

    public EconomyResponse withdrawPlayer(final String playerName, final String worldName, final double amount) {
        return this.withdrawPlayer(playerName, amount);
    }

    public EconomyResponse withdrawPlayer(final OfflinePlayer player, final String worldName, final double amount) {
        return this.withdrawPlayer(player.getName(), amount);
    }

    public EconomyResponse depositPlayer(final String playerName, final double amount) {
        economyModule.setBalance(Bukkit.getPlayer(playerName).getUniqueId(), economyModule.getBalancePlayer(Bukkit.getPlayer(playerName).getUniqueId()) + amount);
        return new EconomyResponse(amount, economyModule.getBalancePlayer(Bukkit.getPlayer(playerName).getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "not yet Supported");
    }

    public EconomyResponse depositPlayer(final OfflinePlayer player, final double amount) {
        return this.depositPlayer(player.getName(), amount);
    }

    public EconomyResponse depositPlayer(final String playerName, final String worldName, final double amount) {
        return this.depositPlayer(playerName, amount);
    }

    public EconomyResponse depositPlayer(final OfflinePlayer player, final String worldName, final double amount) {
        return this.depositPlayer(player.getName(), amount);
    }

    public EconomyResponse createBank(final String name, final String player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse createBank(final String name, final OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse deleteBank(final String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse bankBalance(final String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse bankHas(final String name, final double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse bankWithdraw(final String name, final double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse bankDeposit(final String name, final double amount) {
        return null;
    }

    public EconomyResponse isBankOwner(final String name, final String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse isBankOwner(final String name, final OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse isBankMember(final String name, final String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public EconomyResponse isBankMember(final String name, final OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    public List<String> getBanks() {
        return null;
    }

    public boolean createPlayerAccount(final String playerName) {
        this.economyModule.setBalance(Bukkit.getPlayer(playerName).getUniqueId(), 0.0);
        return false;
    }

    public boolean createPlayerAccount(final OfflinePlayer player) {
        this.createPlayerAccount(player.getName());
        return false;
    }

    public boolean createPlayerAccount(final String playerName, final String worldName) {
        this.createPlayerAccount(playerName);
        return false;
    }

    public boolean createPlayerAccount(final OfflinePlayer player, final String worldName) {
        this.createPlayerAccount(player.getName());
        return false;
    }
}
