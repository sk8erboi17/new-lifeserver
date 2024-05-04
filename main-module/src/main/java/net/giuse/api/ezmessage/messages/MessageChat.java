package net.giuse.api.ezmessage.messages;

import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.api.ezmessage.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MessageChat implements Message {

    @Getter
    private final String messageChat;

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }

}