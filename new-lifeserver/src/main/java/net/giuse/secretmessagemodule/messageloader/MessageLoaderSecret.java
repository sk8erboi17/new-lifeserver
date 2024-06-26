package net.giuse.secretmessagemodule.messageloader;

import net.giuse.mainmodule.message.SetupMessageLoader;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderSecret extends SetupMessageLoader {

    @Inject
    private SecretMessageModule secretMessageModule;

    @Override
    public void load() {
        ConfigurationSection generalMessageSection = secretMessageModule.getSecretMessageFileManager().getMessagesSecretChatYaml().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }
}
