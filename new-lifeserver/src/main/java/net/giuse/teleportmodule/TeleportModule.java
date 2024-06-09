package net.giuse.teleportmodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.modules.AbstractModule;
import net.giuse.teleportmodule.events.EntityBackOnDeath;
import net.giuse.teleportmodule.files.TeleportFileManager;
import net.giuse.teleportmodule.messageloader.MessageLoaderTeleport;
import net.giuse.teleportmodule.submodule.home.HomeLoaderModule;
import net.giuse.teleportmodule.submodule.spawn.SpawnLoaderModule;
import net.giuse.teleportmodule.submodule.teleportrequest.TeleportRequestModule;
import net.giuse.teleportmodule.submodule.teleportrequest.dto.PendingRequest;
import net.giuse.teleportmodule.submodule.warp.WarpLoaderModule;
import net.giuse.teleportmodule.submodule.warp.gui.WarpGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TeleportModule extends AbstractModule {

    @Getter
    private final Set<PendingRequest> pendingRequests = new HashSet<>();

    @Getter
    private final HashMap<Player, Location> backLocations = new HashMap<>();

    @Getter
    private TeleportFileManager teleportFileManager;

    @Inject
    private Injector injector;

    @Inject
    private FileConfiguration mainConfig;

    @Inject
    private MainModule mainModule;

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
        ReflectionsFiles.loadFiles(teleportFileManager = new TeleportFileManager());

        //Load Message
        messageLoaderTeleport = injector.getSingleton(MessageLoaderTeleport.class);
        messageLoaderTeleport.load();
        warpGui = injector.getSingleton(WarpGui.class);

        //Check if load back-on-death is active
        if (mainConfig.getBoolean("allow-back-on-death")) {
            Bukkit.getServer().getPluginManager().registerEvents(injector.getSingleton(EntityBackOnDeath.class), mainModule);
        }
        injector.getSingleton(WarpLoaderModule.class).load();
        injector.getSingleton(HomeLoaderModule.class).load();
        injector.getSingleton(SpawnLoaderModule.class).load();
        injector.getSingleton(TeleportRequestModule.class).load();


    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§f Unloaded teleports");
        injector.getSingleton(WarpLoaderModule.class).unload();
        injector.getSingleton(HomeLoaderModule.class).unload();
        injector.getSingleton(SpawnLoaderModule.class).unload();
        injector.getSingleton(TeleportRequestModule.class).unload();
    }

    @Override
    public void reload() {

        teleportFileManager.setFile(teleportFileManager.getMessagesHomeFile());
        teleportFileManager.setYamlConfiguration(teleportFileManager.getMessagesHomeYaml());
        teleportFileManager.reload();

        teleportFileManager.setFile(teleportFileManager.getMessagesWarpFile());
        teleportFileManager.setYamlConfiguration(teleportFileManager.getMessagesWarpYaml());
        teleportFileManager.reload();

        teleportFileManager.setFile(teleportFileManager.getWarpFile());
        teleportFileManager.setYamlConfiguration(teleportFileManager.getWarpYaml());
        teleportFileManager.reload();
        warpGui.initInv();

        teleportFileManager.setFile(teleportFileManager.getMessagesSpawnFile());
        teleportFileManager.setYamlConfiguration(teleportFileManager.getMessagesSpawnYaml());
        teleportFileManager.reload();

        teleportFileManager.setFile(teleportFileManager.getMessagesTeleportFile());
        teleportFileManager.setYamlConfiguration(teleportFileManager.getMessagesTeleportYaml());
        teleportFileManager.reload();
        messageLoaderTeleport.load();
    }

}
