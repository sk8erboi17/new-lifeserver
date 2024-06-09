package net.giuse.kitmodule.gui;


import ch.jalu.injector.Injector;
import lombok.Getter;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.api.inventorylib.gui.GuiInitializer;
import net.giuse.kitmodule.KitModule;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Initialize Kit Gui
 */

public class KitGui implements GuiInitializer {

    @Getter
    private InventoryBuilder inventoryBuilder;
    @Inject
    private KitModule kitModule;
    @Inject
    private Injector injector;

    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {

        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = injector.newInstance(InventoryBuilder.class);

        inventoryBuilder = inventoryBuilder.toBuilder()
                .rows(kitModule.getFileKits().getKitYaml().getInt("inventory.rows"))
                .totalPages(kitModule.getFileKits().getKitYaml().getInt("inventory.page"))
                .name(kitModule.getFileKits().getKitYaml().getString("inventory.title"))
                .build();

        inventoryBuilder.createInventories();

        //Initialize items
        injector.getSingleton(KitNextItemGuiInit.class).initItems(inventoryBuilder);
        injector.getSingleton(KitPreviousItemGuiInit.class).initItems(inventoryBuilder);
        injector.getSingleton(KitItemsGuiInit.class).initItems(inventoryBuilder);

        //Build InventoryBuilder
        inventoryBuilder.build();
        this.inventoryBuilder = inventoryBuilder;
    }

    /*
     * Open Inventory to a Player
     */
    public void openInv(Player player) {
        player.openInventory(inventoryBuilder.getPageMap().get(1));
    }
}
