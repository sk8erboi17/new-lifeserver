package net.giuse.teleportmodule.submodule.spawn.repository;

import net.giuse.api.databases.execute.ExecuteQuery;
import net.giuse.api.databases.execute.querystructure.PreparedStatementQuery;
import net.giuse.teleportmodule.submodule.spawn.dto.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpawnRepository {

    @Inject
    private ExecuteQuery executeQuery;

    public void deleteSpawn() {
        String query = "DELETE FROM lifeserver_spawn WHERE id = 1";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete spawn", e);
            }
        }));
    }

    public Spawn getSpawn() {
        String query = "SELECT location FROM lifeserver_spawn WHERE id = 1";
        final Spawn[] spawn = {null};

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String locationString = resultSet.getString("location");
                    Location location = parseLocation(locationString);
                    spawn[0] = new Spawn(location);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get spawn", e);
            }
        }));

        return spawn[0];
    }

    public void setSpawn(Location location) {
        String checkQuery = "SELECT COUNT(*) FROM lifeserver_spawn WHERE id = 1";
        String insertQuery = "INSERT INTO lifeserver_spawn (id, location) VALUES (1, ?)";
        String updateQuery = "UPDATE lifeserver_spawn SET location = ? WHERE id = 1";

        executeQuery.execute(new PreparedStatementQuery(checkQuery, preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);

                if (count > 0) {
                    // Record exists, update it
                    executeQuery.execute(new PreparedStatementQuery(updateQuery, updatePreparedStatement -> {
                        updatePreparedStatement.setString(1, serializeLocation(location));
                        updatePreparedStatement.executeUpdate();
                    }));
                } else {
                    // Record does not exist, insert it
                    executeQuery.execute(new PreparedStatementQuery(insertQuery, insertPreparedStatement -> {
                        insertPreparedStatement.setString(1, serializeLocation(location));
                        insertPreparedStatement.executeUpdate();
                    }));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to set spawn", e);
            }
        }));
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

    public void createTable() {
        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_spawn (" +
                "id INTEGER PRIMARY KEY, " +
                "location VARCHAR(255)" +
                ")", PreparedStatement::execute));
    }
}
