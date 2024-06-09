package net.giuse.kitmodule.gui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import net.giuse.api.inventorylib.ButtonBuilder;
import net.giuse.api.inventorylib.InventoryBuilder;
import net.giuse.api.inventorylib.ItemstackBuilder;
import net.giuse.api.inventorylib.gui.ItemInitializer;
import net.giuse.kitmodule.KitModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.inject.Inject;


/**
 * Initialize Items in kit gui
 */

public class KitItemsGuiInit implements ItemInitializer {
    @Inject
    private KitModule kitModule;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        ConfigurationSection configurationSection = kitModule.getFileKits().getKitYaml().getConfigurationSection("inventory.items");
        for (String string : configurationSection.getKeys(false)) {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (!string.equalsIgnoreCase("previouspage") && !string.equalsIgnoreCase("nextpage")) {

                //Create ItemStackBuilder
                ItemstackBuilder itemstackBuilder = new ItemstackBuilder(XMaterial.matchXMaterial(itemsConfig.getString("material").toUpperCase()).get().parseMaterial(), itemsConfig.getInt("amount"));
                itemstackBuilder.setData((short) itemsConfig.getInt("data"));
                itemstackBuilder.setName(itemsConfig.getString("display-name"));

                //Check there are enchantments from section
                if (itemsConfig.getString("enchant") != null) {
                    itemstackBuilder.setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                            XEnchantment.matchXEnchantment(itemsConfig.getString("enchant").split(":")[0]).get().getEnchant());
                }

                //Check there are lores from section
                if (!itemsConfig.getStringList("lore").isEmpty()) {
                    itemstackBuilder.setLores(itemsConfig.getStringList("lore"));
                }

                //Create a button
                ButtonBuilder button = new ButtonBuilder(
                        inventoryBuilder,
                        itemsConfig.getInt("position"),
                        itemsConfig.getInt("page"),
                        itemstackBuilder.toItem(),
                        false, false, true);

                //Set Event of the button
                button.setEvent(inventoryClickEvent -> {
                    if (itemsConfig.getString("givekit") != null) {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        player.performCommand("kit " + itemsConfig.getString("givekit"));
                        inventoryClickEvent.getWhoClicked().closeInventory();
                    }
                });
                inventoryBuilder.addButton(button);
            }
        }
    }
}
