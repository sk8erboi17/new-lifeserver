package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.homequery.HomeQuery;
import net.giuse.teleportmodule.database.homequery.SaveQueryHome;
import net.giuse.teleportmodule.serializer.HomeBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class HomeLoaderModule extends Services {
    @Getter
    private HashMap<UUID, HashMap<String, Location>> cacheHome;

    @Getter
    private Serializer<HomeSerialized> homeBuilderSerializer;
    @Inject
    private Injector injector;
    @Inject
    private Logger logger;
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
        logger.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Home...");
        injector.register(HomeLoaderModule.class, this);

        homeBuilderSerializer = injector.getSingleton(HomeBuilderSerializer.class);
        cacheHome = new HashMap<>();

        //Load Home
        injector.getSingleton(HomeQuery.class).query();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mainModule, this::saveCache,mainConfig.getInt("auto-save") * 20L,mainConfig.getInt("auto-save") * 20L);
    }



    /*
     * Unload Service
     */
    @Override
    public void unload() {
        logger.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Home...");
        saveCache();
    }
    private void saveCache(){
        logger.info("[LifeServer] Saving home cache...");
        injector.getSingleton(SaveQueryHome.class).query();
    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
    }

    /*
     * Get Home from player's UUID
     */
    public HashMap<String, Location> getHome(UUID owner) {
        return cacheHome.get(owner);
    }
}