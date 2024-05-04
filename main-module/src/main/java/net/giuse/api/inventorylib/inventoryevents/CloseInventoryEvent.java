package net.giuse.api.inventorylib.inventoryevents;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CloseInventoryEvent {

    void close(InventoryCloseEvent e);
}
