package net.giuse.kitmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.kitmodule.service.KitService;
import net.giuse.kitmodule.service.PlayerKitService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitdelete for delete a kit
 */


public class KitDelete extends AbstractCommand {

    private final MessageBuilder messageBuilder;
    private final KitService kitService;
    private final PlayerKitService playerKitService;

    @Inject
    public KitDelete(KitService kitService, MessageBuilder messageBuilder, PlayerKitService playerKitService) {
        super("kitdelete", "lifeserver.kitdelete");
        this.kitService = kitService;
        this.messageBuilder = messageBuilder;
        this.playerKitService = playerKitService;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player player = (Player) commandSender;
        messageBuilder.setCommandSender(player);
        //Check if the name of kit is present
        if (!hasEnoughArgs(args)) return;
        String kitName = args[0].toLowerCase();

        //Check if kit exists
        if (!checkIfKitExists(kitName)) return;

        //Delete kit
        deleteKit(kitName);
    }

    private void deleteKit(String kitName) {
        kitService.removeKit(kitName);
        messageBuilder.setIDMessage("kit-removed").sendMessage(new TextReplacer().match("%kit%").replaceWith(kitName));
        playerKitService.removePlayerKit(kitName);
    }

    private boolean checkIfKitExists(String kitname) {
        if (kitService.getKit(kitname) == null) {
            messageBuilder.setIDMessage("kit-doesnt-exists").sendMessage();
            return false;
        }
        return true;
    }

    private boolean hasEnoughArgs(String[] args) {
        if (args.length == 0) {
            messageBuilder.setIDMessage("kit-insert-name-kit").sendMessage();
            return false;
        }
        return true;
    }
}
