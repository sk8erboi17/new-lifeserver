package net.giuse.teleportmodule.submodule.warp.gui;


import ch.jalu.injector.Injector;
import lombok.Getter;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.api.inventorylib.gui.GuiInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/*
 * Initialize Warp Gui
 */
public class WarpGui implements GuiInitializer {
    @Inject
    private TeleportModule teleportModule;
    @Inject
    private Injector injector;
    @Getter
    private InventoryBuilder inventoryBuilder;


    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {

        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = injector.newInstance(InventoryBuilder.class);

        inventoryBuilder = inventoryBuilder.toBuilder()
                .rows(teleportModule.getTeleportFileManager().getWarpYaml().getInt("inventory.rows"))
                .totalPages(teleportModule.getTeleportFileManager().getWarpYaml().getInt("inventory.page"))
                .name(teleportModule.getTeleportFileManager().getWarpYaml().getString("inventory.title")).build();
        inventoryBuilder.createInventories();

        //Initialize items
        injector.getSingleton(NextItemGuiWarpInit.class).initItems(inventoryBuilder);
        injector.getSingleton(PreviousInitWarpGuiInit.class).initItems(inventoryBuilder);
        injector.getSingleton(ItemsGuiWarpInit.class).initItems(inventoryBuilder);

        //Build InventoryBuilder
        inventoryBuilder.build();
        this.inventoryBuilder = inventoryBuilder;
    }

    /*
     * Open Inventory to a Player
     */
    public void openInv(Player player) {
        player.openInventory(inventoryBuilder.getInventoryMap().get(1));
    }
}