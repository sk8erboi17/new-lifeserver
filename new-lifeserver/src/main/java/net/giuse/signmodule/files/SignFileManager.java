package net.giuse.signmodule.files;

import lombok.Getter;
import net.giuse.api.files.abstractfiles.AbstractConfig;
import net.giuse.api.files.annotations.FileAnnotation;
import net.giuse.api.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class SignFileManager extends AbstractConfig {

    @FileAnnotation(name = "sign_preview.yml", path = "plugins/LifeServer/sign_preview.yml")
    private File signFile;

    @YamlAnnotation(nameFile = "sign_preview.yml")
    private YamlConfiguration signYaml;

}
