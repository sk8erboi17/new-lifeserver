package net.giuse.teleportmodule.commands.spawn;


import io.papermc.lib.PaperLib;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.submodule.SpawnLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SpawnCommand extends AbstractCommand {
    private final SpawnLoaderModule spawnLoaderModule;
    private final MessageBuilder messageBuilder;

    private final TeleportModule teleportModule;

    @Inject
    public SpawnCommand(SpawnLoaderModule spawnLoaderModule, MessageBuilder messageBuilder, TeleportModule teleportModule) {
        super("spawn", "lifeserver.spawn");
        this.spawnLoaderModule = spawnLoaderModule;
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
        Player player = (Player) commandSender;

        //Check if there is a spawn
        if (spawnLoaderModule.getSpawnBuilder() == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("no-spawn").sendMessage();
            return;
        }

        //Teleport to spawn
        teleportModule.getBackLocations().put(player, player.getLocation());
        PaperLib.teleportAsync(player, spawnLoaderModule.getSpawnBuilder().getLocation());
        messageBuilder.setCommandSender(commandSender).setIDMessage("teleported-spawn").sendMessage();

    }
}
