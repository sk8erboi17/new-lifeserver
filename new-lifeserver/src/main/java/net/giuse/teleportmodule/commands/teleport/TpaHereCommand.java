package net.giuse.teleportmodule.commands.teleport;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.enums.TpType;
import net.giuse.teleportmodule.submodule.TeleportRequestModule;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpaHereCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final TeleportRequestModule teleportRequestModule;

    @Inject
    public TpaHereCommand(MessageBuilder messageBuilder, TeleportRequestModule teleportRequestModule) {
        super("tpahere", "lifeserver.tpahere");
        this.messageBuilder = messageBuilder;
        this.teleportRequestModule = teleportRequestModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported");
            return;
        }
        Player sender = (Player) commandSender;

        //Check if player isn't selected
        if (args.length == 0) {
            messageBuilder.setCommandSender(sender).setIDMessage("select-player").sendMessage();

            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        //Check if target is online
        if (target == null) {
            messageBuilder.setCommandSender(sender).setIDMessage("player-not-found").sendMessage();
            return;
        }

        //Send request to the target
        messageBuilder.setCommandSender(sender).setIDMessage("tpahere-request-sender").sendMessage(new TextReplacer().match("%playername%").replaceWith(target.getName()));
        messageBuilder.setCommandSender(target).setIDMessage("tpahere-request-receiver").sendMessage(new TextReplacer().match("%playername%").replaceWith(sender.getName()));
        teleportRequestModule.getPendingRequests().add(new PendingRequest(sender, target, TpType.TPA_HERE));
    }
}
