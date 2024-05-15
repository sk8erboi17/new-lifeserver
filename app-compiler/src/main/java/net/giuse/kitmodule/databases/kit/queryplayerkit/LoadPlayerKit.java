package net.giuse.kitmodule.databases.kit.queryplayerkit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitCooldownSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadPlayerKit implements Query {

    private final ExecuteQuery executeQuery;
    private final MainModule mainModule;
    private final KitModule kitModule;

    @Inject
    public LoadPlayerKit(MainModule mainModule, KitModule kitModule, ExecuteQuery executeQuery) {
        this.mainModule = mainModule;
        this.kitModule = kitModule;
        this.executeQuery = executeQuery;
    }

    @Override
    public void query() {
        executeQuery.execute(new QueryCallback("SELECT * FROM PlayerKit", preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String playerCooldownSerialized = rs.getString(1) + ";" + rs.getString(2);
                    PlayerKitCooldownSerialized PlayerCooldownDecoded = kitModule.getPlayerCooldownSerializer().decoder(playerCooldownSerialized);
                    PlayerCooldownDecoded.getPlayerKitCooldown().runTaskTimerAsynchronously(mainModule, 20L, 20L);
                    kitModule.getCachePlayerKit().put(PlayerCooldownDecoded.getUuidPlayer(), PlayerCooldownDecoded.getPlayerKitCooldown());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }));
    }
}
