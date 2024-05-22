package net.giuse.kitmodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.kitmodule.files.ConfigKits;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.kitmodule.service.KitService;
import net.giuse.kitmodule.service.PlayerKitService;
import net.giuse.mainmodule.services.Services;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;

/**
 * Module Kit
 */
public class KitModule extends Services {

    @Getter
    private ConfigKits fileKits;
    @Inject
    private Injector injector;
    @Inject
    private ExecuteQuery executeQuery;

    /**
     * Load Module Kit
     */
    @SneakyThrows
    @Override
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");
        ReflectionsFiles.loadFiles(fileKits = new ConfigKits());
        PlayerKitService playerKitService = injector.getSingleton(PlayerKitService.class);
        injector.getSingleton(MessageLoaderKit.class).load();
        createTable();

        playerKitService.startTimer();

    }


    /**
     * Unload Module Kit
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Kits...");
    }


    private void createTable() {

        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_kit (" +
                "kit_name VARCHAR(255) NOT NULL PRIMARY KEY, " +
                "kit_items VARCHAR(16384) NOT NULL, " +
                "cooldown INT NOT NULL" +
                ");", PreparedStatement::execute));

        executeQuery.execute(new PreparedStatementQuery(
                "CREATE TABLE IF NOT EXISTS lifeserver_playerkit (" +
                        "player_uuid VARCHAR(255) NOT NULL, " +
                        "kit_name VARCHAR(255) NOT NULL, " +
                        "kit_cooldown INT NOT NULL, " +
                        "PRIMARY KEY (player_uuid, kit_name), " +
                        "FOREIGN KEY (kit_name) REFERENCES lifeserver_kit(kit_name)" +
                        ");",
                PreparedStatement::execute
        ));


    }


}

