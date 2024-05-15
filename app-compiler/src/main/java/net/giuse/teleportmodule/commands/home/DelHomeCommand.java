package net.giuse.teleportmodule.commands.home;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.submodule.HomeLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class DelHomeCommand extends AbstractCommand {
    private final HomeLoaderModule homeLoaderModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public DelHomeCommand(HomeLoaderModule homeLoaderModule, MessageBuilder messageBuilder) {
        super("delhome", "lifeserver.delhome");
        this.homeLoaderModule = homeLoaderModule;
        this.messageBuilder = messageBuilder;
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
        if (homeLoaderModule.getCacheHome().isEmpty()) {
            messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
            return;
        }

        //Check if player has multiple home
        if (sender.hasPermission("lifeserver.delhome.multiple") || sender.isOp()) {
            if (args.length == 0) {

                //Check if player has one home
                if (homeLoaderModule.getHome(sender.getUniqueId()).size() == 1) {
                    homeLoaderModule.getHome(sender.getUniqueId()).keySet().forEach(home -> homeLoaderModule.getHome(sender.getUniqueId()).remove(home));
                    messageBuilder.setCommandSender(sender).setIDMessage("deleted_home").sendMessage();
                    return;
                }
                messageBuilder.setCommandSender(sender).setIDMessage("select-home").sendMessage();
            }

            //Check if home exists
            if (homeLoaderModule.getHome(sender.getUniqueId()).get(args[0]) == null) {
                messageBuilder.setCommandSender(sender).setIDMessage("no_home_found").sendMessage();
                return;
            }

            //Delete home
            homeLoaderModule.getHome(sender.getUniqueId()).remove(args[0].toLowerCase());
            messageBuilder.setCommandSender(sender).setIDMessage("deleted_home").sendMessage();
            return;
        }

        //Delete Home
        homeLoaderModule.getHome(sender.getUniqueId()).keySet().forEach(home -> homeLoaderModule.getHome(sender.getUniqueId()).remove(home));
        messageBuilder.setCommandSender(sender).setIDMessage("deleted_home").sendMessage();
    }
}
