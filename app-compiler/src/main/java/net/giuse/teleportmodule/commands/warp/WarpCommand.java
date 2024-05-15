package net.giuse.teleportmodule.commands.warp;

import io.papermc.lib.PaperLib;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.gui.WarpGui;
import net.giuse.teleportmodule.submodule.WarpLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpCommand extends AbstractCommand {
    private final WarpLoaderModule warpLoaderModule;
    private final MessageBuilder messageBuilder;
    private final WarpGui warpGui;
    private final TeleportModule teleportModule;
    private final FileConfiguration mainConfig;

    @Inject
    public WarpCommand(WarpLoaderModule warpLoaderModule,
                       FileConfiguration mainConfig,
                       MessageBuilder messageBuilder,
                       WarpGui warpGui,
                       TeleportModule teleportModule) {

        super("warp", "lifeserver.warp.list");
        this.warpLoaderModule = warpLoaderModule;
        this.messageBuilder = messageBuilder;
        this.warpGui = warpGui;
        this.teleportModule = teleportModule;
        this.mainConfig = mainConfig;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) commandSender;

        //Show warp gui or warp list
        if (args.length == 0) {

            //Check if gui is active
            if (mainConfig.getBoolean("use-warp-gui")) {
                warpGui.openInv(p);
                return;
            }

            //Check if warp list is empty
            if (warpLoaderModule.getWarps().isEmpty()) {
                messageBuilder.setCommandSender(p).setIDMessage("no-warp-available").sendMessage();

                return;
            }

            //Send Warp List
            StringBuilder sb = new StringBuilder();
            warpLoaderModule.getWarps().forEach((warpName, location) -> sb.append(warpName).append(","));
            messageBuilder.setCommandSender(p).setIDMessage("warp-list").sendMessage(new TextReplacer().match("%list%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
            return;
        }

        //Check if Player has permission for a warp
        if (!p.hasPermission("lifeserver.warp." + args[0])) {
            commandSender.sendMessage("No perms");
            return;
        }
        //Check if warp exists
        if (warpLoaderModule.getWarp(args[0]) == null) {
            messageBuilder.setCommandSender(p).setIDMessage("warp-no-exists").sendMessage(new TextReplacer().match("%name%").replaceWith(args[0]));
            return;
        }

        //Teleport to a Warp
        teleportModule.getBackLocations().put(p, p.getLocation());
        PaperLib.teleportAsync(p, warpLoaderModule.getWarp(args[0]));
        messageBuilder.setCommandSender(p).setIDMessage("warp-teleport").sendMessage(new TextReplacer().match("%name%").replaceWith(args[0]));


    }
}
