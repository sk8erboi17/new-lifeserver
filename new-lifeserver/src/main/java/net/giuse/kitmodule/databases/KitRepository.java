package net.giuse.kitmodule.databases;

import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.kitmodule.dto.Kit;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KitRepository {
    @Inject
    private ExecuteQuery executeQuery;

    public void addKit(String kitName, Kit kit, int cooldown) {
        executeQuery.execute(new PreparedStatementQuery("INSERT INTO lifeserver_kit VALUES(?,?,?)", preparedStatement -> {
            try {
                preparedStatement.setString(1, kitName);
                preparedStatement.setString(2, kit.getElementsKitBase64());
                preparedStatement.setInt(3, cooldown);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error, transaction rolled back: " + e.getMessage());
            }
        }));
    }

    public void removeKit(String kitName) {
        executeQuery.execute(new PreparedStatementQuery("DELETE FROM lifeserver_kit WHERE kit_name = ?", preparedStatement -> {
            try {
                preparedStatement.setString(1, kitName);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error: " + e.getMessage());
            }
        }));
    }

    public Optional<Kit> getKit(String kitName) {
        Kit[] kit = {null};

        executeQuery.execute(new PreparedStatementQuery("SELECT * FROM lifeserver_kit WHERE kit_name = ?", preparedStatement -> {
            try {
                preparedStatement.setString(1, kitName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        kit[0] = new Kit(
                                kitName,
                                resultSet.getInt("cooldown"),
                                resultSet.getString("kit_items")
                        );
                        kit[0].build();
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error: " + e.getMessage());
            }
        }));

        return Optional.ofNullable(kit[0]);
    }

    public List<Kit> getAllKits() {
        List<Kit> kits = new ArrayList<>();
        executeQuery.execute(new PreparedStatementQuery("SELECT * FROM lifeserver_kit", preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Kit kit = new Kit(
                            resultSet.getString("kit_name"),
                            resultSet.getInt("cooldown"),
                            resultSet.getString("kit_items")
                    );
                    kits.add(kit);
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error: " + e.getMessage());
            }
        }));
        return kits;
    }

    public void createTable() {
        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_kit (" +
                "kit_name VARCHAR(255) NOT NULL PRIMARY KEY, " +
                "kit_items VARCHAR(16384) NOT NULL, " +
                "cooldown INT NOT NULL" +
                ");", PreparedStatement::execute));
    }
}
