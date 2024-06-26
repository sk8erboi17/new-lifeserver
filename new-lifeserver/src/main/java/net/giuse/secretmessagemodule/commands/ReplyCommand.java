package net.giuse.secretmessagemodule.commands;


import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.secretmessagemodule.SecretChatBuilder;
import net.giuse.secretmessagemodule.SecretMessageModule;
import net.giuse.secretmessagemodule.process.SecretChatProcess;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class ReplyCommand extends AbstractCommand {

    private final SecretMessageModule secretMessageModule;

    private final SecretChatProcess secretChatProcess;

    private final MessageBuilder messageBuilder;

    @Inject
    public ReplyCommand(SecretMessageModule secretMessageModule, SecretChatProcess secretChatProcess, MessageBuilder messageBuilder) {
        super("reply", "lifeserver.reply");
        this.secretMessageModule = secretMessageModule;
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
        StringBuilder sb = new StringBuilder();
        SecretChatBuilder secret;

        //Check if VARCHAR(255) lenght is 0
        if (args.length == 0) {
            messageBuilder.setCommandSender(sender).setIDMessage("insert-text").sendMessage();
            return;
        }

        //Check if exists receiver
        if (secretMessageModule.getReceiverSecretChat(sender.getUniqueId()) == null && secretMessageModule.getSenderSecretChat(sender.getUniqueId()) == null) {
            messageBuilder.setCommandSender(sender).setIDMessage("nobody-reply").sendMessage();
            return;
        }

        //Search between sender a player to reply
        if (secretMessageModule.getReceiverSecretChat(sender.getUniqueId()) == null) {
            secret = secretMessageModule.getSenderSecretChat(sender.getUniqueId());

            //Check if Receiver isn't Online
            if (!secret.getReceiver().isOnline()) {
                messageBuilder.setCommandSender(sender).setIDMessage("nobody-reply").sendMessage();
                return;
            }

            //Build Message
            for (String arg : args) {
                sb.append(arg).append(" ");
            }

            //Set Sender and Receiver
            secretChatProcess.setSender(sender);
            secretChatProcess.setReceiver(secret.getReceiver());

            //Send Message
            secretChatProcess.setText(sb.toString());
            secretChatProcess.send();
            return;
        }

        //Check if Receiver isn't Online
        secret = secretMessageModule.getReceiverSecretChat(sender.getUniqueId());
        if (!secret.getSender().isOnline()) {
            messageBuilder.setCommandSender(sender).setIDMessage("nobody-reply").sendMessage();
            return;
        }

        //Build Message
        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        //Set Sender and Receiver
        secretChatProcess.setSender(sender);
        secretChatProcess.setReceiver(secret.getSender());

        //Send Message
        secretChatProcess.setText(sb.toString());
        secretChatProcess.send();
    }
}

