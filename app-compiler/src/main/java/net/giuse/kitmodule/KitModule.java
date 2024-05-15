package net.giuse.kitmodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.kitmodule.builder.KitElement;
import net.giuse.kitmodule.cooldownsystem.PlayerKitCooldown;
import net.giuse.kitmodule.databases.kit.querykit.LoadKit;
import net.giuse.kitmodule.databases.kit.querykit.SaveKit;
import net.giuse.kitmodule.databases.kit.queryplayerkit.LoadPlayerKit;
import net.giuse.kitmodule.databases.kit.queryplayerkit.SavePlayerKit;
import net.giuse.kitmodule.files.ConfigKits;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.kitmodule.serializer.PlayerKitCooldownSerializer;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitCooldownSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module Kit
 */
public class KitModule extends Services {

    @Getter
    private final HashMap<UUID, PlayerKitCooldown> cachePlayerKit = new HashMap<>();
    @Getter
    private final HashMap<String, KitElement> kitElements = new HashMap<>();
    @Getter
    private final Serializer<PlayerKitCooldownSerialized> playerCooldownSerializer = new PlayerKitCooldownSerializer();
    @Getter
    private ConfigKits fileKits;
    @Inject
    private Injector injector;
    @Inject
    private Logger logger;
    @Inject
    private MainModule mainModule;
    @Inject
    private FileConfiguration mainConfig;

    /**
     * Load Module Kit
     */
    @SneakyThrows
    @Override
    public void load() {
        logger.info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");
        injector.register(KitModule.class, this);
        injector.getSingleton(KitGui.class);
        ReflectionsFiles.loadFiles(fileKits = new ConfigKits());
        injector.getSingleton(MessageLoaderKit.class).load();
        loadCache();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mainModule, this::saveCache,mainConfig.getInt("auto-save") * 20L,mainConfig.getInt("auto-save") * 20L);
    }



    /**
     * Unload Module Kit
     */
    @Override
    public void unload() {
        logger.info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Kits...");
        saveCache();
    }

    /**
     * Priority Module
     */
    @Override
    public int priority() {
        return -1;
    }


    /**
     * Search PlayerTimerSystem from Set
     */
    public PlayerKitCooldown getPlayerCooldown(UUID playerUUID) {
        return cachePlayerKit.get(playerUUID);
    }

    /**
     * Search Kit  from Name in a Set
     */
    public KitElement getKit(@NotNull String searchKitBuilder) {
        searchKitBuilder = searchKitBuilder.toLowerCase();
        if (kitElements.containsKey(searchKitBuilder)) {
            return kitElements.get(searchKitBuilder.toLowerCase());
        }
        return null;
    }


    private void saveCache() {
        Bukkit.getLogger().info("[LifeServer] Saving kits...");
        injector.getSingleton(SaveKit.class).query();
        injector.getSingleton(SavePlayerKit.class).query();
    }

    private void loadCache() {
        injector.getSingleton(LoadKit.class).query();
        injector.getSingleton(LoadPlayerKit.class).query();
    }

}

