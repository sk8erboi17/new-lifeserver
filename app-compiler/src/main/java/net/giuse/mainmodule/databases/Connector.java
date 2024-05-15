package net.giuse.mainmodule.databases;

import java.sql.Connection;

/**
 * Connector for databases
 */
public interface Connector {

    void openConnect();

    void closeConnection();

    Connection getConnection();
}
