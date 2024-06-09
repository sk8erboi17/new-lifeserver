package net.giuse.simplycommandmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class FlyCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public FlyCommand(MessageBuilder messageBuilder) {
        super("fly", "lifeserver.fly");
        this.messageBuilder = messageBuilder;
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                if (player.isFlying()) {
                    messageBuilder.setCommandSender(commandSender).setIDMessage("fly-disabled").sendMessage();
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    return;
                }

                messageBuilder.setCommandSender(commandSender).setIDMessage("fly-enable").sendMessage();
                player.setAllowFlight(true);
                player.setFlying(true);
                return;
            }
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
        }

        if (!commandSender.hasPermission("lifeserver.fly.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }

        if (target.isFlying()) {
            target.setAllowFlight(false);
            target.setFlying(false);
            messageBuilder.setCommandSender(target).setIDMessage("fly-disabled").sendMessage();
            messageBuilder.setCommandSender(commandSender).setIDMessage("fly-disabled-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));

            return;
        }

        target.setAllowFlight(true);
        target.setFlying(true);
        messageBuilder.setCommandSender(target).setIDMessage("fly-disabled").sendMessage();
        messageBuilder.setCommandSender(commandSender).setIDMessage("fly-enable-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));

    }

}
