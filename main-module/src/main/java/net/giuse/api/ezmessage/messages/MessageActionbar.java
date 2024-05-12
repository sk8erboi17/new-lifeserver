package net.giuse.api.ezmessage.messages;


import net.giuse.api.ezmessage.interfaces.Message;
import net.giuse.api.ezmessage.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageActionbar implements Message {
    private final String messageBar;

    @Override
    public MessageType getMessageType() {
        return MessageType.ACTION_BAR;
    }

}
