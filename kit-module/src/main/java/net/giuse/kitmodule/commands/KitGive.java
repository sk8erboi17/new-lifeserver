package net.giuse.kitmodule.commands;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitElement;
import net.giuse.kitmodule.cooldownsystem.PlayerKitCooldown;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;


/**
 * Command /kit for view a list of kit and give a kit
 */
public class KitGive extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final KitModule kitModule;
    private final FileConfiguration mainConfig;
    private final KitGui kitGui;

    @Inject
    public KitGive(KitModule kitModule, KitGui kitGui, FileConfiguration mainConfig, MessageBuilder messageBuilder) {
        super("kit", "lifeserver.kitcreate");
        this.kitModule = kitModule;
        this.mainConfig = mainConfig;
        this.messageBuilder = messageBuilder;
        this.kitGui = kitGui;
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
            if (kitModule.getKitElements().isEmpty()) {
                messageBuilder.setIDMessage("kit-list-empty").sendMessage();
                return;
            }
            StringBuilder sb = new StringBuilder();
            kitModule.getKitElements().forEach((name, kitBuilder) -> sb.append(StringUtils.capitalize(name)).append(","));
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
        int actualCooldown = kitModule.getPlayerCooldown(player.getUniqueId()).getActualCooldown(kitName);

        if (checkCooldown(actualCooldown)) {
            messageBuilder.setIDMessage("kit-wait").sendMessage(new TextReplacer().match("%time%").replaceWith(Utils.formatTime(actualCooldown)));
            return;
        }

        giveKit(player, kitName);
    }

    private void giveKit(Player player, String kitName) {
        KitElement kitElement = kitModule.getKit(kitName);
        if (kitElement != null) {
            //Give kit to Player
            PlayerKitCooldown playerKitCooldown = kitModule.getPlayerCooldown(player.getUniqueId());
            playerKitCooldown.addKit(kitName.toLowerCase(), kitElement.getCoolDown());
            kitElement.giveItems(player);
            messageBuilder.setIDMessage("kit-receive").sendMessage(new TextReplacer().match("%kit%").replaceWith(kitName));
            return;
        }
        messageBuilder.setIDMessage("kit-doesnt-exists").sendMessage();

    }

    private boolean checkCooldown(int cooldown) {
        return cooldown != 0;
    }
}

