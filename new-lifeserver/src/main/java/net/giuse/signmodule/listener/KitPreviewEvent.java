package net.giuse.signmodule.listener;

import net.giuse.kitmodule.dto.Kit;
import net.giuse.kitmodule.service.KitService;
import net.giuse.signmodule.gui.KitPreviewGui;
import net.giuse.signmodule.service.SignService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;
import java.util.List;

public class KitPreviewEvent implements Listener {

    @Inject
    private KitService kitService;

    @Inject
    private SignService signService;

    @Inject
    private KitPreviewGui kitPreviewGui;

    @EventHandler
    public void onKitPreview(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[kp]")) {
            if (event.getLine(1).isEmpty()) {
                event.setLine(0, "Kit Not Found");
                return;
            }
            List<Kit> kits = kitService.getAllKits();
            for (Kit kit : kits) {
                if (event.getLine(1).equalsIgnoreCase(kit.getName())) {
                    for (int i = 0; i < signService.getLines().size(); i++) {
                        event.setLine(i, ChatColor.translateAlternateColorCodes('&', signService.getLines().get(i).replace("%kitname%", kit.getName())));
                    }
                    signService.addSignKitPreview(event.getBlock().getLocation(), kit.getName());
                    return;
                }
            }
            event.setLine(0, "Kit Not Found");
        }
    }

    @EventHandler
    public void onSignKitPreviewInteraction(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() != null &&
                (event.getClickedBlock().getType() == Material.WALL_SIGN ||
                        event.getClickedBlock().getType() == Material.SIGN_POST)) {
            Location location = signService.getSignPreview(event.getClickedBlock().getLocation());
            if (location != null) {
                kitPreviewGui.setKitName(signService.getKitNameByLocation(location));
                kitPreviewGui.initInv();
                kitPreviewGui.openInv(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void signPreviewRemove(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST) {
            Location location = event.getBlock().getLocation();
            Location repository = signService.getSignPreview(location);
            if (repository != null) {
                signService.removeSignPreview(location);
            }
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if(kitPreviewGui.getInventoryBuilder().getName() == null) return;
        if (event.getClickedInventory().getTitle().equalsIgnoreCase(kitPreviewGui.getInventoryBuilder().getName())) {
            event.setCancelled(true);
        }
    }

}
