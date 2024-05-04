package net.giuse.simplycommandmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HealCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public HealCommand(MainModule mainModule) {
        super("heal", "lifeserver.heal");
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
                return;
            }
            Player player = (Player) commandSender;
            player.setHealth(20);
            messageBuilder.setCommandSender(commandSender).setIDMessage("heal").sendMessage();
            return;
        }

        if (!commandSender.hasPermission("lifeserver.heal.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }
        messageBuilder.setCommandSender(target).setIDMessage("heal").sendMessage();
        messageBuilder.setCommandSender(commandSender).setIDMessage("heal-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
        target.setHealth(20);
    }

}
