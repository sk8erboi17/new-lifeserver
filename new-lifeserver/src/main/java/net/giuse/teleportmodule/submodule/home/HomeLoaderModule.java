package net.giuse.teleportmodule.submodule.home;

import ch.jalu.injector.Injector;
import lombok.SneakyThrows;
import net.giuse.mainmodule.modules.AbstractSubModule;
import net.giuse.teleportmodule.submodule.home.repository.HomeRepository;
import org.bukkit.Bukkit;

import javax.inject.Inject;

public class HomeLoaderModule extends AbstractSubModule {

    @Inject
    private Injector injector;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Home...");
        injector.getSingleton(HomeRepository.class).createTable();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Home...");
    }

}
