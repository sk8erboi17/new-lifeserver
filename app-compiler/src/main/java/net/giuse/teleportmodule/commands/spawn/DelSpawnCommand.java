package net.giuse.teleportmodule.commands.spawn;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.submodule.SpawnLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.inject.Inject;

public class DelSpawnCommand extends AbstractCommand {
    private final SpawnLoaderModule spawnLoaderModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public DelSpawnCommand(SpawnLoaderModule spawnLoaderModule, MessageBuilder messageBuilder) {
        super("delspawn", "lifeserver.delspawn");
        this.messageBuilder = messageBuilder;
        this.spawnLoaderModule = spawnLoaderModule;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        //Delete Spawn
        spawnLoaderModule.setSpawnBuilder(null);
        messageBuilder.setCommandSender(commandSender).setIDMessage("removespawn").sendMessage();
    }
}
