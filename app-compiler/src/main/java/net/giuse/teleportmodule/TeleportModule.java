package net.giuse.teleportmodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.events.EntityBackOnDeath;
import net.giuse.teleportmodule.files.FileManager;
import net.giuse.teleportmodule.messageloader.MessageLoaderTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;

public class TeleportModule extends Services {

    @Getter
    private final HashMap<Player, Location> backLocations = new HashMap<>();
    @Getter
    private FileManager fileManager;
    @Inject
    private Injector injector;
    @Inject
    private FileConfiguration mainConfig;
    @Inject
    private MainModule mainModule;


    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9 Loading");
        //Load Files
        ReflectionsFiles.loadFiles(fileManager = new FileManager());

        //Load Message
        injector.getSingleton(MessageLoaderTeleport.class).load();
        //Check if load back-on-death is active
        if (mainConfig.getBoolean("allow-back-on-death")) {
            Bukkit.getServer().getPluginManager().registerEvents(injector.getSingleton(EntityBackOnDeath.class), mainModule);
        }
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9 Unloaded");
    }

}
