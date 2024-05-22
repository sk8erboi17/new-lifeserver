package net.giuse.teleportmodule.submodule.home.commands;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.submodule.home.dto.Home;
import net.giuse.teleportmodule.submodule.home.service.HomeService;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class HomeCommand extends AbstractCommand {

    private final HomeService homeService;
    private final MessageBuilder messageBuilder;
    private final TeleportModule teleportModule;

    @Inject
    public HomeCommand(HomeService homeService, MessageBuilder messageBuilder, TeleportModule teleportModule) {
        super("home", "lifeserver.home");
        this.homeService = homeService;
        this.messageBuilder = messageBuilder;
        this.teleportModule = teleportModule;
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
        if (sender.hasPermission("lifeserver.home.multiple") || sender.isOp()) {
            // Looking for a home list
            if (args.length == 0) {
                // Send Home List to Player
                if (homes.size() > 1) {
                    String homeList = homes.stream()
                            .map(Home::getName)
                            .collect(Collectors.joining(", "));

                    messageBuilder.setCommandSender(sender).setIDMessage("home_list")
                            .sendMessage(new TextReplacer().match("%list%").replaceWith(homeList));
                    return;
                }

                // Check if player has one home, and teleport him
                for (Home home : homes) {
                    Location location = home.getLocation();

                    teleportModule.getBackLocations().put(sender, sender.getLocation());
                    PaperLib.teleportAsync(sender, location);
                    messageBuilder.setCommandSender(sender).setIDMessage("teleport").sendMessage();
                }
                return;
            }

            String homeName = args[0].toLowerCase();
            Home home = homeService.getHome(playerUuid, homeName);

            if (home == null) {
                messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
                return;
            }

            Location location = home.getLocation();
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, location);
            messageBuilder.setCommandSender(sender).setIDMessage("teleport").sendMessage();
            return;
        }

        // Teleport player to the default home
        for (Home home : homes) {
            Location location = home.getLocation();
            teleportModule.getBackLocations().put(sender, teleportModule.getBackLocations().put(sender, sender.getLocation()));
            PaperLib.teleportAsync(sender, location);
            messageBuilder.setCommandSender(sender).setIDMessage("teleport").sendMessage();
            break;
        }
    }
}

