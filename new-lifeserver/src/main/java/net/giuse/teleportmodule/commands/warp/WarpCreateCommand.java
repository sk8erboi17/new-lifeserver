package net.giuse.teleportmodule.commands.warp;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.submodule.subservice.WarpService;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpCreateCommand extends AbstractCommand {
    private final WarpService warpService;
    private final MessageBuilder messageBuilder;

    @Inject
    public WarpCreateCommand(WarpService warpService, MessageBuilder messageBuilder) {
        super("warpcreate", "lifeserver.warpcreate");
        this.warpService = warpService;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;

        // Check if name length is 0
        if (args.length == 0) {
            messageBuilder.setCommandSender(player).setIDMessage("warp-insert-name").sendMessage();
            return;
        }

        String warpName = args[0].toLowerCase();
        Location location = player.getLocation();
        String locationString = serializeLocation(location);

        // Check if warp exists
        if (warpService.getWarp(warpName) != null) {
            messageBuilder.setCommandSender(player).setIDMessage("warp-already-exists").sendMessage();
            return;
        }

        // Create warp
        warpService.addWarp(warpName, locationString);
        messageBuilder.setCommandSender(player).setIDMessage("warp-created")
                .sendMessage(new TextReplacer().match("%name%").replaceWith(warpName));
    }

    private String serializeLocation(Location location) {
        return location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }
}
