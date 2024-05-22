package net.giuse.teleportmodule.commands;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class BackCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final TeleportModule teleportModule;

    @Inject
    public BackCommand(TeleportModule teleportModule, MessageBuilder messageBuilder) {
        super("back", "lifeserver.back");
        this.teleportModule = teleportModule;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;

        //Check if there is any back location
        if (!teleportModule.getBackLocations().containsKey(player)) {
            messageBuilder.setCommandSender(player).setIDMessage("back-no-location").sendMessage();
            return;
        }

        //Teleport to back location
        messageBuilder.setCommandSender(player).setIDMessage("back").sendMessage();
        Location location = player.getLocation();
        PaperLib.teleportAsync(player, teleportModule.getBackLocations().get(player));
        teleportModule.getBackLocations().put(player, location);
    }
}
