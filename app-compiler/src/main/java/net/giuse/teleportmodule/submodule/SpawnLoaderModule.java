package net.giuse.teleportmodule.submodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.database.spawnquery.SaveQuerySpawn;
import net.giuse.teleportmodule.database.spawnquery.SpawnQuery;
import net.giuse.teleportmodule.serializer.SpawnBuilderSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import java.util.logging.Logger;

public class SpawnLoaderModule extends Services {

    @Getter
    private final Serializer<SpawnBuilder> spawnBuilderSerializer = new SpawnBuilderSerializer();
    @Inject
    private Injector injector;
    @Inject
    private Logger logger;
    @Getter
    @Setter
    private SpawnBuilder spawnBuilder;
    @Inject
    private MainModule mainModule;
    @Inject
    private FileConfiguration mainConfig;
    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        logger.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Spawn...");
        injector.register(SpawnLoaderModule.class, this);

        //Load from database
        injector.getSingleton(SpawnQuery.class).query();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mainModule, this::saveCache,mainConfig.getInt("auto-save") * 20L,mainConfig.getInt("auto-save") * 20L);

    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        logger.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Spawn...");
        saveCache();
    }

    private void saveCache(){
        logger.info("[LifeServer] Saving spawn cache...");
        injector.getSingleton(SaveQuerySpawn.class).query();
    }
    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
    }

    /*
     * Save in database
     */
}
