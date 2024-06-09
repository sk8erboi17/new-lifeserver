package net.giuse.api.ezmessage;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.api.ezmessage.messages.MessageActionbar;
import net.giuse.api.ezmessage.messages.MessageChat;
import net.giuse.api.ezmessage.messages.MessageTitle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MessageBuilder {

    private final MessageLoader messageLoader;

    private CommandSender commandSender;

    private String idMessage;

    /*
     * Set CommandSender to send Message
     */
    public MessageBuilder setCommandSender(CommandSender commandSender) {
        this.commandSender = commandSender;
        return this;
    }

    /*
     * Set ID of the messaage to search
     */
    public MessageBuilder setIDMessage(String ID) {
        this.idMessage = ID;
        return this;
    }

    /*
     * Send Message with replaces
     */
    @SneakyThrows
    public void sendMessage(TextReplacer... textReplacers) {
        for (String string : new String[]{"_chat", "_bossbar", "_title"}) {
            Message message = messageLoader.getCache().get(idMessage + string);
            if (message == null) continue;
            switch (message.getMessageType()) {
                //SEND CHAT
                case CHAT:
                    MessageChat messageChat = (MessageChat) message;
                    String messageToReplace = messageChat.getMessageChat();
                    for (TextReplacer textReplacer : textReplacers) {
                        messageToReplace = textReplacer.setText(messageToReplace).returnReplace();
                    }
                    sendChatMessageWithNewlines(commandSender, messageToReplace);
                    break;

                //Send Action Bar
                case ACTION_BAR:
                    if (commandSender instanceof Player) {
                        MessageActionbar messageActionbar = (MessageActionbar) message;
                        String placeHolder = messageActionbar.getMessageBar();
                        if (textReplacers.length == 0) {
                            messageLoader.sendActionBar((Player) commandSender, Component.text(placeHolder));
                            break;
                        }

                        for (TextReplacer textReplacer : textReplacers) {
                            placeHolder = textReplacer.setText(placeHolder).returnReplace();
                        }
                        Component newComponentReplaced = Component.text(placeHolder);
                        messageLoader.sendActionBar((Player) commandSender, newComponentReplaced);
                        break;
                    }
                    //Send Title
                case TITLE:
                    if (commandSender instanceof Player) {
                        MessageTitle messageTitle = (MessageTitle) message;
                        String title = messageTitle.getTitle();
                        String subTitle = messageTitle.getSubTitle();
                        if (textReplacers.length == 0) {
                            TextComponent newTitleComponent = Component.text(title);
                            TextComponent newSubTitleComponent = Component.text(subTitle);
                            messageLoader.sendTitle((Player) commandSender, newTitleComponent, newSubTitleComponent, messageTitle.getFadeIn(), messageTitle.getStay(), messageTitle.getFadeOut());
                            break;
                        }

                        for (TextReplacer textReplacer : textReplacers) {
                            title = textReplacer.setText(title).returnReplace();
                            subTitle = textReplacer.setText(subTitle).returnReplace();

                        }

                        TextComponent newTitleComponent = Component.text(title);
                        TextComponent newSubTitleComponent = Component.text(subTitle);
                        messageLoader.sendTitle((Player) commandSender, newTitleComponent, newSubTitleComponent, messageTitle.getFadeIn(), messageTitle.getStay(), messageTitle.getFadeOut());
                    }
            }
        }
    }


    private void sendChatMessageWithNewlines(CommandSender sender, String message) {
        String[] lines = message.split("%newline%");
        TextColor lastColor = NamedTextColor.WHITE;
        TextDecoration[] decorations = new TextDecoration[]{};
        Pattern colorPattern = Pattern.compile("§[0-9a-fk-or]");

        for (String line : lines) {
            Matcher matcher = colorPattern.matcher(line);

            while (matcher.find()) {
                String colorCode = matcher.group();
                switch (colorCode) {
                    case "§0":
                        lastColor = NamedTextColor.BLACK;
                        break;
                    case "§1":
                        lastColor = NamedTextColor.DARK_BLUE;
                        break;
                    case "§2":
                        lastColor = NamedTextColor.DARK_GREEN;
                        break;
                    case "§3":
                        lastColor = NamedTextColor.DARK_AQUA;
                        break;
                    case "§4":
                        lastColor = NamedTextColor.DARK_RED;
                        break;
                    case "§5":
                        lastColor = NamedTextColor.DARK_PURPLE;
                        break;
                    case "§6":
                        lastColor = NamedTextColor.GOLD;
                        break;
                    case "§7":
                        lastColor = NamedTextColor.GRAY;
                        break;
                    case "§8":
                        lastColor = NamedTextColor.DARK_GRAY;
                        break;
                    case "§9":
                        lastColor = NamedTextColor.BLUE;
                        break;
                    case "§a":
                        lastColor = NamedTextColor.GREEN;
                        break;
                    case "§b":
                        lastColor = NamedTextColor.AQUA;
                        break;
                    case "§c":
                        lastColor = NamedTextColor.RED;
                        break;
                    case "§d":
                        lastColor = NamedTextColor.LIGHT_PURPLE;
                        break;
                    case "§e":
                        lastColor = NamedTextColor.YELLOW;
                        break;
                    case "§f":
                        lastColor = NamedTextColor.WHITE;
                        break;
                    case "§k":
                        decorations = new TextDecoration[]{TextDecoration.OBFUSCATED};
                        break;
                    case "§l":
                        decorations = new TextDecoration[]{TextDecoration.BOLD};
                        break;
                    case "§m":
                        decorations = new TextDecoration[]{TextDecoration.STRIKETHROUGH};
                        break;
                    case "§n":
                        decorations = new TextDecoration[]{TextDecoration.UNDERLINED};
                        break;
                    case "§o":
                        decorations = new TextDecoration[]{TextDecoration.ITALIC};
                        break;
                    case "§r":
                        lastColor = NamedTextColor.WHITE;
                        decorations = new TextDecoration[]{};
                        break;
                }
            }

            Component component = Component.text(line).color(lastColor).decorate(decorations);
            messageLoader.sendChat(sender, component);
        }

        if (lines.length == 0) {
            messageLoader.sendChat(sender, Component.text(message));
        }
    }

}
