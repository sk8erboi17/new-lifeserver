package net.giuse.economymodule.databases;


import net.giuse.economymodule.EconPlayerSerialized;
import net.giuse.economymodule.EconomyModule;
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

        queryCallbacks.add(new QueryCallback("DROP TABLE Economy;", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS Economy (name TEXT, balance DOUBLE);", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("INSERT INTO Economy VALUES(?,?);", preparedStatement -> economyModule.getEconPlayersCache().forEach((uuid, balance) -> {
            try {
                String[] args = economyModule.getEconPlayerSerializer().encode(new EconPlayerSerialized(uuid, balance)).split(",");
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
