package net.giuse.economymodule.commands;


import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.EconomyModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class PayCommand extends AbstractCommand {
    private final EconomyModule economyModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public PayCommand(EconomyModule economyModule, MessageBuilder messageBuilder) {
        super("pay", "lifeserver.pay");
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
        if (args.length <= 1) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-pay-args").sendMessage();
            return;
        }
        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-number").sendMessage();
            return;
        }

        if (Double.parseDouble(args[1]) < 0.0) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-number").sendMessage();
            return;
        }

        if (!this.economyModule.getEconPlayerIsPresent(Bukkit.getPlayer(args[0]).getUniqueId())) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-neverJoin").sendMessage();
            return;
        }

        if (this.economyModule.getCustomEcoManager().getBalance(p) >= Double.parseDouble(args[1])) {
            this.economyModule.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[0]), Double.parseDouble(args[1]));
            this.economyModule.getCustomEcoManager().withdrawPlayer(p, Double.parseDouble(args[1]));

            messageBuilder.setCommandSender(p).setIDMessage("economy-pay-send").sendMessage(
                    new TextReplacer().match("%player%").replaceWith(args[0]),
                    new TextReplacer().match("%amount%").replaceWith(args[1]));

            messageBuilder.setCommandSender(Bukkit.getOfflinePlayer(args[0]).getPlayer()).setIDMessage("economy-pay-receive").sendMessage(
                    new TextReplacer().match("%player%").replaceWith(p.getName()),
                    new TextReplacer().match("%amount%").replaceWith(args[1]));

            return;
        }
        messageBuilder.setCommandSender(p).setIDMessage("economy-no-money").sendMessage();

    }
}
