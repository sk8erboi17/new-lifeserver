package net.giuse.teleportmodule.gui;


import ch.jalu.injector.Injector;
import lombok.Getter;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.gui.GuiInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/*
 * Initialize Warp Gui
 */
public class WarpGui implements GuiInitializer {
    @Inject
    private MainModule mainModule;
    @Inject
    private Injector injector;
    @Getter
    private InventoryBuilder inventoryBuilder;


    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);

        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = new InventoryBuilder(
                mainModule,
                teleportModule.getFileManager().getWarpYaml().getInt("inventory.rows"),
                teleportModule.getFileManager().getWarpYaml().getString("inventory.title"),
                teleportModule.getFileManager().getWarpYaml().getInt("inventory.page")).createInvs();

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
        player.openInventory(inventoryBuilder.getInventoryHash().get(1));
    }
}