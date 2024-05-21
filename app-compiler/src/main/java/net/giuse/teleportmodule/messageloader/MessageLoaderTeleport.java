package net.giuse.teleportmodule.messageloader;

import net.giuse.mainmodule.message.SetupMessageLoader;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderTeleport extends SetupMessageLoader {
    @Inject
    private TeleportModule teleportModule;

    @Override
    public void load() {

        ConfigurationSection[] generalMessageSectionArray = new ConfigurationSection[]{
                teleportModule.getFileManager().getMessagesTeleportYaml().getConfigurationSection("messages"),
                teleportModule.getFileManager().getMessagesWarpYaml().getConfigurationSection("messages"),
                teleportModule.getFileManager().getMessagesHomeYaml().getConfigurationSection("messages"),
                teleportModule.getFileManager().getMessagesSpawnYaml().getConfigurationSection("messages")};

        for (ConfigurationSection generalMessageSection : generalMessageSectionArray) {
            for (String idMessage : generalMessageSection.getKeys(false)) {
                ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

                //setup
                setupMessageChat(idMessage, messageSection);
                setupTitle(idMessage, messageSection);
                setupActionBar(idMessage, messageSection);

            }
        }
    }
}
