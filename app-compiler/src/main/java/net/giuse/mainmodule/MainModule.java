package net.giuse.mainmodule;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import lombok.SneakyThrows;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.databases.Connector;
import net.giuse.api.databases.implentation.ExecuteQuery;
import net.giuse.api.databases.implentation.h2.ConnectorSQLite;
import net.giuse.api.databases.implentation.postgres.ConnectorPostgres;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.MessageLoader;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.api.inventorylib.gui.GuiInitializer;
import net.giuse.mainmodule.files.FilesList;
import net.giuse.mainmodule.files.SQLFile;
import net.giuse.mainmodule.message.MessageLoaderMain;
import net.giuse.mainmodule.services.Services;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.*;
import java.util.logging.Logger;

public class MainModule extends JavaPlugin {

    private Injector injector;

    private Connector connector;

    private final Reflections reflections = new Reflections("net.giuse");

    private List<Services> services = new ArrayList<>();


    /*
     * Enable MainModule
     */
    @Override
    public void onEnable() {
        //Get current millis for check startup time
        long millis = System.currentTimeMillis();
        getLogger().info("§aLifeserver starting...");

        //setup
        setupInjector();
        setupMessage();
        setupFiles();
        setupSQL();

        //open connection
        connector.openConnect();

        //another setup
        setupService();
        setupCommands();
        setupGuis();
        setupListeners();

        //close connection
        connector.closeConnection();

        getLogger().info("§aLifeserver started in " + (System.currentTimeMillis() - millis) + "ms...");
    }

    /*
     * Disable MainModule
     */
    @Override
    public void onDisable() {
        //Unload services
        connector.openConnect();
        services.forEach(Services::unload);
        connector.closeConnection();
    }

    /*
     * Setup Files
     */
    private void setupFiles() {
        //Setup in default dir
        for (FilesList pathFile : FilesList.values()) {
            saveResource(pathFile.toString(), false);
        }
    }

    /*
     * Setup Injector
     */
    private void setupInjector() {
        injector = new InjectorBuilder().addDefaultHandlers("net.giuse").create();
        injector.register(MainModule.class, this);
        injector.register(Logger.class, getLogger());
        injector.register(FileConfiguration.class, getConfig());
    }

    /*
     * Setup Messages
     */
    private void setupMessage() {
        injector.register(MessageLoader.class, new MessageLoader(this));
        injector.register(MessageBuilder.class, new MessageBuilder(injector.getIfAvailable(MessageLoader.class)));
        injector.getSingleton(MessageLoaderMain.class).load();
    }

    /*
     * Setup services
     */
    private void setupService() {
        Set<Class<? extends Services>> serviceClasses = reflections.getSubTypesOf(Services.class);

        for (Class<? extends Services> serviceClass : serviceClasses) {
            Services servicesSingle = injector.getSingleton(serviceClass);
            servicesSingle.load();
            services.add(servicesSingle);
        }
    }


    /*
     * Setup SQL
     */
    @SneakyThrows
    private void setupSQL() {
        if (getConfig().getString("storage-type").equalsIgnoreCase("postgres")) {
            connector = injector.getSingleton(ConnectorPostgres.class);
        } else if (getConfig().getString("storage-type").equalsIgnoreCase("h2")) {
            ReflectionsFiles.loadFiles(new SQLFile());
            connector = injector.getSingleton(ConnectorSQLite.class);
        }
        injector.register(ExecuteQuery.class, new ExecuteQuery(connector));
    }

    /*
     * Setup Listeners
     */
    private void setupListeners() {
        reflections.getSubTypesOf(Listener.class).stream()
                .filter(listenerClass -> !listenerClass.getSimpleName().equalsIgnoreCase("FoodEvent")
                        && !listenerClass.getSimpleName().equalsIgnoreCase("EntityBackOnDeath")
                        && !listenerClass.getSimpleName().equalsIgnoreCase("ButtonBuilder")
                        && !listenerClass.getSimpleName().equalsIgnoreCase("InventoryBuilder"))
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(injector.getSingleton(listener), this));
    }

    /*
     * Setup Guis
     */

    private void setupGuis() {
        for (Class<? extends GuiInitializer> guiInitializer : reflections.getSubTypesOf(GuiInitializer.class)) {
            injector.getSingleton(guiInitializer).initInv();
        }
    }

    /*
     * Setup Commands
     */
    private void setupCommands() {
        for (Class<? extends AbstractCommand> command : reflections.getSubTypesOf(AbstractCommand.class)) {
            Utils.registerCommand(command.getName(), injector.getSingleton(command));
        }
    }

}
