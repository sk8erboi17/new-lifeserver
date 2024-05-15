package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.UUID;

public class HomeBuilderSerializer implements Serializer<HomeSerialized> {

    @Inject
    private MainModule mainModule;

    /*
     * Convert String to HomeBuilder
     */
    @Override
    public String encode(HomeSerialized homeSerializedObject) {
        StringBuilder homeSerialized = new StringBuilder();
        int i = 0;
        homeSerialized.append(homeSerializedObject.getOwner().toString()).append(":");
        for (String s : homeSerializedObject.getLocations().keySet()) {
            i++;
            homeSerialized.append(s).append(",").append(homeSerializedObject.getLocations().get(s).getWorld().getName())
                    .append(",").append(homeSerializedObject.getLocations().get(s).getX())
                    .append(",").append(homeSerializedObject.getLocations().get(s).getY()).append(",")
                    .append(homeSerializedObject.getLocations().get(s).getZ()).append(",")
                    .append(homeSerializedObject.getLocations().get(s).getYaw())
                    .append(",").append(homeSerializedObject.getLocations().get(s).getPitch());
            if (i != homeSerializedObject.getLocations().size()) {
                homeSerialized.append(";");
            }
        }
        return homeSerialized.toString();
    }

    /*
     * Convert HomeBuilder to String
     */
    @Override
    public HomeSerialized decoder(String str) {
        if (str == null) return null;

        String[] homes = str.split(":");
        HomeSerialized homeSerialized = new HomeSerialized(UUID.fromString(homes[0]), new HashMap<>());

        if (homes.length == 1) return homeSerialized;

        if (homes[1].contains(";")) {
            for (String s : homes[1].split(";")) {
                String[] variousHome = s.split(",");
                World world = Bukkit.getWorld(variousHome[1]);
                if (world != null) {
                    homeSerialized.getLocations().put(variousHome[0], new Location(
                            world,
                            Double.parseDouble(variousHome[2]),
                            Double.parseDouble(variousHome[3]),
                            Double.parseDouble(variousHome[4])
                    ));
                } else {
                    Bukkit.getLogger().severe("World " + variousHome[1] + " is not loaded or does not exist.");
                }
            }
        } else {
            String[] defaultHome = homes[1].split(",");
            World world = Bukkit.getWorld(defaultHome[1]);
            if (world != null) {
                homeSerialized.getLocations().put(defaultHome[0], new Location(
                        world,
                        Double.parseDouble(defaultHome[2]),
                        Double.parseDouble(defaultHome[3]),
                        Double.parseDouble(defaultHome[4])
                ));
            } else {
                Bukkit.getLogger().severe("World " + defaultHome[1] + " is not loaded or does not exist.");
            }
        }
        return homeSerialized;
    }

}


