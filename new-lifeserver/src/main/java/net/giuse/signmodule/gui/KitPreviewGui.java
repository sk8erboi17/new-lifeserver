package net.giuse.signmodule.gui;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.Setter;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.api.inventorylib.gui.GuiInitializer;
import net.giuse.signmodule.files.SignFileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class KitPreviewGui implements GuiInitializer, Listener {

    @Setter
    private String kitName;

    @Inject
    private SignFileManager fileManager;

    @Getter
    private InventoryBuilder inventoryBuilder;

    @Inject
    private Injector injector;

    @Override
    public void initInv() {
        if (kitName == null) return;
        InventoryBuilder inventoryBuilder = injector.newInstance(InventoryBuilder.class);

        inventoryBuilder = inventoryBuilder.toBuilder()
                .rows(4)
                .totalPages(1)
                .name(fileManager.getSignYaml().getString("preview-gui-title").replace("%kitname%", kitName))
                .build();

        inventoryBuilder.createInventories();
        ItemKitPreviewInitializer itemKitPreviewInitializer = injector.newInstance(ItemKitPreviewInitializer.class);
        itemKitPreviewInitializer.setKitName(kitName);
        itemKitPreviewInitializer.initItems(inventoryBuilder);

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
