package net.giuse.teleportmodule.commands.warp;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.submodule.WarpLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpListCommand extends AbstractCommand {
    private final WarpLoaderModule warpLoaderModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public WarpListCommand(WarpLoaderModule warpLoaderModule, MessageBuilder messageBuilder) {
        super("warplist", "lifeserver.warp.list");
        this.warpLoaderModule = warpLoaderModule;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) commandSender;

        //Check if there are warp
        if (warpLoaderModule.getWarps().isEmpty()) {
            messageBuilder.setCommandSender(p).setIDMessage("no-warp-available").sendMessage();
            return;
        }

        //Send player warp Message
        StringBuilder sb = new StringBuilder();
        warpLoaderModule.getWarps().forEach((name, location) -> sb.append(name).append(","));
        messageBuilder.setCommandSender(p).setIDMessage("warp-list").sendMessage(new TextReplacer().match("%list%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
    }
}
