package net.giuse.economymodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.economymodule.economymanager.EconomyManager;
import net.giuse.economymodule.files.FileManager;
import net.giuse.economymodule.messageloader.MessageLoaderEconomy;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.services.Services;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import javax.inject.Inject;
import java.sql.PreparedStatement;


public class EconomyModule extends Services {
    @Inject
    private Injector injector;

    @Inject
    private MainModule mainModule;

    @Inject
    private ExecuteQuery executeQuery;

    @Getter
    private EconomyManager customEcoManager;

    @Getter
    private FileManager configManager;


    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Loading economy...");
        customEcoManager = injector.getSingleton(EconomyManager.class);
        mainModule.getServer().getServicesManager().register(Economy.class, customEcoManager, mainModule, ServicePriority.Normal);
        createTable();
        ReflectionsFiles.loadFiles(configManager = new FileManager());
        injector.getSingleton(MessageLoaderEconomy.class).load();

    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS lifeserver_economy (" +
                "uuid TEXT PRIMARY KEY, " +
                "balance DECIMAL)";
        executeQuery.execute(new PreparedStatementQuery(query, PreparedStatement::execute));
    }

    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Unloading economy...");
    }


}