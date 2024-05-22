package net.giuse.simplycommandmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SudoCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public SudoCommand(MessageBuilder messageBuilder) {
        super("sudo", "lifeserver.sudo");
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("sudo-usage").sendMessage();
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }

        if (sb.toString().startsWith("chat:")) {
            target.chat(sb.toString().replace("chat:", ""));
            messageBuilder.setCommandSender(commandSender).setIDMessage("sudo-forced").sendMessage(
                    new TextReplacer().match("%command%").replaceWith(sb.toString()),
                    new TextReplacer().match("%player_name%").replaceWith(target.getName()));

            return;
        }

        target.performCommand(sb.toString());
        messageBuilder.setCommandSender(commandSender).setIDMessage("sudo-forced").sendMessage(
                new TextReplacer().match("%command%").replaceWith(sb.toString()),
                new TextReplacer().match("%player_name%").replaceWith(target.getName()));

    }
}
