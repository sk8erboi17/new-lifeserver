package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

@Getter
public class TeleportRequestModule extends Services {
    private final Set<PendingRequest> pendingRequests = new HashSet<>();
    
    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Teleport Requests...");
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Teleport Requests...");
    }


    /*
     * Get a PendingRequest from a player's UUID
     */
    public PendingRequest getPending(UUID uuid) {
        return pendingRequests.stream()
                .filter(pendingRequests -> pendingRequests.getReceiver().getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

}
