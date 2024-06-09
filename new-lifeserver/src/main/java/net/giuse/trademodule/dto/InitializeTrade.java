package net.giuse.trademodule.dto;

import lombok.Data;
import net.giuse.trademodule.gui.TradeGui;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Data
public class InitializeTrade {

    private final TradeGui tradeGui;
    private Player requestTrade;
    private Player targetTrade;

    @Inject
    public InitializeTrade(TradeGui tradeGui) {
        this.tradeGui = tradeGui;
    }


}
