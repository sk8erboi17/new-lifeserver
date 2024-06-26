package net.giuse.simplycommandmodule.commands;


import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;

public class RepairCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public RepairCommand(MessageBuilder messageBuilder) {
        super("repair", "lifeserver.repair");
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
            messageBuilder.setCommandSender(commandSender).setIDMessage("repair-usage").sendMessage();
            return;
        }

        if (args[0].equalsIgnoreCase("hand")) {
            if (!player.hasPermission("lifeserver.repair.hand")) {
                commandSender.sendMessage("No Perms");
                return;
            }
            if (player.getInventory().getItemInHand().getType() == Material.AIR) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("repair-nothing").sendMessage();
                return;
            }
            player.getInventory().getItemInHand().setDurability((short) 0);
            messageBuilder.setCommandSender(commandSender).setIDMessage("repair-hand").sendMessage();
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (!player.hasPermission("lifeserver.repair.all")) {
                commandSender.sendMessage("No Perms");
                return;
            }

            Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach(itemStack -> itemStack.setDurability((short) 0));
            messageBuilder.setCommandSender(commandSender).setIDMessage("repair-all").sendMessage();

        }
    }
}
