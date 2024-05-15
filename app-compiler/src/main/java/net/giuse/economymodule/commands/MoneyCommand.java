package net.giuse.economymodule.commands;


import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.EconomyModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class MoneyCommand extends AbstractCommand {
    private final EconomyModule economyModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public MoneyCommand(EconomyModule economyModule, MessageBuilder messageBuilder) {
        super("money", "lifeserver.money");
        this.economyModule = economyModule;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        final Player p = (Player) commandSender;
        if (args.length == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-balance").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(economyModule.getBalancePlayer(p.getUniqueId()))));
            return;
        }

        if (!p.hasPermission("lifeserver.balance.other")) {
            messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
        }

        if (economyModule.getEconPlayerIsPresent(Bukkit.getOfflinePlayer(args[0]).getUniqueId())) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-balance-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(economyModule.getBalancePlayer(p.getUniqueId()))),
                    new TextReplacer().match("%player%").replaceWith(args[0]));
            return;
        }
        messageBuilder.setCommandSender(p).setIDMessage("economy-neverJoin").sendMessage();
    }
}
