package net.giuse.economymodule.messageloader;

import net.giuse.economymodule.EconomyModule;
import net.giuse.mainmodule.message.SetupMessageLoader;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderEconomy extends SetupMessageLoader {

    @Inject
    private EconomyModule economyModule;

    @Override
    public void load() {
        ConfigurationSection generalMessageSection = economyModule.getEconomyFileManager().getMessagesYaml().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }
}
