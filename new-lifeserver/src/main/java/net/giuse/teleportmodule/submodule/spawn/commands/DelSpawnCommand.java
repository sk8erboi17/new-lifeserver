package net.giuse.teleportmodule.submodule.spawn.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.teleportmodule.submodule.spawn.service.SpawnService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.inject.Inject;

public class DelSpawnCommand extends AbstractCommand {
    private final SpawnService spawnService;
    private final MessageBuilder messageBuilder;

    @Inject
    public DelSpawnCommand(SpawnService spawnService, MessageBuilder messageBuilder) {
        super("delspawn", "lifeserver.delspawn");
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

        // Delete Spawn
        spawnService.deleteSpawn();
        messageBuilder.setCommandSender(commandSender).setIDMessage("removespawn").sendMessage();
    }
}
