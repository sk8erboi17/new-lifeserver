package net.giuse.teleportmodule.submodule.home.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.teleportmodule.submodule.home.dto.Home;
import net.giuse.teleportmodule.submodule.home.service.HomeService;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import javax.inject.Inject;
import java.util.List;

public class SetHomeCommand extends AbstractCommand {
    private final HomeService homeService;
    private final MessageBuilder messageBuilder;

    @Inject
    public SetHomeCommand(HomeService homeService, MessageBuilder messageBuilder) {
        super("sethome", "lifeserver.sethome");
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
        Location location = sender.getLocation();
        String locationString = serializeLocation(location);

        // Check if player has multiple homes
        if (sender.hasPermission("lifeserver.sethome.multiple") || sender.isOp()) {
            for (PermissionAttachmentInfo effectivePermission : sender.getEffectivePermissions()) {
                if (effectivePermission.getPermission().contains("lifeserver.home.multiple.") || sender.isOp()) {
                    // Get Max Home Available
                    int maxHomes;
                    if (!sender.isOp()) {
                        maxHomes = Integer.parseInt(effectivePermission.getPermission().replace("lifeserver.home.multiple.", ""));
                    } else {
                        maxHomes = Integer.MAX_VALUE;
                    }

                    // Check if player has reached max home
                    List<Home> homes = homeService.getAllHomes(playerUuid);
                    if (homes.size() >= maxHomes) {
                        messageBuilder.setCommandSender(sender).setIDMessage("max_home_reached").sendMessage();
                        return;
                    }

                    // Check if there is a name
                    if (args.length == 0) {
                        messageBuilder.setCommandSender(sender).setIDMessage("sethome").sendMessage();
                        Home existingHome = homeService.getHome(playerUuid, "default");
                        if (existingHome != null) {
                            homeService.removeHome(playerUuid, "default");
                            homeService.addHome(playerUuid, "default", locationString);
                        } else {
                            homeService.addHome(playerUuid, "default", locationString);
                        }
                        return;
                    }


                    Home existingHome = homeService.getHome(playerUuid, args[0].toLowerCase());
                    if (existingHome != null) {
                        homeService.removeHome(playerUuid, args[0].toLowerCase());
                        homeService.addHome(playerUuid, args[0].toLowerCase(), locationString);
                    } else {
                        homeService.addHome(playerUuid, args[0].toLowerCase(), locationString);
                    }
                    messageBuilder.setCommandSender(sender).setIDMessage("sethome").sendMessage();
                    return;
                }
            }
            return;
        }

        // Set home
        homeService.addHome(playerUuid, "default", locationString);
        messageBuilder.setCommandSender(sender).setIDMessage("sethome").sendMessage();
    }

    private String serializeLocation(Location location) {
        return location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }
}
