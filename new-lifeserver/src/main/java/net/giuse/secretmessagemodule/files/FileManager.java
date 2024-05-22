package net.giuse.secretmessagemodule.files;


import lombok.Getter;
import net.giuse.api.files.abstractfiles.AbstractConfig;
import net.giuse.api.files.annotations.FileAnnotation;
import net.giuse.api.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
@Getter
public class FileManager extends AbstractConfig {

    @FileAnnotation(name = "messages_secret_chat.yml", path = "plugins/LifeServer/messages/messages_secret_chat.yml")
    private File messagesSecretChatFile;
    @YamlAnnotation(nameFile = "messages_secret_chat.yml")
    private YamlConfiguration messagesSecretChatYaml;

}