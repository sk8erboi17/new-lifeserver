package net.giuse.trademodule.command;

import ch.jalu.injector.Injector;
import net.giuse.trademodule.dto.InitializeTrade;
import net.giuse.trademodule.gui.TradeGui;
import net.giuse.trademodule.service.TradeService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TradeCommand /*extends AbstractCommand*/ {

    private final Injector injector;
    private final TradeService tradeService;

    @Inject
    public TradeCommand(Injector injector, TradeService tradeService) {
        //super("trade", "lifeserver.trade");
        this.injector = injector;
        this.tradeService = tradeService;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            return;
        }
        Player player = (Player) commandSender;
        if (args.length == 0) {
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (player == null || !player.isOnline()) {
            return;
        }

        if (tradeService.getByPlayer(player.getUniqueId()) != null) {
            return;
        }
        if (tradeService.getByPlayer(targetPlayer.getUniqueId()) != null) {
            return;
        }

        openTrade(player, targetPlayer);

    }

    private void openTrade(Player request, Player target) {
        InitializeTrade initializeTrade = injector.newInstance(InitializeTrade.class);
        initializeTrade.setRequestTrade(request);
        initializeTrade.setTargetTrade(target);
        TradeGui tradeGui = initializeTrade.getTradeGui();
        tradeGui.initInv();
        tradeGui.openInv(request);
        tradeGui.openInv(target);
    }
}
