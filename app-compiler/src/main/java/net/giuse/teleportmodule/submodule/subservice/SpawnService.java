package net.giuse.teleportmodule.submodule.subservice;

import net.giuse.teleportmodule.submodule.dto.Spawn;
import net.giuse.teleportmodule.submodule.subrepository.SpawnRepository;
import org.bukkit.Location;

import javax.inject.Inject;

public class SpawnService {
    @Inject
    private SpawnRepository spawnRepository;

    public void setSpawn(Location location) {
        spawnRepository.setSpawn(location);
    }

    public void deleteSpawn() {
        spawnRepository.deleteSpawn();
    }

    public Spawn getSpawn() {
        return spawnRepository.getSpawn();
    }
}
