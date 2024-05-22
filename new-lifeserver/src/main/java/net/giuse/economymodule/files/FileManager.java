package net.giuse.economymodule.files;

import lombok.Getter;
import net.giuse.api.files.abstractfiles.AbstractConfig;
import net.giuse.api.files.annotations.FileAnnotation;
import net.giuse.api.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
@Getter
public class FileManager extends AbstractConfig {
    @FileAnnotation(name = "messages_economy.yml", path = "plugins/LifeServer/messages/messages_economy.yml")
    private File messagesFile;

    @YamlAnnotation(nameFile = "messages_economy.yml")
    private YamlConfiguration messagesYaml;

}
