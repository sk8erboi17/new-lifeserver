package net.giuse.teleportmodule.submodule.spawn.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.teleportmodule.submodule.spawn.service.SpawnService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SetSpawnCommand extends AbstractCommand {
    private final SpawnService spawnService;
    private final MessageBuilder messageBuilder;

    @Inject
    public SetSpawnCommand(SpawnService spawnService, MessageBuilder messageBuilder) {
        super("setspawn", "lifeserver.setspawn");
        this.spawnService = spawnService;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        // Set Spawn
        Player player = (Player) commandSender;
        spawnService.setSpawn(player.getLocation());
        messageBuilder.setCommandSender(commandSender).setIDMessage("setspawn").sendMessage();
    }
}
