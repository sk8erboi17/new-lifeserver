package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import lombok.SneakyThrows;
import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.gui.WarpGui;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;


public class WarpLoaderModule extends Services {

    @Inject
    private Injector injector;
    @Inject
    private ExecuteQuery executeQuery;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Warps...");
        injector.getSingleton(WarpGui.class);
        createTable();

    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Warps...");
    }


    private void createTable() {
        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_warp (" +
                "name TEXT PRIMARY KEY,location TEXT);", PreparedStatement::execute));
    }


}
