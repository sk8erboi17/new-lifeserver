package net.giuse.teleportmodule.submodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;

public class SpawnLoaderModule extends Services {

    @Inject
    private ExecuteQuery executeQuery;
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
        createTable();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Spawn...");
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS lifeserver_spawn (" +
                "id INTEGER PRIMARY KEY, " +
                "location VARCHAR(255)" +
                ")";
        executeQuery.execute(new PreparedStatementQuery(query, PreparedStatement::execute));
    }
}
