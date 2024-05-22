package net.giuse.simplycommandmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.simplycommandmodule.SimplyCommandModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class GodCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    private final SimplyCommandModule simplyCommandModule;

    @Inject
    public GodCommand(MessageBuilder messageBuilder, SimplyCommandModule simplyCommandModule) {
        super("god", "lifeserver.god");
        this.messageBuilder = messageBuilder;
        this.simplyCommandModule = simplyCommandModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
                return;
            }

            Player player = (Player) commandSender;
            if (simplyCommandModule.getStringsNameGods().contains(player.getName())) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("god-disabled").sendMessage();
                simplyCommandModule.getStringsNameGods().remove(player.getName());
                return;
            }

            messageBuilder.setCommandSender(commandSender).setIDMessage("god-enabled").sendMessage();
            simplyCommandModule.getStringsNameGods().add(player.getName());
            return;


        }

        if (!commandSender.hasPermission("lifeserver.god.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();
            return;
        }

        if (simplyCommandModule.getStringsNameGods().contains(target.getName())) {
            messageBuilder.setCommandSender(target).setIDMessage("god-disabled").sendMessage();
            messageBuilder.setCommandSender(commandSender).setIDMessage("god-disabled-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
            simplyCommandModule.getStringsNameGods().remove(target.getName());
            return;
        }

        messageBuilder.setCommandSender(target).setIDMessage("god-enabled").sendMessage();
        messageBuilder.setCommandSender(commandSender).setIDMessage("god-enabled-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
        simplyCommandModule.getStringsNameGods().add(target.getName());
    }


}
