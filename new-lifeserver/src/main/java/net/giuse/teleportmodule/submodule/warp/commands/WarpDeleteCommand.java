package net.giuse.teleportmodule.submodule.warp.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.submodule.warp.service.WarpService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpDeleteCommand extends AbstractCommand {
    private final WarpService warpService;
    private final MessageBuilder messageBuilder;

    @Inject
    public WarpDeleteCommand(WarpService warpService, MessageBuilder messageBuilder) {
        super("warpdelete", "lifeserver.warpdelete");
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

        // Check if warp exists
        if (warpService.getWarp(warpName) == null) {
            messageBuilder.setCommandSender(player).setIDMessage("warp-no-exists").sendMessage(new TextReplacer().match("%name%").replaceWith(warpName));
            return;
        }

        // Delete Warp
        warpService.removeWarp(warpName);
        messageBuilder.setCommandSender(player).setIDMessage("warp-removed")
                .sendMessage(new TextReplacer().match("%name%").replaceWith(warpName));
    }
}
