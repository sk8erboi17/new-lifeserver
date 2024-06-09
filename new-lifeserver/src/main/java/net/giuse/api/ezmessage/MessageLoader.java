package net.giuse.api.ezmessage;


import lombok.Getter;
import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.mainmodule.MainModule;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.time.Duration;
import java.util.HashMap;


public class MessageLoader {

    public final MainModule javaPlugin;

    private final BukkitAudiences audience;

    @Getter
    private final HashMap<String, Message> cache = new HashMap<>();;

    @Inject
    public MessageLoader(MainModule javaPlugin) {
        this.javaPlugin = javaPlugin;
        audience = BukkitAudiences.create(javaPlugin);
    }

    /*
     * Send title to Player
     */
    public void sendTitle(Player player, Component title, Component subTitle, int fadeIn, int stay, int fadeOut) {
        Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
        Title titleToSend = Title.title(title, subTitle, times);
        audience.player(player).showTitle(titleToSend);
    }

    /*
     * Send message to Player
     */
    public void sendChat(CommandSender player, Component messageChat) {
        audience.sender(player).sendMessage(messageChat);

    }


    /*
     * Send message to Player
     */
    public void sendActionBar(Player player, Component send) {
        audience.player(player).sendActionBar(send);
    }

    /*
     * Add Message to cache
     */

    public void addMessageCache(String id, Message message) {
        cache.put(id, message);
    }

    /*
     * Add Message to cache
     */

    public void clearCache() {
        cache.clear();
    }

}
