package net.giuse.teleportmodule.database.homequery;


import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import net.giuse.teleportmodule.submodule.HomeLoaderModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveQueryHome implements Query {

    private final ExecuteQuery executeQuery;

    private final HomeLoaderModule homeModule;

    @Inject
    public SaveQueryHome(HomeLoaderModule homeModule, ExecuteQuery executeQuery) {
        this.executeQuery = executeQuery;
        this.homeModule = homeModule;
    }


    @Override
    public void query() {
        if (homeModule.getCacheHome().isEmpty()) return;
        List<QueryCallback> queryCallbacks = new ArrayList<>();
        queryCallbacks.add(new QueryCallback("DROP TABLE Home;", PreparedStatement::executeQuery));
        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS Home (UUID TEXT,Location TEXT);", PreparedStatement::executeQuery));
        queryCallbacks.add(new QueryCallback("INSERT INTO Home VALUES(?,?)", preparedStatement -> homeModule.getCacheHome().forEach((uuid, hashMap) -> {
            String[] args = homeModule.getHomeBuilderSerializer().encode(new HomeSerialized(uuid, hashMap)).split(":");
            if (args.length == 1) return;
            try {
                preparedStatement.setString(1, args[0]);
                preparedStatement.setString(2, args[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }
        })));
        executeQuery.executeBatch(queryCallbacks);

    }
}
