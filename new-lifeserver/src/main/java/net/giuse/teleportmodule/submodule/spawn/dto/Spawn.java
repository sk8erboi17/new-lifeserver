package net.giuse.teleportmodule.submodule.spawn.dto;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class Spawn {

    private final Location location;

    public Spawn(Location location) {
        this.location = location;
    }

}