package net.giuse.economymodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.EconomyModule;
import net.giuse.economymodule.service.EconomyService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
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

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ITALY);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;

        decimalFormat.applyPattern("#,###.##");
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage("§aUse /money <player>");
                return;
            }
            final Player player = (Player) commandSender;

            BigDecimal balance = economyService.getBalance(player.getUniqueId());
            String formattedBalance = decimalFormat.format(balance) + EconomyModule.MONEY_SYMBOL;

            messageBuilder.setCommandSender(player).setIDMessage("economy-balance").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(formattedBalance));
            return;
        }
        if (!commandSender.hasPermission("lifeserver.balance.other")) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("no-perms").sendMessage();
            return;
        }

        UUID targetUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        if (economyService.playerExists(targetUUID)) {
            BigDecimal balance = economyService.getBalance(targetUUID);
            String formattedBalance = decimalFormat.format(balance) + EconomyModule.MONEY_SYMBOL;

            messageBuilder.setCommandSender(commandSender)
                    .setIDMessage("economy-balance-other")
                    .sendMessage(
                            new TextReplacer().match("%money%").replaceWith(formattedBalance),
                            new TextReplacer().match("%player%").replaceWith(args[0])
                    );
            return;
        }

        messageBuilder.setCommandSender(commandSender).setIDMessage("economy-neverJoin").sendMessage();
    }
}


