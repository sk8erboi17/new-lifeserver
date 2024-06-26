package net.giuse.simplycommandmodule.commands;


import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

public class GamemodeCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public GamemodeCommand(MessageBuilder messageBuilder) {
        super("gamemode",
                "Change gamemode",
                "/gamemode [player]",
                new ArrayList<>(Collections.singleton("gm")),
                "lifeserver.gamemode");
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            if (commandSender instanceof ConsoleCommandSender) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("gamemode-available").sendMessage();
                return;
            }

            Player player = (Player) commandSender;
            if (args.length == 0) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("gamemode-available").sendMessage();
                return;
            }
            setGamemode(args[0], player);
            return;
        }


        if (!commandSender.hasPermission("lifeserver.gamemode.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }

        if (setGamemode(args[0], target.getPlayer())) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("gamemode-other").sendMessage(
                    new TextReplacer().match("%gamemode%").replaceWith(args[0]),
                    new TextReplacer().match("%player_name%").replaceWith(target.getName()));
            return;
        }

        messageBuilder.setCommandSender(commandSender).setIDMessage("gamemode-available").sendMessage();
    }


    /*
     * Gamemode Selector
     */
    private boolean setGamemode(String gamemode, Player player) {

        if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("1")) {
            player.setGameMode(GameMode.CREATIVE);
            messageBuilder.setCommandSender(player).setIDMessage("gamemode").sendMessage(new TextReplacer().match("%gamemode%").replaceWith("creative"));

            return true;
        }

        if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("0")) {
            player.setGameMode(GameMode.SURVIVAL);
            messageBuilder.setCommandSender(player).setIDMessage("gamemode").sendMessage(new TextReplacer().match("%gamemode%").replaceWith("survival"));
            return true;
        }

        if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("2")) {
            player.setGameMode(GameMode.ADVENTURE);
            messageBuilder.setCommandSender(player).setIDMessage("gamemode").sendMessage(new TextReplacer().match("%gamemode%").replaceWith("adventure"));
            return true;
        }

        if (gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("3")) {
            player.setGameMode(GameMode.SPECTATOR);
            messageBuilder.setCommandSender(player).setIDMessage("gamemode").sendMessage(new TextReplacer().match("%gamemode%").replaceWith("spectator"));
            return true;
        }
        messageBuilder.setCommandSender(player).setIDMessage("gamemode-available").sendMessage();
        return false;
    }
}

