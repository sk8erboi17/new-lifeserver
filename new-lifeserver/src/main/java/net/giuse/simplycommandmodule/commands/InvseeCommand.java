package net.giuse.simplycommandmodule.commands;


import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

//TODO Better Invsee, Offline Player Too
public class InvseeCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public InvseeCommand(MessageBuilder messageBuilder) {
        super("invsee", "lifeserver.invsee");
        this.messageBuilder = messageBuilder;
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }

        Player player = (Player) commandSender;
        if (args.length == 0) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("invsee-select-player").sendMessage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();
            return;
        }

        player.openInventory(target.getInventory());
    }

}
