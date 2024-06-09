package net.giuse.teleportmodule.submodule.spawn;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.modules.AbstractSubModule;
import net.giuse.teleportmodule.submodule.spawn.builder.SpawnBuilder;
import net.giuse.teleportmodule.submodule.spawn.repository.SpawnRepository;
import org.bukkit.Bukkit;

import javax.inject.Inject;

public class SpawnLoaderModule extends AbstractSubModule {

    @Inject
    private Injector injector;

    @Getter
    @Setter
    private SpawnBuilder spawnBuilder;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Spawn...");
        injector.getSingleton(SpawnRepository.class).createTable();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Spawn...");
    }

}
