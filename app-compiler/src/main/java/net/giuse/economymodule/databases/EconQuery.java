package net.giuse.economymodule.databases;

import net.giuse.economymodule.serializer.EconPlayerSerialized;
import net.giuse.economymodule.EconomyModule;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final EconomyModule economyModule;

    @Inject
    public EconQuery(ExecuteQuery executeQuery, EconomyModule economyModule) {
        this.executeQuery = executeQuery;
        this.economyModule = economyModule;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_economy (name TEXT, balance decimal);", PreparedStatement::execute));
        executeQuery.execute(new QueryCallback("SELECT * FROM lifeserver_economy", preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    EconPlayerSerialized econPlayerSerialized = economyModule.getEconPlayerSerializer().decoder(rs.getString(1) + "," + rs.getString(2));
                    economyModule.getEconPlayersCache().put(econPlayerSerialized.getUuid(), econPlayerSerialized.getBalance());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[ECO] Database error, transaction rolled back: " + e.getMessage());
            }

        }));
    }
}
