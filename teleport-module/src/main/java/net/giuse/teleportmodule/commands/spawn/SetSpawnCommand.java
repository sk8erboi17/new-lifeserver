package net.giuse.teleportmodule.commands.spawn;

import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.submodule.SpawnLoaderModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SetSpawnCommand extends AbstractCommand {
    private final SpawnLoaderModule spawnLoaderModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public SetSpawnCommand(SpawnLoaderModule spawnLoaderModule, MessageBuilder messageBuilder) {
        super("setspawn", "lifeserver.setspawn");
        this.spawnLoaderModule = spawnLoaderModule;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        //Set Spawn
        Player player = (Player) commandSender;
        spawnLoaderModule.setSpawnBuilder(new SpawnBuilder(player.getLocation()));
        messageBuilder.setCommandSender(commandSender).setIDMessage("setspawn").sendMessage();

    }
}
