package net.giuse.teleportmodule.commands.warp;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.submodule.WarpLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpDeleteCommand extends AbstractCommand {
    private final WarpLoaderModule warpLoaderModule;

    private final MessageBuilder messageBuilder;

    @Inject
    public WarpDeleteCommand(WarpLoaderModule warpLoaderModule, MessageBuilder messageBuilder) {
        super("warpdelete", "lifeserver.warpdelete");
        this.messageBuilder = messageBuilder;
        this.warpLoaderModule = warpLoaderModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) commandSender;

        //Check if name length is 0
        if (args.length == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("warp-insert-name").sendMessage();
            return;
        }

        //Check if warp exists
        if (warpLoaderModule.getWarp(args[0]) == null) {
            messageBuilder.setCommandSender(p).setIDMessage("warp-no-exists").sendMessage();
            return;
        }

        //Delete Warp
        warpLoaderModule.getWarps().remove(args[0]);
        messageBuilder.setCommandSender(p).setIDMessage("warp-removed").sendMessage(new TextReplacer().match("%name%").replaceWith(args[0]));
    }

}
