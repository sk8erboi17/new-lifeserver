package net.giuse.teleportmodule.commands.warp;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.gui.WarpGui;
import net.giuse.teleportmodule.submodule.dto.Warp;
import net.giuse.teleportmodule.submodule.subservice.WarpService;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class WarpCommand extends AbstractCommand {
    private final WarpService warpService;
    private final MessageBuilder messageBuilder;
    private final WarpGui warpGui;
    private final TeleportModule teleportModule;
    private final FileConfiguration mainConfig;

    @Inject
    public WarpCommand(WarpService warpService,
                       FileConfiguration mainConfig,
                       MessageBuilder messageBuilder,
                       WarpGui warpGui,
                       TeleportModule teleportModule) {
        super("warp", "lifeserver.warp.list");
        this.warpService = warpService;
        this.messageBuilder = messageBuilder;
        this.warpGui = warpGui;
        this.teleportModule = teleportModule;
        this.mainConfig = mainConfig;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;

        // Show warp GUI or warp list
        if (args.length == 0) {
            // Check if GUI is active
            if (mainConfig.getBoolean("use-warp-gui")) {
                warpGui.openInv(player);
                return;
            }

            // Get all warps
            List<Warp> warps = warpService.getAllWarps();

            // Check if warp list is empty
            if (warps.isEmpty()) {
                messageBuilder.setCommandSender(player).setIDMessage("no-warp-available").sendMessage();
                return;
            }

            // Send Warp List
            String warpList = warps.stream()
                    .map(Warp::getName)
                    .collect(Collectors.joining(", "));
            messageBuilder.setCommandSender(player).setIDMessage("warp-list")
                    .sendMessage(new TextReplacer().match("%list%").replaceWith(warpList));
            return;
        }

        String warpName = args[0].toLowerCase();

        // Check if Player has permission for a warp
        if (!player.hasPermission("lifeserver.warp." + warpName)) {
            commandSender.sendMessage("No perms");
            return;
        }

        // Check if warp exists
        Warp warp = warpService.getWarp(warpName);
        if (warp == null) {
            messageBuilder.setCommandSender(player).setIDMessage("warp-no-exists")
                    .sendMessage(new TextReplacer().match("%name%").replaceWith(warpName));
            return;
        }

        // Teleport to a Warp
        teleportModule.getBackLocations().put(player, player.getLocation());
        Location warpLocation = warp.getLocation();
        PaperLib.teleportAsync(player, warpLocation);
        messageBuilder.setCommandSender(player).setIDMessage("warp-teleport")
                .sendMessage(new TextReplacer().match("%name%").replaceWith(warpName));
    }
}
