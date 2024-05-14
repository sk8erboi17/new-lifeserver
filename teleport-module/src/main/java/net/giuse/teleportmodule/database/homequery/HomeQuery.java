package net.giuse.teleportmodule.database.homequery;

import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import net.giuse.teleportmodule.submodule.HomeLoaderModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final HomeLoaderModule homeModule;

    @Inject
    public HomeQuery(HomeLoaderModule homeModule, ExecuteQuery executeQuery) {
        this.executeQuery = executeQuery;
        this.homeModule = homeModule;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("SELECT * FROM Home", preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    HomeSerialized homeSerialized = homeModule.getHomeBuilderSerializer().decoder(rs.getString(1) + ":" + rs.getString(2));
                    homeModule.getCacheHome().put(homeSerialized.getOwner(), homeSerialized.getLocations());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }));
    }
}
