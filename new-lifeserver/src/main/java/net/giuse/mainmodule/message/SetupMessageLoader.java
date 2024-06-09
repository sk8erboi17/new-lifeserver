package net.giuse.mainmodule.message;


import net.giuse.api.ezmessage.MessageLoader;
import net.giuse.api.ezmessage.messages.MessageActionbar;
import net.giuse.api.ezmessage.messages.MessageChat;
import net.giuse.api.ezmessage.messages.MessageTitle;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;

public abstract class SetupMessageLoader {

    @Inject
    private MessageLoader messageLoader;

    public abstract void load();

    protected void setupActionBar(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("actionbar.send-actionbar", section)) {
            String messageComponent = ChatColor.translateAlternateColorCodes('&', section.getString("actionbar.actionbar-message"));
            MessageActionbar message = new MessageActionbar(messageComponent);
            messageLoader.addMessageCache(idMessage + "_bossbar", message);
        }
    }

    protected void setupMessageChat(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("message-chat.send-chat", section)) {
            String messageComponent = ChatColor.translateAlternateColorCodes('&', section.getString("message-chat.string-message"));
            MessageChat message = new MessageChat(messageComponent);
            messageLoader.addMessageCache(idMessage + "_chat", message);
        }
    }

    protected void setupTitle(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("title.send-title", section)) {
            String subTitle = ChatColor.translateAlternateColorCodes('&', section.getString("title.subtitle"));
            String title = ChatColor.translateAlternateColorCodes('&', section.getString("title.main-title"));
            MessageTitle message = new MessageTitle(
                    title, subTitle,
                    section.getInt("title.fade-in"),
                    section.getInt("title.fade-stay"),
                    section.getInt("title.fade-out"));
            messageLoader.addMessageCache(idMessage + "_title", message);
        }
    }

    private boolean checkIfOptionIsActive(String typeMessage, ConfigurationSection section) {
        return section.getBoolean(typeMessage);
    }
}
