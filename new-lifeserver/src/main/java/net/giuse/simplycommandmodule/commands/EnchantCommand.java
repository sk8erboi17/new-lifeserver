package net.giuse.simplycommandmodule.commands;

import net.giuse.api.commands.AbstractCommand;
import net.giuse.api.ezmessage.MessageBuilder;
import net.giuse.api.ezmessage.TextReplacer;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;

public class EnchantCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public EnchantCommand(MessageBuilder messageBuilder) {
        super("enchant", "lifeserver.enchant");
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("enchant-usage").sendMessage();

            return;
        }

        if (args.length == 1) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("enchant-insert-level").sendMessage();
            return;
        }

        if (Enchantment.getByName(args[0]) == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("enchant-invalid").sendMessage(new TextReplacer().match("%enchant%").replaceWith(args[0]));
            return;
        }

        if (NumberUtils.isNumber(args[1]) && Integer.parseInt(args[1]) < 0) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("enchant-correct-insert-level").sendMessage();
            return;
        }

        ItemStack hand = player.getInventory().getItemInHand();
        ItemMeta itemMeta = player.getInventory().getItemInHand().getItemMeta();
        hand.addUnsafeEnchantment(Enchantment.getByName(args[0]), Integer.parseInt(args[1]));
        itemMeta.addEnchant(Enchantment.getByName(args[0]), Integer.parseInt(args[1]), true);
        hand.setItemMeta(itemMeta);
        player.updateInventory();
    }
}
