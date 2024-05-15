package net.giuse.simplycommandmodule.commands;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WorkBenchCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public WorkBenchCommand(MessageBuilder messageBuilder) {
        super("workbench", "lifeserver.workbench");
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.openWorkbench(null, true);
            return;
        }

        messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();

    }
}
