package net.giuse.kitmodule.service;

import net.giuse.kitmodule.databases.PlayerKitRepository;
import net.giuse.kitmodule.dto.PlayerKit;
import net.giuse.mainmodule.MainModule;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PlayerKitService {
    @Inject
    private PlayerKitRepository playerKitRepository;
    @Inject
    private MainModule mainModule;

    public Integer getPlayerCooldown(UUID uniqueId, String kitName) {
        return playerKitRepository.getCooldown(uniqueId.toString(), kitName);
    }

    public List<PlayerKit> getPlayerKits() {
        return playerKitRepository.getAllPlayerKits();
    }

    public void addPlayerCooldown(UUID playerUuid, String kitName) {
        playerKitRepository.addPlayerKit(playerUuid.toString(), kitName);
    }

    public void removePlayerKit(String kitName) {
        playerKitRepository.removePlayerKit(kitName);
    }

    public void updateCooldown(UUID playerUuid, String kitName, int newCooldown) {
        playerKitRepository.updateCooldown(playerUuid.toString(), kitName, newCooldown);
    }

    public void startTimer() {
        for (PlayerKit allPlayerKit : playerKitRepository.getAllPlayerKits()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    int newCooldown = (getPlayerCooldown(UUID.fromString(allPlayerKit.getPlayerUuid()), allPlayerKit.getKitName()) - 1);
                    if (newCooldown <= 0) {
                        this.cancel();
                        updateCooldown(UUID.fromString(allPlayerKit.getPlayerUuid()), allPlayerKit.getKitName(), 0);
                    } else {
                        updateCooldown(UUID.fromString(allPlayerKit.getPlayerUuid()), allPlayerKit.getKitName(), newCooldown);

                    }
                }
            }.runTaskTimer(mainModule, new Random().nextInt(120), 20L);
        }
    }
}
