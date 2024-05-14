package net.giuse.teleportmodule.database.warpquery;


import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import net.giuse.teleportmodule.submodule.WarpLoaderModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveQueryWarp implements Query {
    private final ExecuteQuery executeQuery;
    private final WarpLoaderModule warpModule;

    @Inject
    public SaveQueryWarp(WarpLoaderModule warpModule, ExecuteQuery executeQuery) {
        this.executeQuery = executeQuery;
        this.warpModule = warpModule;
    }


    @Override
    public void query() {
        if (warpModule.getWarps().isEmpty()) return;
        List<QueryCallback> queryCallbacks = new ArrayList<>();

        queryCallbacks.add(new QueryCallback("DROP TABLE Warp;", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS Warp (Name TEXT,Location TEXT);", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("INSERT INTO Warp VALUES(?,?);", preparedStatement -> warpModule.getWarps().forEach((uuid, hashMap) -> {
            try {
                String[] args = warpModule.getWarpBuilderSerializer().encode(new WarpSerialized(uuid, hashMap)).split(":");
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
