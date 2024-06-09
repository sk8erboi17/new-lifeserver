package net.giuse.simplycommandmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

public class ClearInventoryCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public ClearInventoryCommand(MessageBuilder messageBuilder) {
        super("clearinventory",
                "Clear inventory",
                "/clearinventory [player]",
                new ArrayList<>(Collections.singleton("ci")),
                "lifeserver.clearinventory");
        this.messageBuilder = messageBuilder;
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
                return;
            }
            Player player = (Player) commandSender;
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            messageBuilder.setCommandSender(commandSender).setIDMessage("cleaninv").sendMessage();
            return;
        }

        if (!commandSender.hasPermission("lifeserver.clearinventory.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }


        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }

        target.getInventory().clear();
        target.getInventory().setArmorContents(null);
        messageBuilder.setCommandSender(commandSender).setIDMessage("cleaninv-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
        messageBuilder.setCommandSender(target).setIDMessage("cleaninv").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
    }
}



