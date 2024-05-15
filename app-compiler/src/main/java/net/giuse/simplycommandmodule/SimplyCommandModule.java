package net.giuse.simplycommandmodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.services.Services;
import net.giuse.simplycommandmodule.events.FoodEvent;
import net.giuse.simplycommandmodule.files.FileManager;
import net.giuse.simplycommandmodule.messages.MessageLoaderSimplyCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class SimplyCommandModule extends Services {

    @Getter
    private final HashMap<String, String> message = new HashMap<>();

    @Getter
    private final ArrayList<String> stringsNameGods = new ArrayList<>();

    @Inject
    private Injector injector;
    @Inject
    private Logger logger;
    @Inject
    private FileConfiguration mainConfig;
    @Inject
    private MainModule mainModule;
    @Getter
    private FileManager fileManager;

    @Getter
    private MessageLoaderSimplyCommand messageLoaderSimplyCommand;

    /**
     * This is Module of Simply Command
     * Most of these commands are not async executable so they will not pass through the process engine
     */
    @Override
    @SneakyThrows
    public void load() {
        logger.info("§8[§2Life§aServer §7>> §eGeneral Commands§9] §7Loading General Commands...");
        injector.register(SimplyCommandModule.class, this);
        messageLoaderSimplyCommand = injector.getSingleton(MessageLoaderSimplyCommand.class);
        ReflectionsFiles.loadFiles(fileManager = new FileManager());
        messageLoaderSimplyCommand.load();

        message.put("no-perms", mainConfig.getString("no-perms"));

        if (mainConfig.getBoolean("no-hunger")) {
            Bukkit.getServer().getPluginManager().registerEvents(new FoodEvent(), mainModule);
        }
        if (mainModule.getConfig().getBoolean("always-day")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (World world : Bukkit.getWorlds()) {
                        world.setTime(24000L);
                        world.setStorm(false);
                        world.setThundering(false);
                    }
                }
            }.runTaskTimer(mainModule, 20L * 20L, 20L * 20L);
        }

    }

    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eGeneral Commands§9] §7Unloading General Commands...");
    }

    @Override
    public int priority() {
        return 0;
    }

}
