package net.giuse.economymodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.economymodule.EconomyModule;
import net.giuse.economymodule.service.EconomyService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TopBalanceCommand extends AbstractCommand {

    private final EconomyService economyService;

    private final MessageBuilder messageBuilder;

    private final FileConfiguration mainConfig;

    @Inject
    public TopBalanceCommand(EconomyService economyService, MessageBuilder messageBuilder, FileConfiguration mainConfig) {
        super("topbalance", "lifeserver.topbalance");
        this.economyService = economyService;
        this.messageBuilder = messageBuilder;
        this.mainConfig = mainConfig;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ITALY);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("#,###.##");

        if (!commandSender.hasPermission("lifeserver.topbalance")) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("no-perms").sendMessage();
            return;
        }

        Map<UUID, BigDecimal> topBalances = economyService.getTopBalances();
        StringBuilder topBalancesFormatted = new StringBuilder();

        int rank = 1;
        for (Map.Entry<UUID, BigDecimal> entry : topBalances.entrySet()) {
            String playerName = getPlayerName(entry.getKey());
            String formattedBalance = decimalFormat.format(entry.getValue()) + EconomyModule.MONEY_SYMBOL;
            topBalancesFormatted.append(rank).append(". ").append(playerName).append(": ").append(formattedBalance).append("%newline%");
            rank++;
        }

        messageBuilder.setCommandSender(commandSender).setIDMessage("economy-topBalance").sendMessage(
                new TextReplacer().match("%topbalances%").replaceWith(topBalancesFormatted.toString()),
                new TextReplacer().match("%top-number%").replaceWith(String.valueOf(mainConfig.getInt("top-balance-show")))
        );
    }

    private String getPlayerName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return player.getName();
        }
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
