package net.giuse.api.databases.execute;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCallback {
    void setPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
