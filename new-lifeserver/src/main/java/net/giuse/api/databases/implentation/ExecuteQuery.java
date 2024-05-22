package net.giuse.api.databases.implentation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.api.databases.Connector;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

}
