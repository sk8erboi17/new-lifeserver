package net.giuse.economymodule.repository;

import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.PreparedStatementQuery;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EconomyRepository {
    @Inject
    private ExecuteQuery executeQuery;

    public BigDecimal getBalance(UUID uuid) {
        String query = "SELECT balance FROM lifeserver_economy WHERE uuid = ?";
        final BigDecimal[] balance = {BigDecimal.ZERO};

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        balance[0] = resultSet.getBigDecimal("balance");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get balance", e);
            }
        }));

        return balance[0];
    }

    public void setBalance(UUID uuid, BigDecimal balance) {
        String query = "INSERT OR REPLACE INTO lifeserver_economy (uuid, balance) VALUES (?, ?)";

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setBigDecimal(2, balance);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to set balance", e);
            }
        }));
    }

    public boolean playerExists(UUID uuid) {
        String query = "SELECT COUNT(*) FROM lifeserver_economy WHERE uuid = ?";
        final boolean[] exists = {false};

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try {
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        exists[0] = resultSet.getInt(1) > 0;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to check if player exists", e);
            }
        }));

        return exists[0];
    }
}

