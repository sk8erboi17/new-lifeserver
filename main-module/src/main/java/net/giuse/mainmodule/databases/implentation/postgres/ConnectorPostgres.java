package net.giuse.mainmodule.databases.implentation.postgres;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Connector;


import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectorPostgres implements Connector {

    /**
     * Connector for Postgres
     */
    @Getter
    private Connection connection;

    @Inject
    private MainModule mainModule;

    @Override
    @SneakyThrows
    public void openConnect() {
        Class.forName("org.postgresql.Driver");
        // Set connection properties
        String url = "jdbc:postgresql://localhost:5432/minecraft";
        Properties props = new Properties();
        props.setProperty("user", mainModule.getConfig().getString("username"));
        props.setProperty("password",  mainModule.getConfig().getString("password"));

        // Establish a connection to the PostgreSQL database
        this.connection = DriverManager.getConnection(url, props);
    }

    @SneakyThrows
    public void closeConnection() {
        if (connection != null) {
            this.connection.close();
        }
    }

}
