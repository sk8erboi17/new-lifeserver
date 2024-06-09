package net.giuse.teleportmodule.events;


import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.inject.Inject;

public class EntityBackOnDeath implements Listener {

    private final TeleportModule teleportModule;

    @Inject
    public EntityBackOnDeath(TeleportModule teleportModule) {
        this.teleportModule = teleportModule;
    }

    /*
     * Event for back on death
     */
    @EventHandler
    public void onTeleport(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            teleportModule.getBackLocations().put((Player) e.getEntity(), e.getEntity().getLocation());
        }
    }


}
