package net.giuse.teleportmodule.submodule.teleportrequest.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.submodule.teleportrequest.TeleportRequestModule;
import net.giuse.teleportmodule.submodule.teleportrequest.dto.PendingRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpDenyCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    private final TeleportRequestModule teleportRequestModule;

    @Inject
    public TpDenyCommand(MessageBuilder messageBuilder, TeleportRequestModule teleportRequestModule) {
        super("tpdeny", "lifeserver.tpdeny");
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

        //Check if are Pending Requests
        if (teleportRequestModule.getPending(sender.getUniqueId()) == null) {
            messageBuilder.setCommandSender(sender).setIDMessage("no-pending-request").sendMessage();
            return;
        }

        //Deny Pending Requests
        PendingRequest pendingRequest = teleportRequestModule.getPending(sender.getUniqueId());
        messageBuilder.setCommandSender(pendingRequest.getSender()).setIDMessage("request-refused").sendMessage(new TextReplacer().match("%playername%").replaceWith(pendingRequest.getReceiver().getName()));
        teleportRequestModule.getPendingRequests().remove(pendingRequest);

    }
}
