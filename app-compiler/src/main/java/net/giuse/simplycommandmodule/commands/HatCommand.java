package net.giuse.simplycommandmodule.commands;


import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HatCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public HatCommand(MessageBuilder messageBuilder) {
        super("hat", "lifeserver.hat");
        this.messageBuilder = messageBuilder;
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }

        Player player = (Player) commandSender;
        if (player.getInventory().getItemInHand() != null) {
            player.getInventory().setHelmet(player.getInventory().getItemInHand());
            player.getInventory().setItemInHand(null);
        }
    }
}
