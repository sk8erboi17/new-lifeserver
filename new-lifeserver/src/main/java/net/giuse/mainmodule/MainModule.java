package net.giuse.mainmodule;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.databases.Connector;
import net.giuse.api.databases.execute.ExecuteQuery;
import net.giuse.api.databases.implentation.h2.ConnectorH2;
import net.giuse.api.databases.implentation.postgres.ConnectorPostgres;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.MessageLoader;
import net.giuse.api.inventorylib.gui.GuiInitializer;
import net.giuse.mainmodule.files.FilesList;
import net.giuse.mainmodule.message.MessageLoaderMain;
import net.giuse.mainmodule.modules.AbstractModule;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainModule extends JavaPlugin {

    @Getter
    private final List<AbstractModule> services = new ArrayList<>();
    private final BukkitLibraryManager libraryManager = new BukkitLibraryManager(this);
    private Injector injector;
    private Connector connector;
    private Reflections reflections;
    private MessageLoaderMain messageLoaderMain;

    /*
     * Enable MainModule
     */
    @Override
    public void onEnable() {
        //Get current millis for check startup time
        long millis = System.currentTimeMillis();
        Bukkit.getLogger().info("§aLifeserver starting...");

        //setup
        setupDependencies();
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
        Bukkit.getLogger().info("§aLifeserver started in §2" + (System.currentTimeMillis() - millis) + "§ams...");
    }

    /*
     * Disable MainModule
     */
    @Override
    public void onDisable() {
        //Unload services
        connector.openConnect();
        services.forEach(AbstractModule::unload);
        connector.closeConnection();
    }

    /*
     * Setup Files
     */
    private void setupFiles() {
        //Setup in default dir
        for (FilesList pathFile : FilesList.values()) {
            if (!new File(getDataFolder(), pathFile.toString()).exists()) {
                saveResource(pathFile.toString(), false);
            }
        }
    }

    /*
     * Setup dependencies
     */
    private void setupDependencies() {
        Library postgresql = Library.builder()
                .groupId("org{}postgresql") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("postgresql")
                .version("42.7.3")
                // Relocation is applied to the downloaded jar before loading it
                .relocate("org{}postgres", "net{}lib{}org{}postgres")
                .build();

        Library h2 = Library.builder()
                .groupId("com{}h2database")
                .artifactId("h2")
                .version("2.1.210")
                .relocate("com{}h2database", "net{}lib{}com{}h2")
                .build();

        Library hikariCp = Library.builder()
                .groupId("com{}zaxxer")
                .artifactId("HikariCP")
                .version("3.4.5")
                .relocate("com{}zaxxer", "net{}lib{}zaxxer")
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(postgresql);
        libraryManager.loadLibrary(h2);
        libraryManager.loadLibrary(hikariCp);
    }

    /*
     * Setup Injector
     */
    private void setupInjector() {
        injector = new InjectorBuilder().addDefaultHandlers("net.giuse").create();
        injector.register(MainModule.class, this);
        reflections = new Reflections("net.giuse");
        injector.register(FileConfiguration.class, getConfig());
    }

    /*
     * Setup Messages
     */
    private void setupMessage() {
        injector.register(MessageLoader.class, new MessageLoader(this));
        injector.register(MessageBuilder.class, new MessageBuilder(injector.getIfAvailable(MessageLoader.class)));
        messageLoaderMain = injector.getSingleton(MessageLoaderMain.class);
        messageLoaderMain.load();
    }

    /*
     * Setup services
     */
    private void setupService() {
        Set<Class<? extends AbstractModule>> serviceClasses = reflections.getSubTypesOf(AbstractModule.class);

        for (Class<? extends AbstractModule> serviceClass : serviceClasses) {
            AbstractModule abstractModuleSingle = injector.getSingleton(serviceClass);
            abstractModuleSingle.load();
            services.add(abstractModuleSingle);
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
            connector = injector.getSingleton(ConnectorH2.class);
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

    public void reloadMessage() {
        messageLoaderMain.load();
    }

}
