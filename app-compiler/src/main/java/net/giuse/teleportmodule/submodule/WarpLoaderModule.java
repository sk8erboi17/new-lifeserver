package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.warpquery.SaveQueryWarp;
import net.giuse.teleportmodule.database.warpquery.WarpQuery;
import net.giuse.teleportmodule.gui.WarpGui;
import net.giuse.teleportmodule.serializer.WarpBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import org.bukkit.Location;

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
