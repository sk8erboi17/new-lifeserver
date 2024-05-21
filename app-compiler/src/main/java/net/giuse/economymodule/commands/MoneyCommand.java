package net.giuse.economymodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.service.EconomyService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.UUID;

public class MoneyCommand extends AbstractCommand {
    private final EconomyService economyService;
    private final MessageBuilder messageBuilder;

    @Inject
    public MoneyCommand(EconomyService economyService, MessageBuilder messageBuilder) {
        super("money", "lifeserver.money");
        this.economyService = economyService;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        final Player player = (Player) commandSender;

        if (args.length == 0) {
            BigDecimal balance = economyService.getBalance(player.getUniqueId());
            messageBuilder.setCommandSender(player).setIDMessage("economy-balance").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(balance.toString()));
            return;
        }
        if (!player.hasPermission("lifeserver.balance.other")) {
            messageBuilder.setCommandSender(player).setIDMessage("no-perms").sendMessage();
            return;
        }

        UUID targetUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        if (economyService.playerExists(targetUUID)) {
            BigDecimal balance = economyService.getBalance(targetUUID);
            messageBuilder.setCommandSender(player).setIDMessage("economy-balance-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(balance.toString()),
                    new TextReplacer().match("%player%").replaceWith(args[0]));
            return;
        }

        messageBuilder.setCommandSender(player).setIDMessage("economy-neverJoin").sendMessage();
    }
}


