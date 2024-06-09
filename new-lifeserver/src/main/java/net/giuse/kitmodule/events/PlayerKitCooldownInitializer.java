package net.giuse.kitmodule.events;


import net.giuse.kitmodule.dto.Kit;
import net.giuse.kitmodule.service.KitService;
import net.giuse.kitmodule.service.PlayerKitService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;


/**
 * Register a PlayerTimerSystem on Join
 */

public class PlayerKitCooldownInitializer implements Listener {

    @Inject
    private KitService kitService;

    @Inject
    private PlayerKitService playerKitService;

    /*
     * Check if player on join has the timer tasks of kits
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (Kit allKit : kitService.getAllKits()) {
            Integer cooldown = playerKitService.getPlayerCooldown(e.getPlayer().getUniqueId(), allKit.getName());

            if (cooldown == null) {
                playerKitService.addPlayerCooldown(e.getPlayer().getUniqueId(), allKit.getName());
                playerKitService.updateCooldown(e.getPlayer().getUniqueId(), allKit.getName(), 0);
            }

        }
    }
}