package net.giuse.kitmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.kitmodule.service.KitService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitlist for view a list of kit
 */


public class KitList extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    private final KitService kitService;

    @Inject
    public KitList(MessageBuilder messageBuilder, KitService kitService) {
        super("kitlist", "lifeserver.kitcreate");
        this.messageBuilder = messageBuilder;
        this.kitService = kitService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Check if sender is Console
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }

        //Check if player has permission
        Player p = (Player) sender;
        if (!p.hasPermission("lifeserver.kit.list")) {
            messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
            return;
        }

        //Check if there are kits
        if (kitService.getAllKits().isEmpty()) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-list-empty").sendMessage();
            return;
        }

        //Show a list of kit to player
        StringBuilder sb = new StringBuilder();
        kitService.getAllKits().forEach((kitElement) -> sb.append(StringUtils.capitalize(kitElement.getName())).append(","));
        messageBuilder.setCommandSender(p).setIDMessage("kit-list").sendMessage(new TextReplacer().match("%listkit%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
    }
}
