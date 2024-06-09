package net.giuse.teleportmodule.submodule.home.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.teleportmodule.submodule.home.dto.Home;
import net.giuse.teleportmodule.submodule.home.service.HomeService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;

public class DelHomeCommand extends AbstractCommand {

    private final HomeService homeService;

    private final MessageBuilder messageBuilder;

    @Inject
    public DelHomeCommand(HomeService homeService, MessageBuilder messageBuilder) {
        super("delhome", "lifeserver.delhome");
        this.homeService = homeService;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player sender = (Player) commandSender;
        String playerUuid = sender.getUniqueId().toString();

        // Check if player has home
        List<Home> homes = homeService.getAllHomes(playerUuid);
        if (homes.isEmpty()) {
            messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
            return;
        }

        // Check if player has multiple homes
        if (sender.hasPermission("lifeserver.delhome.multiple") || sender.isOp()) {
            if (args.length == 0) {
                // Check if player has one home
                if (homes.size() == 1) {
                    String homeName = homes.get(0).getName();
                    homeService.removeHome(playerUuid, homeName);
                    messageBuilder.setCommandSender(sender).setIDMessage("deleted_home").sendMessage();
                    return;
                }
                messageBuilder.setCommandSender(sender).setIDMessage("select-home").sendMessage();
                return;
            }

            // Check if home exists
            String homeName = args[0].toLowerCase();
            Home home = homeService.getHome(playerUuid, homeName);
            if (home == null) {
                messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
                return;
            }

            // Delete home
            homeService.removeHome(playerUuid, homeName);
            messageBuilder.setCommandSender(sender).setIDMessage("deleted_home").sendMessage();
            return;
        }

        // Delete all homes
        for (Home home : homes) {
            homeService.removeHome(playerUuid, home.getName());
        }
        messageBuilder.setCommandSender(sender).setIDMessage("deleted_home").sendMessage();
    }
}
