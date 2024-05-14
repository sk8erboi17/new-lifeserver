package net.giuse.teleportmodule.submodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class TeleportRequestModule extends Services {
    @Getter
    private final Set<PendingRequest> pendingRequests = new HashSet<>();
    @Inject
    private Injector injector;
    @Inject
    private Logger logger;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        logger.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Teleport Requests...");
        injector.register(TeleportRequestModule.class, this);

    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        logger.info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Teleport Requests...");
    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
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
