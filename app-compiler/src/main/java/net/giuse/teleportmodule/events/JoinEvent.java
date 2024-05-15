package net.giuse.teleportmodule.events;


import net.giuse.mainmodule.MainModule;
import net.giuse.teleportmodule.submodule.HomeLoaderModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import java.util.HashMap;

public class JoinEvent implements Listener {
    private final HomeLoaderModule homeLoaderModule;

    private final MainModule mainModule;

    @Inject
    public JoinEvent(MainModule mainModule) {
        this.mainModule = mainModule;
        homeLoaderModule = (HomeLoaderModule) mainModule.getService(HomeLoaderModule.class);
    }

    /*
     * Check if player has a HomeBuilder instance
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (homeLoaderModule.getHome(e.getPlayer().getUniqueId()) == null) {
            homeLoaderModule.getCacheHome().put(e.getPlayer().getUniqueId(), new HashMap<>());
        }
    }

}
