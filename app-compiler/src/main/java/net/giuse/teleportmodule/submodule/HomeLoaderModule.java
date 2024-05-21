package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import lombok.SneakyThrows;
import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.mainmodule.services.Services;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;

public class HomeLoaderModule extends Services {

    @Inject
    private ExecuteQuery executeQuery;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Home...");
        createTable();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Home...");
    }

    private void createTable() {
        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_home (" +
                "uuid TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "location TEXT NOT NULL, " +
                "PRIMARY KEY (UUID, Name));", PreparedStatement::execute));
    }


}
