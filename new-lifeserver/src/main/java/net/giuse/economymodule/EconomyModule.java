package net.giuse.economymodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.economymodule.economymanager.EconomyManager;
import net.giuse.economymodule.files.EconomyFileManager;
import net.giuse.economymodule.messageloader.MessageLoaderEconomy;
import net.giuse.economymodule.repository.EconomyRepository;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.modules.AbstractModule;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import javax.inject.Inject;


public class EconomyModule extends AbstractModule {
    public static String MONEY_SYMBOL;
    @Inject
    private Injector injector;

    @Inject
    private MainModule mainModule;

    @Getter
    private EconomyManager customEcoManager;

    @Getter
    private EconomyFileManager economyFileManager;

    private MessageLoaderEconomy messageLoaderEconomy;

    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Loading economy...");
        customEcoManager = injector.getSingleton(EconomyManager.class);
        mainModule.getServer().getServicesManager().register(Economy.class, customEcoManager, mainModule, ServicePriority.Normal);
        injector.getSingleton(EconomyRepository.class).createTable();
        ReflectionsFiles.loadFiles(economyFileManager = new EconomyFileManager());
        this.messageLoaderEconomy = injector.getSingleton(MessageLoaderEconomy.class);
        messageLoaderEconomy.load();
        MONEY_SYMBOL = mainModule.getConfig().getString("money-symbol");
    }


    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Unloading economy...");
    }

    @Override
    public void reload() {
        economyFileManager.setFile(economyFileManager.getMessagesFile());
        economyFileManager.setYamlConfiguration(economyFileManager.getMessagesYaml());
        economyFileManager.reload();
        messageLoaderEconomy.load();
    }


}