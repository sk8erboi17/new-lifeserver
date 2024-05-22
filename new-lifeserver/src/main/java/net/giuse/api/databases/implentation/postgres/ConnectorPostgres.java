package net.giuse.api.databases.implentation.postgres;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import net.giuse.api.databases.Connector;
import net.giuse.mainmodule.MainModule;

import javax.inject.Inject;
import java.sql.Connection;

public class ConnectorPostgres implements Connector {

    private static HikariDataSource dataSource;

    @Inject
    private MainModule mainModule;

    @Override
    @SneakyThrows
    public synchronized void openConnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/" + mainModule.getConfig().getString("database"));
        config.setUsername(mainModule.getConfig().getString("username"));
        config.setPassword(mainModule.getConfig().getString("password"));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);

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
