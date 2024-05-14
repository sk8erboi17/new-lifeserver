package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitElement;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadQueryKit implements Query {

    private final ExecuteQuery executeQuery;
    private final KitModule kitModule;

    @Inject
    public LoadQueryKit(KitModule kitModule, ExecuteQuery executeQuery) {
        this.kitModule = kitModule;
        this.executeQuery = executeQuery;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("SELECT * FROM Kit", preparedStatement -> {
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
                Bukkit.getLogger().info("Empty Database");
            }

        }));
    }
}
