package net.giuse.kitmodule.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Data
public class Kit {

    private final String name;

    private final int coolDown;

    private final String elementsKitBase64;

    private ItemStack[] itemStackList;

    public void build() {
        itemStackList = Utils.itemStackArrayFromBase64(elementsKitBase64);
    }

    @SneakyThrows
    public void giveItems(Player player) {
        for (ItemStack item : itemStackList) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }
    }

}