package net.giuse.kitmodule.databases.kit.queryplayerkit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitCooldownSerialized;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.QueryCallback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavePlayerKit implements Query {

    private final ExecuteQuery executeQuery;
    private final KitModule kitModule;

    @Inject
    public SavePlayerKit(KitModule kitModule, ExecuteQuery executeQuery) {
        this.executeQuery = executeQuery;
        this.kitModule = kitModule;
    }


    @Override
    public void query() {
        if (kitModule.getCachePlayerKit().isEmpty()) return;
        List<QueryCallback> queryCallbacks = new ArrayList<>();
        queryCallbacks.add(new QueryCallback("CREATE TABLE IF NOT EXISTS lifeserver_playerkit(PlayerUUID TEXT,KitCooldown TEXT);", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("DELETE FROM lifeserver_playerkit;", PreparedStatement::execute));
        queryCallbacks.add(new QueryCallback("INSERT INTO lifeserver_playerkit VALUES(?,?)", preparedStatement -> kitModule.getCachePlayerKit().forEach(((uuid, playerTimerSystem) -> {
            String[] playerCooldownEncoded = kitModule.getPlayerCooldownSerializer().encode(new PlayerKitCooldownSerialized(uuid, playerTimerSystem)).split(";");
            boolean notEnoughArgument = playerCooldownEncoded.length == 1;
            if (notEnoughArgument) return;
            try {
                preparedStatement.setString(1, playerCooldownEncoded[0]);
                preparedStatement.setString(2, playerCooldownEncoded[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("[PLAYER-KIT] Database error, transaction rolled back: " + e.getMessage());
            }
        }))));
        executeQuery.executeBatch(queryCallbacks);


    }
}
