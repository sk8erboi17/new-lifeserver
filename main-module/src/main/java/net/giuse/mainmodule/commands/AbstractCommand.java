package net.giuse.mainmodule.commands;

import net.giuse.api.ezmessage.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;


/**
 * AbstractCommand for create Concrete Commands
 */

public abstract class AbstractCommand extends Command {
    private final String permission;
    @Inject
    private MessageBuilder messageBuilder;

    public AbstractCommand(String name, String permission) {
        super(name);
        this.permission = permission;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission(this.permission)) {
            messageBuilder.setCommandSender(sender).setIDMessage("no-perms").sendMessage();
            return true;
        }
        execute(sender, args);
        return true;
    }

    public abstract void execute(CommandSender commandSender, String[] args);


}