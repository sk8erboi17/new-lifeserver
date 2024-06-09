package net.giuse.teleportmodule.submodule.teleportrequest.commands;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.TeleportModule;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    private final TeleportModule teleportModule;

    @Inject
    public TpCommand(MessageBuilder messageBuilder, TeleportModule teleportModule) {
        super("tp", "lifeserver.tp");
        this.messageBuilder = messageBuilder;
        this.teleportModule = teleportModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Will be implemented");
            return;
        }
        Player sender = (Player) commandSender;


        //Check if player isn't selected
        if (args.length == 0) {
            messageBuilder.setCommandSender(sender).setIDMessage("select-player").sendMessage();
            return;
        }
        if (NumberUtils.isNumber(args[0])) {
            if (args.length < 3) {
                messageBuilder.setCommandSender(sender).setIDMessage("select-number").sendMessage();
                return;
            }
            messageBuilder.setCommandSender(sender).setIDMessage("teleport-player").sendMessage(new TextReplacer().match("%playername%").replaceWith(sender.getName()));
            PaperLib.teleportAsync(sender, new Location(sender.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])));
            return;
        }

        //Teleport to the target
        if (args.length == 1) {
            if (NumberUtils.isNumber(args[0])) {


                return;
            }
            Player target = Bukkit.getPlayer(args[0]);

            //Check if target is online
            if (target == null) {
                messageBuilder.setCommandSender(sender).setIDMessage("player-not-found").sendMessage();
                return;
            }

            //Teleport Target
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, target.getLocation());
            messageBuilder.setCommandSender(sender).setIDMessage("teleport-player").sendMessage(new TextReplacer().match("%playername%").replaceWith(sender.getName()));
            return;
        }

        //Teleport First Target to Second Target
        Player firstTarget = Bukkit.getPlayer(args[0]);
        Player secondTarget = Bukkit.getPlayer(args[1]);

        //Check if targets are online
        if (firstTarget == null || secondTarget == null) {
            messageBuilder.setCommandSender(sender).setIDMessage("player-not-found").sendMessage();
            return;
        }

        //Teleport Targets
        teleportModule.getBackLocations().put(sender, sender.getLocation());
        PaperLib.teleportAsync(firstTarget, secondTarget.getLocation());
        messageBuilder.setCommandSender(sender).setIDMessage("teleport-player").sendMessage(new TextReplacer().match("%playername%").replaceWith(sender.getName()));
    }
}
