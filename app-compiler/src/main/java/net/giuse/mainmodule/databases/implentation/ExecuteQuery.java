package net.giuse.mainmodule.databases.implentation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.mainmodule.databases.Connector;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class ExecuteQuery {

    private final Connector connector;

    @SneakyThrows
    public void execute(QueryCallback queryCallback) {
        String query = queryCallback.getQuery();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            try {
                queryCallback.getCallback().setPreparedStatement(preparedStatement);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                Bukkit.getLogger().severe(String.format(
                        "Database error, transaction rolled back: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(String.format(
                    "Database connection error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                    e.getMessage(), e.getSQLState(), e.getErrorCode(), query
            ));
        }
    }

    @SneakyThrows
    public void execute(String query) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                Bukkit.getLogger().severe(String.format(
                        "Database error, transaction rolled back: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(String.format(
                    "Database connection error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                    e.getMessage(), e.getSQLState(), e.getErrorCode(), query
            ));
        }
    }

    @SneakyThrows
    public void executeBatch(List<QueryCallback> queries) {
        try (Connection connection = connector.getConnection()) {

            connection.setAutoCommit(false);

            for (QueryCallback queryCallback : queries) {
                String query = queryCallback.getQuery();
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    queryCallback.getCallback().setPreparedStatement(preparedStatement);
                } catch (SQLException e) {
                    connection.rollback();
                    Bukkit.getLogger().severe(String.format(
                            "Database error, transaction rolled back: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                            e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                    ));
                    return; // Exit on first failure
                }
            }
            connection.commit();

        } catch (SQLException e) {
            Bukkit.getLogger().severe(String.format(
                    "Database connection error: %s (SQLState: %s, ErrorCode: %d)",
                    e.getMessage(), e.getSQLState(), e.getErrorCode()
            ));
        }
    }
}
