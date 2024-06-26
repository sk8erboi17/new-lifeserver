package net.giuse.simplycommandmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class FoodCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public FoodCommand(MessageBuilder messageBuilder) {
        super("food", "lifeserver.food");
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
            player.setFoodLevel(20);
            messageBuilder.setCommandSender(commandSender).setIDMessage("food").sendMessage();
            return;
        }

        if (!commandSender.hasPermission("lifeserver.food.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }

        messageBuilder.setCommandSender(target).setIDMessage("food").sendMessage();
        messageBuilder.setCommandSender(commandSender).setIDMessage("food-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
        target.setFoodLevel(20);
    }
}
