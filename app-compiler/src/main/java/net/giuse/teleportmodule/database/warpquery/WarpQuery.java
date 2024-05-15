package net.giuse.teleportmodule.database.warpquery;

import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import net.giuse.teleportmodule.submodule.WarpLoaderModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarpQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final WarpLoaderModule warpModule;

    @Inject
    public WarpQuery(ExecuteQuery executeQuery, WarpLoaderModule warpModule) {
        this.executeQuery = executeQuery;
        this.warpModule = warpModule;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_warp (Name TEXT,Location TEXT);", PreparedStatement::execute));

        executeQuery.execute(new QueryCallback("SELECT * FROM lifeserver_warp", preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    WarpSerialized warpSerialized = warpModule.getWarpBuilderSerializer().decoder(rs.getString(1) + ":" + rs.getString(2));
                    warpModule.getWarps().put(warpSerialized.getName(), warpSerialized.getLocation());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[WARP] Database error, transaction rolled back: " + e.getMessage());
            }
        }));
    }
}
