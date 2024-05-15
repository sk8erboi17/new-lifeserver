package net.giuse.mainmodule;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import lombok.SneakyThrows;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.MessageLoader;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.databases.Connector;
import net.giuse.mainmodule.databases.implentation.ExecuteQuery;
import net.giuse.mainmodule.databases.implentation.h2.ConnectorSQLite;
import net.giuse.mainmodule.databases.implentation.postgres.ConnectorPostgres;
import net.giuse.mainmodule.files.FilesList;
import net.giuse.mainmodule.files.SQLFile;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.gui.GuiInitializer;
import net.giuse.mainmodule.message.MessageLoaderMain;
import net.giuse.mainmodule.services.Services;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.logging.Logger;

public class MainModule extends JavaPlugin {

    private Injector injector;

    private Connector connector;

    private final Reflections reflections = new Reflections("net.giuse");

    private HashMap<Services, Integer> servicesByPriority = new HashMap<>();


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
        servicesByPriority.keySet().forEach(Services::unload);
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
        reflections.getSubTypesOf(Services.class).forEach(serviceKlass -> {
            Services services = injector.newInstance(serviceKlass);
            servicesByPriority.put(services, services.priority());
        });
        servicesByPriority = (HashMap<Services, Integer>) Utils.sortByValue(servicesByPriority);
        servicesByPriority.keySet().forEach(Services::load);
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

    /*
     * Get a Service by Class
     */
    public Services getService(Class<?> name) {
        return servicesByPriority.keySet().stream().filter(services -> services.getClass().equals(name)).findFirst().orElseThrow(() -> new NullPointerException("No Service Found"));
    }
}
