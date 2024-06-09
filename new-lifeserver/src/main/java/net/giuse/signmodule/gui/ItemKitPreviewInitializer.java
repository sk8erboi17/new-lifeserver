package net.giuse.signmodule.gui;

import lombok.Setter;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.api.inventorylib.gui.ItemInitializer;
import net.giuse.kitmodule.dto.Kit;
import net.giuse.kitmodule.service.KitService;
import org.bukkit.inventory.Inventory;

import javax.inject.Inject;

public class ItemKitPreviewInitializer implements ItemInitializer {

    @Setter
    private String kitName;

    @Inject
    private KitService kitService;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        Kit kit = kitService.getKit(kitName);
        Inventory inventory = inventoryBuilder.getPageMap().get(1);
        inventory.addItem(kit.getItemStackList());

    }
}
