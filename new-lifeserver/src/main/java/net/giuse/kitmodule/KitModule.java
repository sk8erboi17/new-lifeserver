package net.giuse.kitmodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.kitmodule.databases.KitRepository;
import net.giuse.kitmodule.databases.PlayerKitRepository;
import net.giuse.kitmodule.files.KitFileManager;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.kitmodule.service.PlayerKitService;
import net.giuse.mainmodule.modules.AbstractService;
import org.bukkit.Bukkit;

import javax.inject.Inject;

/**
 * Module Kit
 */
public class KitModule extends AbstractService {

    @Getter
    private KitFileManager fileKits;

    @Inject
    private Injector injector;

    private MessageLoaderKit messageLoaderKit;
    private KitGui kitGui;

    /**
     * Load Module Kit
     */
    @SneakyThrows
    @Override
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");
        ReflectionsFiles.loadFiles(fileKits = new KitFileManager());
        PlayerKitService playerKitService = injector.getSingleton(PlayerKitService.class);
        injector.getSingleton(KitRepository.class).createTable();
        injector.getSingleton(PlayerKitRepository.class).createTable();

        kitGui = injector.getSingleton(KitGui.class);
        messageLoaderKit = injector.getSingleton(MessageLoaderKit.class);
        messageLoaderKit.load();

        playerKitService.startTimer();

    }


    /**
     * Unload Module Kit
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Kits...");
    }

    @Override
    public void reloadConfig() {
        fileKits.setFile(fileKits.getKitFile());
        fileKits.setYamlConfiguration(fileKits.getKitYaml());
        fileKits.reload();
        kitGui.initInv();

        fileKits.setFile(fileKits.getMessagesFile());
        fileKits.setYamlConfiguration(fileKits.getMessagesYaml());
        fileKits.reload();
        messageLoaderKit.load();
    }

}

