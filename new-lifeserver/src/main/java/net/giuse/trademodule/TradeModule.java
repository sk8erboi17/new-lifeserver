package net.giuse.trademodule;


import lombok.SneakyThrows;
import org.bukkit.Bukkit;


//TODO WORKING IN PROGRESS
public class TradeModule /*extends AbstractModule*/ {

    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Trade...");

    }

    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Trades...");
    }

    public void reload() {

    }

}

