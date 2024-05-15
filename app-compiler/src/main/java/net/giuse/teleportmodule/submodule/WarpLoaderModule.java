package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import com.avaje.ebean.config.GlobalProperties;
import jdk.jfr.internal.tool.Main;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.spawnquery.SaveQuerySpawn;
import net.giuse.teleportmodule.database.warpquery.SaveQueryWarp;
import net.giuse.teleportmodule.database.warpquery.WarpQuery;
import net.giuse.teleportmodule.gui.WarpGui;
import net.giuse.teleportmodule.serializer.WarpBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.logging.Logger;


public class WarpLoaderModule extends Services {

    @Getter
    private final Serializer<WarpSerialized> warpBuilderSerializer = new WarpBuilderSerializer();
    @Getter
    private HashMap<String, Location> warps;
    @Inject
    private Injector injector;
    @Inject
    private Logger log;
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
        log.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Warps...");
        injector.register(WarpLoaderModule.class, this);
        injector.getSingleton(WarpGui.class);

        //Load Cache
        warps = new HashMap<>();

        //Load Warps
        injector.getSingleton(WarpQuery.class).query();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mainModule, this::saveCache,mainConfig.getInt("auto-save") * 20L,mainConfig.getInt("auto-save") * 20L);

    }

    private void saveCache(){
        log.info("[LifeServer] Saving warp cache...");
        injector.getSingleton(SaveQueryWarp.class).query();
    }
    /*
     * Unload Service
     */
    @Override
    public void unload() {
        log.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Warps...");
        injector.getSingleton(SaveQueryWarp.class).query();
    }


    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 2;
    }

    /*
     * Get Warp From Name
     */
    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }
}
