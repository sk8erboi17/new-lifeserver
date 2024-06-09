package net.giuse.secretmessagemodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SocialSpyCommand extends AbstractCommand {

    private final SecretMessageModule secretMessageModule;

    private final MessageBuilder messageBuilder;

    @Inject
    public SocialSpyCommand(SecretMessageModule secretMessageModule, MessageBuilder messageBuilder) {
        super("socialspy", "lifeserver.socialspy");
        this.secretMessageModule = secretMessageModule;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player player = (Player) commandSender;

        //Enable or disable SocialSpy
        if (secretMessageModule.getPlayerSocialSpy().contains(player)) {
            messageBuilder.setCommandSender(player).setIDMessage("socialspy").sendMessage(new TextReplacer().match("%status%").replaceWith("§cOFF"));
            secretMessageModule.getPlayerSocialSpy().remove(player);
            return;
        }
        messageBuilder.setCommandSender(player).setIDMessage("socialspy").sendMessage(new TextReplacer().match("%status%").replaceWith("§aON"));
        secretMessageModule.getPlayerSocialSpy().add(player);

    }
}
