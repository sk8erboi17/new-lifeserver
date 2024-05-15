package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitElement;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadKit implements Query {

    private final ExecuteQuery executeQuery;
    private final KitModule kitModule;

    @Inject
    public LoadKit(KitModule kitModule, ExecuteQuery executeQuery) {
        this.kitModule = kitModule;
        this.executeQuery = executeQuery;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_kit (KitName TEXT, KitItems TEXT, coolDown INT);", PreparedStatement::execute));
        executeQuery.execute(new QueryCallback("SELECT * FROM lifeserver_kit", preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String kitName = rs.getString(1);
                    int kitCooldown = rs.getInt(3);
                    String elementsKitBase64 = rs.getString(2);

                    KitElement kitElementDecoded = new KitElement(kitCooldown);
                    kitElementDecoded.setBase(elementsKitBase64);
                    kitElementDecoded.build();
                    kitModule.getKitElements().put(kitName, kitElementDecoded);
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error, transaction rolled back: " + e.getMessage());
            }

        }));
    }
}
