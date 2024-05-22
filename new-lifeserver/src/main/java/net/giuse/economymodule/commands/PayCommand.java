package net.giuse.economymodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.EconomyModule;
import net.giuse.economymodule.service.EconomyService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.UUID;
//TODO ADD CHECK IF MAX MONEY REACHED
public class PayCommand extends AbstractCommand {
    private final EconomyService economyService;
    private final MessageBuilder messageBuilder;

    @Inject
    public PayCommand(EconomyService economyService, MessageBuilder messageBuilder) {
        super("pay", "lifeserver.pay");
        this.economyService = economyService;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        final Player sender = (Player) commandSender;

        if (args.length <= 1) {
            messageBuilder.setCommandSender(sender).setIDMessage("economy-pay-args").sendMessage();
            return;
        }

        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setCommandSender(sender).setIDMessage("economy-number").sendMessage();
            return;
        }

        BigDecimal amount = new BigDecimal(args[1]);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            messageBuilder.setCommandSender(sender).setIDMessage("economy-number").sendMessage();
            return;
        }

        UUID recipientUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        if (!economyService.playerExists(recipientUUID)) {
            messageBuilder.setCommandSender(sender).setIDMessage("economy-neverJoin").sendMessage();
            return;
        }

        BigDecimal senderBalance = economyService.getBalance(sender.getUniqueId());
        if (senderBalance.compareTo(amount) >= 0) {
            economyService.withdraw(sender.getUniqueId(), amount);
            economyService.deposit(recipientUUID, amount);

            messageBuilder.setCommandSender(sender).setIDMessage("economy-pay-send").sendMessage(
                    new TextReplacer().match("%player%").replaceWith(args[0]),
                    new TextReplacer().match("%amount%").replaceWith(amount + EconomyModule.MONEY_SYMBOL));

            Player recipient = Bukkit.getPlayer(recipientUUID);
            if (recipient != null) {
                messageBuilder.setCommandSender(recipient).setIDMessage("economy-pay-receive").sendMessage(
                        new TextReplacer().match("%player%").replaceWith(sender.getName()),
                        new TextReplacer().match("%amount%").replaceWith(amount + EconomyModule.MONEY_SYMBOL));
            }
            return;
        }

        messageBuilder.setCommandSender(sender).setIDMessage("economy-no-money").sendMessage();
    }
}
