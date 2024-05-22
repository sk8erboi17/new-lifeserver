package net.giuse.economymodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.economymodule.economymanager.EconomyManager;
import net.giuse.economymodule.files.FileManager;
import net.giuse.economymodule.messageloader.MessageLoaderEconomy;
import net.giuse.economymodule.repository.EconomyRepository;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.modules.AbstractService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import javax.inject.Inject;


public class EconomyModule extends AbstractService {
    public static String MONEY_SYMBOL;
    @Inject
    private Injector injector;

    @Inject
    private MainModule mainModule;

    @Getter
    private EconomyManager customEcoManager;

    @Getter
    private FileManager configManager;

    private MessageLoaderEconomy messageLoaderEconomy;

    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Loading economy...");
        customEcoManager = injector.getSingleton(EconomyManager.class);
        mainModule.getServer().getServicesManager().register(Economy.class, customEcoManager, mainModule, ServicePriority.Normal);
        injector.getSingleton(EconomyRepository.class).createTable();
        ReflectionsFiles.loadFiles(configManager = new FileManager());
        this.messageLoaderEconomy = injector.getSingleton(MessageLoaderEconomy.class);
        MONEY_SYMBOL = mainModule.getConfig().getString("money-symbol");
    }



    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Unloading economy...");
    }

    @Override
    public void reloadConfig() {
        configManager.setFile(configManager.getMessagesFile());
        configManager.setYamlConfiguration(configManager.getMessagesYaml());
        configManager.reload();
        messageLoaderEconomy.load();
    }


}