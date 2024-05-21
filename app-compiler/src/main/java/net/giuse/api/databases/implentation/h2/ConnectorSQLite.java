package net.giuse.api.databases.implentation.h2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import net.giuse.api.databases.Connector;

import java.sql.Connection;

/**
 * Connector for SQLite
 */
public class ConnectorSQLite implements Connector {

    private static HikariDataSource dataSource;

    @Override
    @SneakyThrows
    public synchronized void openConnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            return; // Già connesso
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:plugins/LifeServer/sql.db");
        config.setDriverClassName("org.sqlite.JDBC");

        // Configura HikariCP per le tue esigenze
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10); // Configura il numero massimo di connessioni nel pool

        dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public synchronized void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    @SneakyThrows
    public Connection getConnection() {
        if (dataSource == null || dataSource.isClosed()) {
            openConnect();
        }
        return dataSource.getConnection();
    }
}
