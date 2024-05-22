package net.giuse.api.databases.implentation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.api.databases.Connector;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class ExecuteQuery {

    private final Connector connector;

    @SneakyThrows
    public void execute(PreparedStatementQuery preparedStatementQuery) {
        String query = preparedStatementQuery.getQuery();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            try {
                preparedStatementQuery.getPreparedStatementCallback().setPreparedStatement(preparedStatement);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                Bukkit.getLogger().severe(String.format(
                        "Database error, transaction rolled back: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                ));
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(String.format(
                    "Database connection error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                    e.getMessage(), e.getSQLState(), e.getErrorCode(), query
            ));
            throw e;
        }
    }

    @SneakyThrows
    public void execute(String sql) {
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            try {
                statement.executeQuery(sql);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                Bukkit.getLogger().severe(String.format(
                        "Database connection error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                        e.getMessage(), e.getSQLState(), e.getErrorCode(), sql
                ));
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(String.format(
                    "Database connection error: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                    e.getMessage(), e.getSQLState(), e.getErrorCode(), sql
            ));
            throw e;
        }
    }

    @SneakyThrows
    public void executeBatch(List<PreparedStatementQuery> queries) {
        try (Connection connection = connector.getConnection()) {

            connection.setAutoCommit(false);

            try {
                for (PreparedStatementQuery preparedStatementQuery : queries) {
                    String query = preparedStatementQuery.getQuery();
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatementQuery.getPreparedStatementCallback().setPreparedStatement(preparedStatement);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                for (PreparedStatementQuery preparedStatementQuery : queries) {
                    String query = preparedStatementQuery.getQuery();
                    Bukkit.getLogger().severe(String.format(
                            "Database error, transaction rolled back: %s (SQLState: %s, ErrorCode: %d) Query: %s",
                            e.getMessage(), e.getSQLState(), e.getErrorCode(), query
                    ));
                }
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(String.format(
                    "Database connection error: %s (SQLState: %s, ErrorCode: %d)",
                    e.getMessage(), e.getSQLState(), e.getErrorCode()
            ));
            throw e;
        }
    }
}
