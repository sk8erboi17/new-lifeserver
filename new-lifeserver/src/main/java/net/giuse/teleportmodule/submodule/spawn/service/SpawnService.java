package net.giuse.teleportmodule.submodule.spawn.service;

import net.giuse.teleportmodule.submodule.spawn.dto.Spawn;
import net.giuse.teleportmodule.submodule.spawn.repository.SpawnRepository;
import org.bukkit.Location;

import javax.inject.Inject;

public class SpawnService {

    @Inject
    private SpawnRepository spawnRepository;

    public void deleteSpawn() {
        spawnRepository.deleteSpawn();
    }

    public Spawn getSpawn() {
        return spawnRepository.getSpawn();
    }

    public void setSpawn(Location location) {
        spawnRepository.setSpawn(location);
    }
}
