package net.giuse.api.inventorylib;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.giuse.mainmodule.MainModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.inject.Inject;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InventoryBuilder {

    @Inject
    private MainModule javaPlugin;

    @Getter
    private final Map<Integer, Inventory> pageMap = new HashMap<>();

    @Getter
    private final List<ButtonBuilder> buttonBuilders = new ArrayList<>();

    @Getter
    private int rows, totalPages;

    @Getter
    private String name;

    private final Map<UUID, Integer> pageCounter = new HashMap<>();

    public void createInventories() {
        for (int i = 1; i <= totalPages; i++) {
            pageMap.put(i, Bukkit.createInventory(null, 9 * rows, getFormattedPageName(i)));
        }
    }

    public void nextPage(Player player) {
        int currentPage = pageCounter.getOrDefault(player.getUniqueId(), 1);
        int nextPage = (currentPage % totalPages) + 1;
        switchPage(player, nextPage);
    }

    public void previousPage(Player player) {
        int currentPage = pageCounter.getOrDefault(player.getUniqueId(), 1);
        int previousPage = (currentPage == 1) ? totalPages : currentPage - 1;
        switchPage(player, previousPage);
    }

    private void switchPage(Player player, int page) {
        pageCounter.put(player.getUniqueId(), page);
        player.closeInventory();
        player.openInventory(pageMap.get(page));
    }

    public void addButton(ButtonBuilder buttonBuilder) {
        buttonBuilders.add(buttonBuilder);
    }

    public void build() {
        buttonBuilders.forEach(buttonBuilder ->
                pageMap.forEach((page, inventory) -> {
                    if (buttonBuilder.getPage() == page) {
                        inventory.setItem(buttonBuilder.getPosition(), buttonBuilder.getItemStack());
                    }
                })
        );
        buttonBuilders.forEach(buttonBuilder -> Bukkit.getPluginManager().registerEvents(buttonBuilder, javaPlugin));
    }


    private String getFormattedPageName(int page) {
        return name.replace("%page%", String.valueOf(page));
    }
}
