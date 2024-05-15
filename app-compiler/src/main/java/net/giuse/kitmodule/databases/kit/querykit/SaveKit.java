package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveKit implements Query {
    private final ExecuteQuery executeQuery;
    private final KitModule kitModule;

    @Inject
    public SaveKit(KitModule kitModule, ExecuteQuery executeQuery) {
        this.kitModule = kitModule;
        this.executeQuery = executeQuery;
    }


    @Override
    public void query() {
        if (kitModule.getKitElements().isEmpty()) return;
        List<QueryCallback> queryCallbacks = new ArrayList<>();

        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_kit (KitName TEXT, KitItems TEXT, coolDown INT);", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("DELETE FROM lifeserver_kit;", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("INSERT INTO lifeserver_kit VALUES(?,?,?)", preparedStatement -> kitModule.getKitElements().forEach((name, kitBuilder) -> {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, kitBuilder.getElementsKitBase64());
                preparedStatement.setInt(3, kitBuilder.getCoolDown());
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error, transaction rolled back: " + e.getMessage());
            }
        })));

        executeQuery.executeBatch(queryCallbacks);
    }
}
