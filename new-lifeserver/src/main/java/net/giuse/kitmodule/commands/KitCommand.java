package net.giuse.kitmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.kitmodule.dto.Kit;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.kitmodule.service.KitService;
import net.giuse.kitmodule.service.PlayerKitService;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;


/**
 * Command /kit for view a list of kit and give a kit
 */
public class KitCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final KitService kitService;
    private final PlayerKitService playerKitService;
    private final FileConfiguration mainConfig;
    private final MainModule mainModule;
    private final KitGui kitGui;

    @Inject
    public KitCommand(KitService kitService, KitGui kitGui, FileConfiguration mainConfig, MessageBuilder messageBuilder, PlayerKitService playerKitService, MainModule mainModule) {
        super("kit", "lifeserver.kitcreate");
        this.kitService = kitService;
        this.mainConfig = mainConfig;
        this.messageBuilder = messageBuilder;
        this.kitGui = kitGui;
        this.playerKitService = playerKitService;
        this.mainModule = mainModule;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        //Check if Sender is Player
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }

        //Check if show kit list or kit gui
        Player player = (Player) sender;
        messageBuilder.setCommandSender(player);
        if (args.length == 0) {

            //Check if player has permission
            if (!player.hasPermission("lifeserver.kit.list")) {
                messageBuilder.setIDMessage("no-perms").sendMessage();
                return;
            }

            //check if show gui or list
            if (mainConfig.getBoolean("use-kit-gui")) {
                kitGui.openInv(player);
                return;
            }

            //Check if there are kits
            if (kitService.getAllKits().isEmpty()) {
                messageBuilder.setIDMessage("kit-list-empty").sendMessage();
                return;
            }
            StringBuilder sb = new StringBuilder();
            kitService.getAllKits().forEach((kit) -> sb.append(StringUtils.capitalize(kit.getName())).append(","));
            messageBuilder.setIDMessage("kit-list").sendMessage(new TextReplacer().match("%listkit%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
            return;
        }
        String kitName = args[0].toLowerCase();
        //Check if player has permission
        if (!player.hasPermission("lifeserver.kit.  " + kitName)) {
            messageBuilder.setIDMessage("no-perms").sendMessage();
            return;
        }


        //Check if player can get kit
        Integer actualCooldown = playerKitService.getPlayerCooldown(player.getUniqueId(), kitName);

        if (checkCooldown(actualCooldown)) {
            if (actualCooldown == null) {
                messageBuilder.setIDMessage("kit-doesnt-exists").sendMessage();
            } else {
                messageBuilder.setIDMessage("kit-wait").sendMessage(new TextReplacer().match("%time%").replaceWith(Utils.formatTime(actualCooldown)));
            }
            return;
        }

        giveKit(player, kitName);
    }

    private void giveKit(Player player, String kitName) {
        Kit kit = kitService.getKit(kitName);
        if (kit != null) {
            playerKitService.addPlayerCooldown(player.getUniqueId(), kitName);

            new BukkitRunnable() {
                @Override
                public void run() {
                    int newCooldown = (playerKitService.getPlayerCooldown(player.getUniqueId(), kitName) - 1);
                    if (newCooldown <= 0) {
                        this.cancel();
                        playerKitService.updateCooldown(player.getUniqueId(), kitName, 0);
                    } else {
                        playerKitService.updateCooldown(player.getUniqueId(), kitName, newCooldown);

                    }
                }
            }.runTaskTimerAsynchronously(mainModule, 20L, 20L);
            kit.giveItems(player);
            messageBuilder.setIDMessage("kit-receive").sendMessage(new TextReplacer().match("%kit%").replaceWith(kitName));
            return;
        }
    }

    private boolean checkCooldown(Integer cooldown) {
        if (cooldown == null) return true;
        return cooldown > 0;
    }
}

