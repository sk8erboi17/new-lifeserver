package net.giuse.teleportmodule.submodule.dto;

import org.bukkit.Location;

public class Spawn {
    private final Location location;

    public Spawn(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}