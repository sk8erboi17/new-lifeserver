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
                teleportModule.getTeleportFileManager().getMessagesTeleportYaml().getConfigurationSection("messages"),
                teleportModule.getTeleportFileManager().getMessagesWarpYaml().getConfigurationSection("messages"),
                teleportModule.getTeleportFileManager().getMessagesHomeYaml().getConfigurationSection("messages"),
                teleportModule.getTeleportFileManager().getMessagesSpawnYaml().getConfigurationSection("messages")};

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
