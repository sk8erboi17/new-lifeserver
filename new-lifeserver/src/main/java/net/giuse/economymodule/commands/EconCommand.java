package net.giuse.economymodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.EconomyModule;
import net.giuse.economymodule.service.EconomyService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.UUID;
//TODO ADD CHECK IF MAX MONEY REACHED
public class EconCommand extends AbstractCommand {
    private final EconomyService economyService;
    private final MessageBuilder messageBuilder;

    @Inject
    public EconCommand(EconomyService economyService, MessageBuilder messageBuilder) {
        super("eco", "lifeserver.eco");
        this.economyService = economyService;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {

        if (!commandSender.hasPermission("lifeserver.eco")) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("no-perms").sendMessage();
            return;
        }

        if (args.length <= 2) {
            commandSender.sendMessage("§a/eco give <player> <money>§7 - Give money to a Player");
            commandSender.sendMessage("§a/eco remove <player> <money>§7 - Remove money to a Player");
            commandSender.sendMessage("§a/eco set <player> <money>§7 - Set balance to a Player");
            return;
        }

        UUID playerUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();

        if (!this.economyService.playerExists(playerUUID)) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("economy-neverJoin").sendMessage();
            return;
        }

        if (!NumberUtils.isNumber(args[2])) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("economy-number").sendMessage();
            return;
        }

        BigDecimal amount = new BigDecimal(args[2]);

        if (args[0].equalsIgnoreCase("give")) {
            if (!commandSender.hasPermission("lifeserver.eco.give")) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("no-perms").sendMessage();
                return;
            }
            this.economyService.deposit(playerUUID, amount);
            sendBalanceMessages(commandSender, args[1], playerUUID, "economy-addMoney", "economy-addMoney-other", amount);
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!commandSender.hasPermission("lifeserver.eco.remove")) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("no-perms").sendMessage();
                return;
            }
            this.economyService.withdraw(playerUUID, amount);
            sendBalanceMessages(commandSender, args[1], playerUUID, "economy-removeMoney", "economy-removeMoney-other", amount);
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!commandSender.hasPermission("lifeserver.eco.set")) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("no-perms").sendMessage();
                return;
            }
            this.economyService.setBalance(playerUUID, amount);
            sendBalanceMessages(commandSender, args[1], playerUUID, "economy-setMoney", "economy-setMoney-other", amount);
        }
    }

    private void sendBalanceMessages(CommandSender sender, String playerName, UUID playerUUID, String playerMessageID, String senderMessageID, BigDecimal amount) {
        Player player = Bukkit.getPlayer(playerUUID);
        BigDecimal balance = this.economyService.getBalance(playerUUID);

        if (player != null) {
            messageBuilder.setCommandSender(player).setIDMessage(playerMessageID).sendMessage(
                    new TextReplacer().match("%money%").replaceWith(balance.toString() + EconomyModule.MONEY_SYMBOL),
                    new TextReplacer().match("%moneyadd%").replaceWith(amount.toString() + EconomyModule.MONEY_SYMBOL));
        }

        messageBuilder.setCommandSender(sender).setIDMessage(senderMessageID).sendMessage(
                new TextReplacer().match("%money%").replaceWith(balance.toString() + EconomyModule.MONEY_SYMBOL),
                new TextReplacer().match("%moneyadd%").replaceWith(amount.toString() + EconomyModule.MONEY_SYMBOL),
                new TextReplacer().match("%player%").replaceWith(playerName));
    }
}
