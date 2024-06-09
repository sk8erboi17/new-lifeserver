package net.giuse.mainmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.MessageLoader;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.modules.AbstractModule;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

public class ReloadCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    private final MessageLoader messageLoader;

    private final MainModule mainModule;

    @Inject
    public ReloadCommand(MessageBuilder messageBuilder, MessageLoader messageLoader, MainModule mainModule) {
        super("lifeserver", "lifeserver.reload");
        this.messageBuilder = messageBuilder;
        this.messageLoader = messageLoader;
        this.mainModule = mainModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("usage-reload").sendMessage();
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            messageLoader.clearCache();
            mainModule.reloadConfig();
            mainModule.reloadMessage();
            mainModule.getServices().forEach(AbstractModule::reload);
            messageBuilder.setCommandSender(commandSender).setIDMessage("reload").sendMessage();
            return;
        }
        messageBuilder.setCommandSender(commandSender).setIDMessage("usage-reload").sendMessage();

    }
}
