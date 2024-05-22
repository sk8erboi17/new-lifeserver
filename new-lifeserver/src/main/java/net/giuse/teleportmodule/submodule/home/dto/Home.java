package net.giuse.teleportmodule.submodule.home.dto;


import lombok.Getter;
import org.bukkit.Location;

@Getter
public class Home {
    private final String name;
    private final Location location;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

}
