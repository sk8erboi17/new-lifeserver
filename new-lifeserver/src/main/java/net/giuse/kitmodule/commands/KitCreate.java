package net.giuse.kitmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.kitmodule.dto.Kit;
import net.giuse.kitmodule.dto.PlayerKit;
import net.giuse.kitmodule.service.KitService;
import net.giuse.kitmodule.service.PlayerKitService;
import net.giuse.mainmodule.utils.Utils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Command /kitcreate for create a kit
 */
public class KitCreate extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    private final KitService kitService;

    private final PlayerKitService playerKitService;

    @Inject
    public KitCreate(KitService kitService, MessageBuilder messageBuilder, PlayerKitService playerKitService) {
        super("kitcreate", "lifeserver.kitcreate");
        this.kitService = kitService;
        this.messageBuilder = messageBuilder;
        this.playerKitService = playerKitService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Check if sender is Console
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }

        Player player = (Player) sender;
        messageBuilder.setCommandSender(player);
        //Check args
        if (args.length == 0) {
            messageBuilder.setIDMessage("kit-insert-name-kit").sendMessage();
            return;
        }
        String kitName = args[0].toLowerCase();

        if (args.length == 1) {
            messageBuilder.setIDMessage("kit-cooldown").sendMessage();
            return;
        }

        //Check if KitExists
        if (kitService.getKit(kitName) != null) {
            messageBuilder.setIDMessage("kit-already-exists").sendMessage();
            return;
        }

        //Check if Player has inventoryu Empty
        if (isEmpty(player)) {
            messageBuilder.setIDMessage("must-have-item").sendMessage();
            return;
        }

        //Check if Number is valid
        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setIDMessage("kit-cooldown-valid").sendMessage();

            return;
        }
        try {
            if (Integer.parseInt(args[1]) < 0) {
                messageBuilder.setIDMessage("kit-cooldown-valid").sendMessage();
                return;
            }
        } catch (NumberFormatException e) {
            messageBuilder.setIDMessage("kit-cooldown-max").sendMessage();

            return;
        }
        int coolDown = Integer.parseInt(args[1]);

        createKit(player, kitName, coolDown);
        messageBuilder.setIDMessage("kit-created").sendMessage(new TextReplacer().match("%kit%").replaceWith(kitName));

    }


    /*
     *  Create Kit
     */
    private void createKit(Player player, String kitName, int coolDownKit) {
        List<ItemStack> inventoryItem = new ArrayList<>();
        Arrays.stream(player.getInventory().getContents())
                .filter(addItem -> addItem != null && !addItem.getType().equals(Material.AIR))
                .forEach(inventoryItem::add);

        Kit kit = new Kit(kitName, coolDownKit, Utils.listItemStackToBase64(inventoryItem));
        kit.build();

        kitService.addKit(kit);
        if (playerKitService.getPlayerKits().isEmpty()) {
            Integer newCooldown = playerKitService.getPlayerCooldown(
                    player.getUniqueId(), kitName);
            if (newCooldown == null) {
                playerKitService.addPlayerCooldown(player.getUniqueId(), kitName);
                playerKitService.updateCooldown(
                        player.getUniqueId(),
                        kitName,
                        0
                );
            }

        } else {
            for (PlayerKit allPlayerKit : playerKitService.getPlayerKits()) {
                Integer newCooldown = playerKitService.getPlayerCooldown(
                        UUID.fromString(allPlayerKit.getPlayerUuid()), kitName);
                if (newCooldown == null) {
                    playerKitService.addPlayerCooldown(UUID.fromString(allPlayerKit.getPlayerUuid()), kitName);
                    playerKitService.updateCooldown(
                            UUID.fromString(allPlayerKit.getPlayerUuid()),
                            kitName,
                            0
                    );
                }
            }
        }

    }

    private boolean isEmpty(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) return false;
        }
        return true;
    }

}
