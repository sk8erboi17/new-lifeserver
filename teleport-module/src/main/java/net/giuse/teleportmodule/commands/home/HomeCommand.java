package net.giuse.teleportmodule.commands.home;

import io.papermc.lib.PaperLib;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.submodule.HomeLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HomeCommand extends AbstractCommand {

    private final HomeLoaderModule homeLoaderModule;

    private final MessageBuilder messageBuilder;

    private final TeleportModule teleportModule;

    @Inject
    public HomeCommand(HomeLoaderModule homeLoaderModule, MessageBuilder messageBuilder, TeleportModule teleportModule) {
        super("home", "lifeserver.home");
        this.homeLoaderModule = homeLoaderModule;
        this.messageBuilder = messageBuilder;
        this.teleportModule = teleportModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player sender = (Player) commandSender;

        //Check if player has home
        if (homeLoaderModule.getHome(sender.getUniqueId()).isEmpty()) {
            messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
            return;
        }

        //Check if player has multiple home
        if (sender.hasPermission("lifeserver.home.multiple") || sender.isOp()) {

            //Looking for a home list
            if (args.length == 0) {

                //Send Home List to Player
                if (homeLoaderModule.getHome(sender.getUniqueId()).size() > 1) {
                    StringBuilder listHome = new StringBuilder();

                    int i = 0;
                    for (String s : homeLoaderModule.getHome(sender.getUniqueId()).keySet()) {
                        i++;
                        if (i == homeLoaderModule.getHome(sender.getUniqueId()).size()) {
                            listHome.append(s);
                            break;
                        }
                        listHome.append(s).append(",");
                    }

                    messageBuilder.setCommandSender(sender).setIDMessage("home_list").sendMessage(new TextReplacer().match("%list%").replaceWith(listHome.toString()));
                    return;
                }

                //Check if player has one home, and teleport him
                for (String s : homeLoaderModule.getHome(sender.getUniqueId()).keySet()) {
                    teleportModule.getBackLocations().put(sender, sender.getLocation());
                    PaperLib.teleportAsync(sender, homeLoaderModule.getHome(sender.getUniqueId()).get(s));
                    messageBuilder.setCommandSender(sender).setIDMessage("teleport").sendMessage();
                }
                return;
            }
            if (homeLoaderModule.getHome(sender.getUniqueId()).get(args[0].toLowerCase()) == null) {
                messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
                return;
            }
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, homeLoaderModule.getHome(sender.getUniqueId()).get(args[0].toLowerCase()));
            messageBuilder.setCommandSender(sender).setIDMessage("teleport").sendMessage();
            return;
        }

        //Teleport player to the default home
        for (String s : homeLoaderModule.getHome(sender.getUniqueId()).keySet()) {
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, homeLoaderModule.getHome(sender.getUniqueId()).get(s));
            messageBuilder.setCommandSender(sender).setIDMessage("teleport").sendMessage();
            break;
        }
    }
}
