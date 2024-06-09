package net.giuse.trademodule.events;

import net.giuse.trademodule.gui.TradeGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose /*implements Listener*/ {

    private TradeGui tradeGui;

    public InventoryClose(TradeGui tradeGui) {
        this.tradeGui = tradeGui;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {

    }
}
