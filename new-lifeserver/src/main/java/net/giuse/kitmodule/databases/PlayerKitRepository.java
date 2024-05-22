package net.giuse.kitmodule.databases;

import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.kitmodule.dto.PlayerKit;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerKitRepository {

    @Inject
    private ExecuteQuery executeQuery;


    public void addPlayerKit(String playerUuid, String kitName) {
        if (playerKitExists(playerUuid, kitName)) {
            updatePlayerKit(playerUuid, kitName);
        } else {
            insertPlayerKit(playerUuid, kitName);
        }
    }

    public Integer getCooldown(String playerUuid, String kitName) {
        String query = "SELECT kit_cooldown FROM lifeserver_playerkit WHERE player_uuid = ? AND kit_name = ?";
        final Integer[] cooldown = {null};

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, playerUuid);
                preparedStatement.setString(2, kitName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        cooldown[0] = resultSet.getInt("kit_cooldown");
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe(String.format(
                        "Database error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
                throw e;
            }
        }));

        return cooldown[0];
    }

    public void updateCooldown(String playerUuid, String kitName, int newCooldown) {
        String query = "UPDATE lifeserver_playerkit SET kit_cooldown = ? WHERE player_uuid = ? AND kit_name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setInt(1, newCooldown);
                preparedStatement.setString(2, playerUuid);
                preparedStatement.setString(3, kitName);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe(String.format(
                        "Database error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
                throw e;
            }
        }));
    }

    public boolean playerKitExists(String playerUuid, String kitName) {
        String query = "SELECT COUNT(*) FROM lifeserver_playerkit WHERE player_uuid = ? AND kit_name = ?";
        Boolean[] exists = {false};  // Array con un solo elemento per catturare il risultato dalla lambda
        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            preparedStatement.setString(1, playerUuid);
            preparedStatement.setString(2, kitName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    exists[0] = resultSet.getInt(1) > 0;
                }
            }
        }));
        return exists[0];
    }

    private void insertPlayerKit(String playerUuid, String kitName) {
        String query = "INSERT INTO lifeserver_playerkit (player_uuid, kit_name, kit_cooldown) " +
                "SELECT ?, kit_name, cooldown FROM lifeserver_kit WHERE kit_name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            preparedStatement.setString(1, playerUuid);
            preparedStatement.setString(2, kitName);
            preparedStatement.execute();
        }));
    }


    private void updatePlayerKit(String playerUuid, String kitName) {
        String query = "UPDATE lifeserver_playerkit SET kit_cooldown = " +
                "(SELECT cooldown FROM lifeserver_kit WHERE kit_name = ?) " +
                "WHERE player_uuid = ? AND kit_name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            preparedStatement.setString(1, kitName);
            preparedStatement.setString(2, playerUuid);
            preparedStatement.setString(3, kitName);
            preparedStatement.execute();
        }));
    }

    public List<PlayerKit> getAllPlayerKits() {
        String query = "SELECT player_uuid, kit_name, kit_cooldown FROM lifeserver_playerkit";
        List<PlayerKit> playerKits = new ArrayList<>();

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String playerUuid = resultSet.getString("player_uuid");
                    String kitName = resultSet.getString("kit_name");
                    int kitCooldown = resultSet.getInt("kit_cooldown");
                    playerKits.add(new PlayerKit(playerUuid, kitName, kitCooldown));
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe(String.format(
                        "Database error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
                throw e;
            }
        }));

        return playerKits;
    }

    public void removePlayerKit(String kitName) {
        String query = "DELETE FROM lifeserver_playerkit WHERE kit_name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, kitName);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe(String.format(
                        "Database error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
                throw e;
            }
        }));
    }
}
