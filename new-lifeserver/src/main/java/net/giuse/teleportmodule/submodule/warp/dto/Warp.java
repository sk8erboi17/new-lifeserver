package net.giuse.teleportmodule.submodule.warp.dto;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class Warp {

    private final String name;

    private final Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

}
