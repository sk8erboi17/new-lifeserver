package net.giuse.mainmodule.databases.implentation;


import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.Callback;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecuteQuery {
    private final MainModule mainModule;


    @Inject
    @SneakyThrows
    public ExecuteQuery(MainModule mainModule) {
        this.mainModule = mainModule;
    }

    @SneakyThrows
    public void execute(Callback callback, String query) {
        try (PreparedStatement preparedStatement = mainModule.getConnector().getConnection().prepareStatement(query)) {
            callback.setQuery(preparedStatement);
        } catch (SQLException e) {
            Bukkit.getLogger().info("Empty Database");
        }
    }


    @SneakyThrows
    public void execute(String query) {
        try (PreparedStatement preparedStatement = mainModule.getConnector().getConnection().prepareStatement(query)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Empty Database");
        }
    }

}
