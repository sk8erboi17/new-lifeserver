package net.giuse.api.ezmessage.messages;

import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.api.ezmessage.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageTitle implements Message {
    private final String title, subTitle;
    private final int fadeIn, stay, fadeOut;

    @Override
    public MessageType getMessageType() {
        return MessageType.TITLE;
    }

}
