package net.giuse.mainmodule.utils;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/*
 *   Utility Class
 */
public class Utils {

    /*
     * Register Commands
     */
    @SneakyThrows
    public static void registerCommand(String name, Command command) {
        Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        commandMap.register(name, command);
    }

    /*
     * Convert Seconds in Time Format
     */
    public static String formatTime(int secs) {
        int remainder = secs % 86400;
        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = remainder / 60 - hours * 60;
        int seconds = remainder % 3600 - minutes * 60;
        String fDays = (days > 0) ? (" " + days + " day" + ((days > 1) ? "s" : "")) : "";
        String fHours = (hours > 0) ? (" " + hours + " hour" + ((hours > 1) ? "s" : "")) : "";
        String fMinutes = (minutes > 0) ? (" " + minutes + " minute" + ((minutes > 1) ? "s" : "")) : "";
        String fSeconds = (seconds > 0) ? (" " + seconds + " second" + ((seconds > 1) ? "s" : "")) : "";
        return fDays.concat(fHours).concat(fMinutes).concat(fSeconds);
    }


    /**
     * A method to serialize an ItemStack array to Base64 String.
     * <p>
     * <p/>
     * <p>
     * src: https://gist.github.com/graywolf336/8153678
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     */
    public static String listItemStackToBase64(List<ItemStack> items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.size());

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }


    /**
     * Gets an array of ItemStacks from Base64 string.
     * <p>
     * <p/>
     * <p>
     * src: https://gist.github.com/graywolf336/8153678
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    @SneakyThrows
    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
