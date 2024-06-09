package net.giuse.trademodule.gui;

import ch.jalu.injector.Injector;
import lombok.Getter;
import net.giuse.api.inventorylib.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.inject.Inject;

public class TradeGui /* implements GuiInitializer */ {
    @Inject
    private Injector injector;

    @Getter
    private InventoryBuilder inventoryBuilder;

    public void initInv() {
        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = injector.newInstance(InventoryBuilder.class);

        inventoryBuilder = inventoryBuilder.toBuilder()
                .rows(6)
                .totalPages(1)
                .name("Test")
                .build();

        inventoryBuilder.createInventories();

        //Initialize items
        //TODO

        //Build InventoryBuilder
        inventoryBuilder.build();
        this.inventoryBuilder = inventoryBuilder;
    }


    /*
     * Open Inventory to a Player
     */
    public void openInv(Player player) {
        player.openInventory(getInventory());
    }

    public Inventory getInventory() {
        return inventoryBuilder.getPageMap().get(1);
    }

}
