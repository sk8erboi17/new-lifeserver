package net.giuse.teleportmodule.submodule.home.repository;

import net.giuse.api.databases.execute.ExecuteQuery;
import net.giuse.api.databases.execute.querystructure.PreparedStatementQuery;
import net.giuse.teleportmodule.submodule.home.dto.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeRepository {

    @Inject
    private ExecuteQuery executeQuery;

    public void addHome(String playerUuid, String name, String location) {
        String query = "INSERT INTO lifeserver_home (uuid, name, location) VALUES (?, ?, ?)";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, playerUuid);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, location);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to add home", e);
            }
        }));
    }

    public void updateHome(String playerUuid, String name, String newLocation) {
        String query = "UPDATE lifeserver_home SET location = ? WHERE uuid = ? AND name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, newLocation);
                preparedStatement.setString(2, playerUuid);
                preparedStatement.setString(3, name);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update home", e);
            }
        }));
    }

    public void removeHome(String playerUuid, String name) {
        String query = "DELETE FROM lifeserver_home WHERE uuid = ? AND name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, playerUuid);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to remove home", e);
            }
        }));
    }

    public List<Home> getAllHomes(String playerUuid) {
        String query = "SELECT name, location FROM lifeserver_home WHERE uuid = ?";
        List<Home> homes = new ArrayList<>();

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, playerUuid);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String locationString = resultSet.getString("Location");
                        Location location = parseLocation(locationString);
                        homes.add(new Home(name, location));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get homes", e);
            }
        }));

        return homes;
    }

    public Home getHome(String playerUuid, String name) {
        String query = "SELECT location FROM lifeserver_home WHERE uuid = ? AND name = ?";
        final Home[] home = {null};

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, playerUuid);
                preparedStatement.setString(2, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String locationString = resultSet.getString("Location");
                        Location location = parseLocation(locationString);
                        home[0] = new Home(name, location);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get home", e);
            }
        }));

        return home[0];
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
        executeQuery.execute(new PreparedStatementQuery("CREATE TABLE IF NOT EXISTS lifeserver_home (" +
                "uuid VARCHAR(255) NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                "location VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (UUID, Name));", PreparedStatement::execute));
    }

}
