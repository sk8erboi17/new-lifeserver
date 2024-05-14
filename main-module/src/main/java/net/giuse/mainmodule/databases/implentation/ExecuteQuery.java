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
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryCallback.getQuery())) {

            connection.setAutoCommit(false);  // Disabilita l'auto commit

            try {
                queryCallback.getCallback().setPreparedStatement(preparedStatement);
                connection.commit();  // Conferma la transazione
            } catch (SQLException e) {
                connection.rollback();  // Annulla la transazione in caso di errore
                Bukkit.getLogger().info("Database error, transaction rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            Bukkit.getLogger().info("Database connection error: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void execute(String query) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);  // Disabilita l'auto commit

            try {
                preparedStatement.execute();
                connection.commit();  // Conferma la transazione
            } catch (SQLException e) {
                connection.rollback();  // Annulla la transazione in caso di errore
                Bukkit.getLogger().info("Database error, transaction rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            Bukkit.getLogger().info("Database connection error: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void executeBatch(List<QueryCallback> queries) {
        try (Connection connection = connector.getConnection()) {

            connection.setAutoCommit(false);

            try {
                for (QueryCallback queryCallback : queries) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(queryCallback.getQuery())) {
                        queryCallback.getCallback().setPreparedStatement(preparedStatement);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                Bukkit.getLogger().info("Database error, transaction rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            Bukkit.getLogger().info("Database connection error: " + e.getMessage());
        }
    }

}
