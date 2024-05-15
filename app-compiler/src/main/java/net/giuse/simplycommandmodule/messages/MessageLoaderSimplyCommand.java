package net.giuse.simplycommandmodule.messages;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.SetupMessageLoader;
import net.giuse.simplycommandmodule.SimplyCommandModule;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderSimplyCommand extends SetupMessageLoader {

    @Inject
    private MainModule mainModule;

    public void load() {
        SimplyCommandModule simplyCommandModule = (SimplyCommandModule) mainModule.getService(SimplyCommandModule.class);
        ConfigurationSection generalMessageSection = simplyCommandModule.getFileManager().getMessageSimpleFileYml().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }
}
