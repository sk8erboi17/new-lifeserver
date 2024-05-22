package net.giuse.teleportmodule.submodule.teleportrequest.commands;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.submodule.teleportrequest.dto.enums.TpType;
import net.giuse.teleportmodule.submodule.teleportrequest.TeleportRequestModule;
import net.giuse.teleportmodule.submodule.teleportrequest.dto.PendingRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpacceptCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final TeleportRequestModule teleportRequestModule;
    private final TeleportModule teleportModule;

    @Inject
    public TpacceptCommand(MessageBuilder messageBuilder, TeleportModule teleportModule, TeleportRequestModule teleportRequestModule) {
        super("tpaccept", "lifeserver.tpaccept");
        this.messageBuilder = messageBuilder;
        this.teleportModule = teleportModule;
        this.teleportRequestModule = teleportRequestModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;


        //Check if there is any Pending Request
        if (teleportRequestModule.getPending(player.getUniqueId()) == null) {
            messageBuilder.setCommandSender(player).setIDMessage("no-pending-request").sendMessage();
            return;
        }

        //Check the type of PendingRequest
        PendingRequest pendingRequest = teleportRequestModule.getPending(player.getUniqueId());
        if (pendingRequest.getTpType().equals(TpType.TPA)) {
            teleportModule.getBackLocations().put(pendingRequest.getSender(), pendingRequest.getSender().getLocation());
            PaperLib.teleportAsync(pendingRequest.getSender(), pendingRequest.getReceiver().getLocation());
        } else {
            teleportModule.getBackLocations().put(pendingRequest.getReceiver(), pendingRequest.getReceiver().getLocation());
            PaperLib.teleportAsync(pendingRequest.getReceiver(), pendingRequest.getSender().getLocation());
        }
        //Accept Pending Request
        messageBuilder.setCommandSender(pendingRequest.getSender()).setIDMessage("teleport-player").sendMessage(new TextReplacer().match("%playername%").replaceWith(pendingRequest.getReceiver().getName()));
        messageBuilder.setCommandSender(player).setIDMessage("request-accept-receiver").sendMessage(new TextReplacer().match("%playername%").replaceWith(player.getName()));

        teleportRequestModule.getPendingRequests().remove(pendingRequest);

    }
}
