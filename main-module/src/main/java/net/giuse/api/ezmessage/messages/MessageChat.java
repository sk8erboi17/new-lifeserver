package net.giuse.api.ezmessage.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.api.ezmessage.type.MessageType;

@Getter
@AllArgsConstructor
public class MessageChat implements Message {

    private final String messageChat;

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }

}
