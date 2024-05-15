package net.giuse.teleportmodule.database.spawnquery;

import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import net.giuse.teleportmodule.submodule.SpawnLoaderModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpawnQuery implements Query {

    private final ExecuteQuery executeQuery;
    private final SpawnLoaderModule spawnModule;

    @Inject
    public SpawnQuery(ExecuteQuery executeQuery, SpawnLoaderModule spawnModule) {
        this.executeQuery = executeQuery;
        this.spawnModule = spawnModule;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_spawn (Location TEXT);", PreparedStatement::execute));

        executeQuery.execute(new QueryCallback("SELECT * FROM lifeserver_spawn;", preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String location = rs.getString("Location");
                    spawnModule.setSpawnBuilder(spawnModule.getSpawnBuilderSerializer().decoder(location));
                } else {
                    Bukkit.getLogger().info("[SPAWN] No spawn data found.");
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[SPAWN] Database error: " + e.getMessage());
            }
        }));
    }
}
