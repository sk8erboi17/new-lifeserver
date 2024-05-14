package net.giuse.mainmodule.databases.execute;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Callback {
    void setPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
