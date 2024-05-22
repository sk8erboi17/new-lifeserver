package net.giuse.teleportmodule.submodule.warp;

import ch.jalu.injector.Injector;
import lombok.SneakyThrows;
import net.giuse.mainmodule.modules.AbstractService;
import net.giuse.mainmodule.modules.AbstractSubService;
import net.giuse.teleportmodule.submodule.warp.repository.WarpRepository;
import org.bukkit.Bukkit;

import javax.inject.Inject;


public class WarpLoaderModule extends AbstractSubService {

    @Inject
    private Injector injector;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Warps...");
        injector.getSingleton(WarpRepository.class).createTable();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Warps...");
    }

}
