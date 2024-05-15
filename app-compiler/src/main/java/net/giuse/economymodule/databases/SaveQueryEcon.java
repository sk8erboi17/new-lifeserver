package net.giuse.economymodule.databases;


import net.giuse.economymodule.EconomyModule;
import net.giuse.economymodule.serializer.EconPlayerSerialized;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveQueryEcon implements Query {
    private final ExecuteQuery executeQuery;
    private final EconomyModule economyModule;

    @Inject
    public SaveQueryEcon(ExecuteQuery executeQuery, EconomyModule economyModule) {
        this.executeQuery = executeQuery;
        this.economyModule = economyModule;
    }

    @Override
    public void query() {
        if (economyModule.getEconPlayersCache().isEmpty()) return;
        List<QueryCallback> queryCallbacks = new ArrayList<>();
        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_economy (name TEXT, balance decimal);", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("DELETE FROM lifeserver_economy;", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("INSERT INTO lifeserver_economy VALUES(?,?);", preparedStatement ->
                economyModule.getEconPlayersCache().forEach((uuid, balance) -> {
                    try {
                        String[] args = economyModule.getEconPlayerSerializer().encode(new EconPlayerSerialized(uuid, balance)).split(",");
                        preparedStatement.setString(1, args[0]);
                        preparedStatement.setDouble(2, Double.parseDouble(args[1]));
                        preparedStatement.execute();
                    } catch (SQLException e) {
                        Bukkit.getLogger().info("[ECO] Database error, transaction rolled back: " + e.getMessage());
                    }
                })));
        executeQuery.executeBatch(queryCallbacks);

    }
}
