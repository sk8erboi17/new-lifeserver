package net.giuse.api.inventorylib;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.giuse.api.inventorylib.inventoryevents.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class ButtonBuilder implements Listener {
    private final InventoryBuilder inventoryBuilder;

    private final int position, page;

    private final ItemStack itemStack;
    private final boolean nextPage, previousPage, eventCancelled;

    @Setter
    private ClickEvent event;

    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getSlot() == position && inventoryBuilder.getPageMap().get(page).equals(inventoryClickEvent.getInventory())) {
            if (nextPage && inventoryBuilder.getTotalPages() > 1)
                inventoryBuilder.nextPage((Player) inventoryClickEvent.getWhoClicked());
            if (previousPage && inventoryBuilder.getTotalPages() > 1)
                inventoryBuilder.previousPage((Player) inventoryClickEvent.getWhoClicked());
            if (eventCancelled) inventoryClickEvent.setCancelled(true);
            if (event != null) {
                event.click(inventoryClickEvent);
            }
        }
    }
}
