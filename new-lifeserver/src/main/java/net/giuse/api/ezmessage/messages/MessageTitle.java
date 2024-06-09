package net.giuse.api.ezmessage.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.api.ezmessage.type.MessageType;

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
