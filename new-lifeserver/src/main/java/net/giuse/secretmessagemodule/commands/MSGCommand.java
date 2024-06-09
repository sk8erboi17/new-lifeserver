package net.giuse.secretmessagemodule.commands;


import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.secretmessagemodule.process.SecretChatProcess;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;


public class MSGCommand extends AbstractCommand {
    private final SecretChatProcess secretChatProcess;
    private final MessageBuilder messageBuilder;

    @Inject
    public MSGCommand(MessageBuilder messageBuilder, SecretChatProcess secretChatProcess) {
        super("msg",
                "Send a secret message",
                "/msg <player> <message>",
                new ArrayList<>(Collections.singleton("tell")),
                "lifeserver.msg");
        this.secretChatProcess = secretChatProcess;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player sender = (Player) commandSender;

        //Send usage of command
        if (args.length == 0) {
            messageBuilder.setCommandSender(sender).setIDMessage("usage").sendMessage();
            return;
        }

        //Check if text-length 0
        if (args.length == 1) {
            messageBuilder.setCommandSender(sender).setIDMessage("insert-text").sendMessage();
            return;
        }

        //Check if player is offline
        if (Bukkit.getPlayer(args[0]) == null) {
            messageBuilder.setCommandSender(sender).setIDMessage("player-offline").sendMessage();
            return;
        }

        Player receiver = Bukkit.getPlayer(args[0]);
        StringBuilder sb = new StringBuilder();

        //Build Message
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        //Send Message
        secretChatProcess.setSender(sender);
        secretChatProcess.setReceiver(receiver);
        secretChatProcess.setText(sb.toString());
        secretChatProcess.send();
    }
}
