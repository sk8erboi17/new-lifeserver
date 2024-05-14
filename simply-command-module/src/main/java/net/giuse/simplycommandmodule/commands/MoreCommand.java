package net.giuse.simplycommandmodule.commands;


import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class MoreCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public MoreCommand(MessageBuilder messageBuilder) {
        super("more", "lifeserver.more");
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }
        Player player = (Player) commandSender;
        ItemStack newItem = player.getInventory().getItemInHand();
        newItem.setAmount(64);
        player.getInventory().setItemInHand(newItem);
    }
}
