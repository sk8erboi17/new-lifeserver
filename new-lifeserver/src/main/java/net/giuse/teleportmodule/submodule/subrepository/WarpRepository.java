package net.giuse.teleportmodule.submodule.subrepository;


import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;
import net.giuse.teleportmodule.submodule.dto.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarpRepository {
    @Inject
    private ExecuteQuery executeQuery;

    public void addWarp(String name, String location) {
        String query = "INSERT INTO lifeserver_warp (name, location) VALUES (?, ?)";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, location);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to add warp", e);
            }
        }));
    }

    public void updateWarp(String name, String newLocation) {
        String query = "UPDATE lifeserver_warp SET location = ? WHERE name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, newLocation);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update warp", e);
            }
        }));
    }

    public void removeWarp(String name) {
        String query = "DELETE FROM lifeserver_warp WHERE name = ?";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to remove warp", e);
            }
        }));
    }

    public List<Warp> getAllWarps() {
        String query = "SELECT name, location FROM lifeserver_warp";
        List<Warp> warps = new ArrayList<>();

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String locationString = resultSet.getString("Location");
                    Location location = parseLocation(locationString);
                    warps.add(new Warp(name, location));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get warps", e);
            }
        }));

        return warps;
    }

    public Warp getWarp(String name) {
        String query = "SELECT Location FROM lifeserver_warp WHERE name = ?";
        final Warp[] warp = {null};

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String locationString = resultSet.getString("Location");
                        Location location = parseLocation(locationString);
                        warp[0] = new Warp(name, location);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get warp", e);
            }
        }));

        return warp[0];
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
