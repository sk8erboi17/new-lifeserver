package net.giuse.signmodule.databases;

import net.giuse.api.databases.execute.ExecuteQuery;
import net.giuse.api.databases.execute.querystructure.PreparedStatementQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SignPreviewRepository {

    @Inject
    private ExecuteQuery executeQuery;

    public void addSignPreview(Location location, String kitName) {
        executeQuery.execute(new PreparedStatementQuery("INSERT INTO lifeserver_sign_preview (uuid, location, kit_name) VALUES(?,?,?)", preparedStatement -> {
            try {
                preparedStatement.setString(1, UUID.randomUUID().toString());
                preparedStatement.setString(2, serializeLocation(location));
                preparedStatement.setString(3, kitName);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error, transaction rolled back: " + e.getMessage());
            }
        }));
    }

    public Optional<String> getKitNameByLocation(Location location) {
        AtomicReference<String> kitName = new AtomicReference<>();
        executeQuery.execute(new PreparedStatementQuery("SELECT kit_name FROM lifeserver_sign_preview WHERE location = ?", preparedStatement -> {
            try {
                preparedStatement.setString(1, serializeLocation(location));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        kitName.set(resultSet.getString("kit_name"));
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error: " + e.getMessage());
            }
        }));
        return Optional.ofNullable(kitName.get());
    }


    public void removeSignByLocation(Location location) {
        executeQuery.execute(new PreparedStatementQuery("DELETE FROM lifeserver_sign_preview WHERE location = ?", preparedStatement -> {
            try {
                preparedStatement.setString(1, serializeLocation(location));
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error: " + e.getMessage());
            }
        }));
    }

    public Location getSignByLocation(Location location) {
        AtomicReference<Location> location1 = new AtomicReference<>();
        executeQuery.execute(new PreparedStatementQuery("SELECT * FROM lifeserver_sign_preview WHERE location = ?", preparedStatement -> {
            try {
                preparedStatement.setString(1, serializeLocation(location));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        location1.set(parseLocation(resultSet.getString(2)));
                    }
                }


            } catch (SQLException e) {
                Bukkit.getLogger().info("[KIT] Database error: " + e.getMessage());
            }
        }));
        return location1.get();
    }

    public void createTable() {
        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_sign_preview (" +
                "uuid VARCHAR(255) NOT NULL PRIMARY KEY, " +
                "location VARCHAR(255) NOT NULL, " +
                "kit_name VARCHAR(255) NOT NULL, " +
                "FOREIGN KEY (kit_name) REFERENCES lifeserver_kit(kit_name)" +
                ");", PreparedStatement::execute));
    }


    private String serializeLocation(Location location) {
        return location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

    private Location parseLocation(String locationString) {
        String[] parts = locationString.split(",");
        String worldName = parts[0];
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
