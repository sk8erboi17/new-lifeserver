package net.giuse.teleportmodule.submodule.teleportrequest.commands;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpHereCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    private final TeleportModule teleportModule;

    @Inject
    public TpHereCommand(MessageBuilder messageBuilder, TeleportModule teleportModule) {
        super("tphere", "lifeserver.tphere");
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

        //Teleport to the target
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            //Check if Target is online
            if (target == null) {
                messageBuilder.setCommandSender(sender).setIDMessage("player-not-found").sendMessage();
                return;
            }

            //Teleport Target
            teleportModule.getBackLocations().put(target, target.getLocation());
            PaperLib.teleportAsync(target, sender.getLocation());
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

        //Teleport targets
        teleportModule.getBackLocations().put(sender, sender.getLocation());
        PaperLib.teleportAsync(secondTarget, firstTarget.getLocation());
        messageBuilder.setCommandSender(sender).setIDMessage("teleport-player").sendMessage(new TextReplacer().match("%playername%").replaceWith(sender.getName()));
    }
}
