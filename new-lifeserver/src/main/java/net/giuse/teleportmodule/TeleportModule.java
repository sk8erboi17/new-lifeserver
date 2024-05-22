package net.giuse.teleportmodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.modules.AbstractService;
import net.giuse.teleportmodule.events.EntityBackOnDeath;
import net.giuse.teleportmodule.files.FileManager;
import net.giuse.teleportmodule.messageloader.MessageLoaderTeleport;
import net.giuse.teleportmodule.submodule.teleportrequest.dto.PendingRequest;
import net.giuse.teleportmodule.submodule.warp.gui.WarpGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TeleportModule extends AbstractService {
    @Getter
    private final Set<PendingRequest> pendingRequests = new HashSet<>();
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
    @Inject
    private WarpGui warpGui;

    private MessageLoaderTeleport messageLoaderTeleport;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§f Loading teleports...");
        //Load Files
        ReflectionsFiles.loadFiles(fileManager = new FileManager());

        //Load Message
        messageLoaderTeleport = injector.getSingleton(MessageLoaderTeleport.class);
        messageLoaderTeleport.load();
        warpGui = injector.getSingleton(WarpGui.class);
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
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§f Unloaded teleports");
    }

    @Override
    public void reloadConfig() {

        fileManager.setFile(fileManager.getMessagesHomeFile());
        fileManager.setYamlConfiguration(fileManager.getMessagesHomeYaml());
        fileManager.reload();

        fileManager.setFile(fileManager.getMessagesWarpFile());
        fileManager.setYamlConfiguration(fileManager.getMessagesWarpYaml());
        fileManager.reload();

        fileManager.setFile(fileManager.getWarpFile());
        fileManager.setYamlConfiguration(fileManager.getWarpYaml());
        fileManager.reload();
        warpGui.initInv();

        fileManager.setFile(fileManager.getMessagesSpawnFile());
        fileManager.setYamlConfiguration(fileManager.getMessagesSpawnYaml());
        fileManager.reload();

        fileManager.setFile(fileManager.getMessagesTeleportFile());
        fileManager.setYamlConfiguration(fileManager.getMessagesTeleportYaml());
        fileManager.reload();
        messageLoaderTeleport.load();
    }

}
