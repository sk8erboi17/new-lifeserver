package net.giuse.economymodule.repository;

import net.giuse.api.databases.execute.ExecuteQuery;
import net.giuse.api.databases.execute.querystructure.PreparedStatementQuery;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyRepository {
    @Inject
    private ExecuteQuery executeQuery;

    @Inject
    private FileConfiguration mainConfig;

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
        String checkQuery = "SELECT COUNT(*) FROM lifeserver_economy WHERE uuid = ?";
        String insertQuery = "INSERT INTO lifeserver_economy (uuid, balance) VALUES (?, ?)";
        String updateQuery = "UPDATE lifeserver_economy SET balance = ? WHERE uuid = ?";

        // Check if the record exists
        executeQuery.execute(new PreparedStatementQuery(checkQuery, preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Record exists, update it
                executeQuery.execute(new PreparedStatementQuery(updateQuery, updatePreparedStatement -> {
                    updatePreparedStatement.setBigDecimal(1, balance);
                    updatePreparedStatement.setString(2, uuid.toString());
                    updatePreparedStatement.executeUpdate();
                }));
            } else {
                // Record does not exist, insert it
                executeQuery.execute(new PreparedStatementQuery(insertQuery, insertPreparedStatement -> {
                    insertPreparedStatement.setString(1, uuid.toString());
                    insertPreparedStatement.setBigDecimal(2, balance);
                    insertPreparedStatement.executeUpdate();
                }));
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

    public Map<UUID, BigDecimal> getTopBalances() {
        String query = "SELECT uuid, balance FROM lifeserver_economy ORDER BY balance DESC LIMIT " + mainConfig.getInt("top-balance-show");
        Map<UUID, BigDecimal> topBalances = new HashMap<>();

        executeQuery.execute(new PreparedStatementQuery(query, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    BigDecimal balance = resultSet.getBigDecimal("balance");
                    topBalances.put(uuid, balance);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get top balances", e);
            }
        }));

        return topBalances;
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS lifeserver_economy (" +
                "uuid VARCHAR(255) PRIMARY KEY, " +
                "balance DECIMAL(20, 2))";
        executeQuery.execute(new PreparedStatementQuery(query, PreparedStatement::execute));
    }

}

