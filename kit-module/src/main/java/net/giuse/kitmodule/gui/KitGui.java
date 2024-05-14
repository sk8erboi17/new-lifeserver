package net.giuse.kitmodule.gui;


import ch.jalu.injector.Injector;
import lombok.Getter;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.gui.GuiInitializer;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Initialize Kit Gui
 */

public class KitGui implements GuiInitializer {
    @Getter
    private InventoryBuilder inventoryBuilder;
    @Inject
    private MainModule mainModule;
    @Inject
    private Injector injector;

    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {
        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);

        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = new InventoryBuilder(
                mainModule,
                kitModule.getFileKits().getKitYaml().getInt("inventory.rows"),
                kitModule.getFileKits().getKitYaml().getString("inventory.title"),
                kitModule.getFileKits().getKitYaml().getInt("inventory.page")).createInvs();

        //Initialize items
        injector.getSingleton(NextItemGuiInit.class).initItems(inventoryBuilder);
        injector.getSingleton(PreviousItemGuiInit.class).initItems(inventoryBuilder);
        injector.getSingleton(ItemsGuiInit.class).initItems(inventoryBuilder);

        //Build InventoryBuilder
        inventoryBuilder.build();
        this.inventoryBuilder = inventoryBuilder;
    }

    /*
     * Open Inventory to a Player
     */
    public void openInv(Player player) {
        player.openInventory(inventoryBuilder.getInventoryHash().get(1));
    }
}
