package net.giuse.teleportmodule.submodule.warp.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.submodule.warp.dto.Warp;
import net.giuse.teleportmodule.submodule.warp.service.WarpService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class WarpListCommand extends AbstractCommand {
    private final WarpService warpService;
    private final MessageBuilder messageBuilder;

    @Inject
    public WarpListCommand(WarpService warpService, MessageBuilder messageBuilder) {
        super("warplist", "lifeserver.warp.list");
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

        // Get all warps
        List<Warp> warps = warpService.getAllWarps();

        // Check if there are warp
        if (warps.isEmpty()) {
            messageBuilder.setCommandSender(player).setIDMessage("no-warp-available").sendMessage();
            return;
        }

        // Send player warp Message
        String warpList = warps.stream()
                .map(Warp::getName)
                .collect(Collectors.joining(", "));
        messageBuilder.setCommandSender(player).setIDMessage("warp-list")
                .sendMessage(new TextReplacer().match("%list%").replaceWith(warpList));
    }
}
