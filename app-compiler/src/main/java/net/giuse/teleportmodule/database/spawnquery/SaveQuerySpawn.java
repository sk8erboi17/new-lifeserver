package net.giuse.teleportmodule.database.spawnquery;


import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import net.giuse.teleportmodule.submodule.SpawnLoaderModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveQuerySpawn implements Query {
    private final ExecuteQuery executeQuery;

    private final SpawnLoaderModule spawnModule;

    @Inject
    public SaveQuerySpawn(ExecuteQuery executeQuery, SpawnLoaderModule spawnModule) {
        this.executeQuery = executeQuery;
        this.spawnModule = spawnModule;
    }


    @Override
    public void query() {
        if (spawnModule.getSpawnBuilderSerializer() == null) return;

        List<QueryCallback> queryCallbacks = new ArrayList<>();
        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_spawn (Location TEXT);", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("DELETE FROM lifeserver_spawn;", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("INSERT INTO lifeserver_spawn VALUES(?)", (preparedStatement -> {
            try {
                if (spawnModule.getSpawnBuilder() != null) {
                    preparedStatement.setString(1, spawnModule.getSpawnBuilderSerializer().encode(spawnModule.getSpawnBuilder()));
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[SPAWN] Database error, transaction rolled back: " + e.getMessage());
            }
        })));
        executeQuery.executeBatch(queryCallbacks);
    }
}
