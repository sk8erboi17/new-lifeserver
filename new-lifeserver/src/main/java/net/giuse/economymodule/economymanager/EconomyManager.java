package net.giuse.economymodule.economymanager;

import net.giuse.economymodule.service.EconomyService;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class EconomyManager implements Economy {

    private final EconomyService economyService;

    @Inject
    public EconomyManager(EconomyService economyService) {
        this.economyService = economyService;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "LifeServer";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;  // Assuming 2 decimal places for currency
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return "moneys";
    }

    @Override
    public String currencyNameSingular() {
        return "money";
    }

    @Override
    public boolean hasAccount(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return player != null && economyService.playerExists(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return economyService.playerExists(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return economyService.getBalance(player.getUniqueId()).doubleValue();
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economyService.getBalance(player.getUniqueId()).doubleValue();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return economyService.getBalance(player.getUniqueId()).compareTo(BigDecimal.valueOf(amount)) >= 0;
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return economyService.getBalance(player.getUniqueId()).compareTo(BigDecimal.valueOf(amount)) >= 0;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            UUID playerUUID = player.getUniqueId();
            BigDecimal currentBalance = economyService.getBalance(playerUUID);
            if (currentBalance.compareTo(BigDecimal.valueOf(amount)) >= 0) {
                economyService.withdraw(playerUUID, BigDecimal.valueOf(amount));
                return new EconomyResponse(amount, economyService.getBalance(playerUUID).doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                return new EconomyResponse(0, currentBalance.doubleValue(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            UUID playerUUID = player.getUniqueId();
            economyService.deposit(playerUUID, BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, economyService.getBalance(playerUUID).doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LifeServer Eco does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return null; // Not supported
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && !economyService.playerExists(player.getUniqueId())) {
            economyService.setBalance(player.getUniqueId(), BigDecimal.ZERO);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return createPlayerAccount(player.getName());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player.getName());
    }
}
