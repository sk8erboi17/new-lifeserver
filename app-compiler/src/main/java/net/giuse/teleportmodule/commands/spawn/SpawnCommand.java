package net.giuse.teleportmodule.commands.spawn;

import io.papermc.lib.PaperLib;
import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.submodule.dto.Spawn;
import net.giuse.teleportmodule.submodule.subservice.SpawnService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SpawnCommand extends AbstractCommand {
    private final SpawnService spawnService;
    private final MessageBuilder messageBuilder;
    private final TeleportModule teleportModule;

    @Inject
    public SpawnCommand(SpawnService spawnService, MessageBuilder messageBuilder, TeleportModule teleportModule) {
        super("spawn", "lifeserver.spawn");
        this.spawnService = spawnService;
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
        Player player = (Player) commandSender;

        // Check if there is a spawn
        Spawn spawn = spawnService.getSpawn();
        if (spawn == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("no-spawn").sendMessage();
            return;
        }

        // Teleport to spawn
        teleportModule.getBackLocations().put(player, player.getLocation());
        PaperLib.teleportAsync(player, spawn.getLocation());
        messageBuilder.setCommandSender(commandSender).setIDMessage("teleported-spawn").sendMessage();
    }
}
