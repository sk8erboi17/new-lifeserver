package net.giuse.mainmodule.databases.implentation.h2;


import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.databases.Connector;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Connector for SQLite
 */
public class ConnectorSQLite implements Connector {

    private Connection connection;

    @Override
    @SneakyThrows
    public void openConnect() {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/LifeServer/sql.db");
    }

    @SneakyThrows
    public void closeConnection() {
        if (connection == null) {
            return;
        }
        this.connection.close();
    }
    @Override
    @SneakyThrows
    public Connection getConnection() {
        if(connection == null|| connection.isClosed()){
            openConnect();
        }
        return connection;
    }

}

