package net.giuse.simplycommandmodule.commands.time;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SunCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public SunCommand(MessageBuilder messageBuilder) {
        super("sun", "lifeserver.sun");
        this.messageBuilder = messageBuilder;

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }
        Player player = (Player) commandSender;
        player.getWorld().setTime(24000L);
        player.getWorld().setStorm(false);
        player.getWorld().setThundering(false);
        messageBuilder.setCommandSender(commandSender).setIDMessage("time").sendMessage();

    }
}
