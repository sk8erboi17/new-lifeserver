package net.giuse.teleportmodule.files;


import lombok.Getter;
import net.giuse.api.files.abstractfiles.AbstractConfig;
import net.giuse.api.files.annotations.FileAnnotation;
import net.giuse.api.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class TeleportFileManager extends AbstractConfig {

    @FileAnnotation(name = "messages_warp.yml", path = "plugins/LifeServer/messages/messages_warp.yml")
    private File messagesWarpFile;

    @YamlAnnotation(nameFile = "messages_warp.yml")
    private YamlConfiguration messagesWarpYaml;

    @FileAnnotation(name = "messages_teleport.yml", path = "plugins/LifeServer/messages/messages_teleport.yml")
    private File messagesTeleportFile;

    @YamlAnnotation(nameFile = "messages_teleport.yml")
    private YamlConfiguration messagesTeleportYaml;

    @FileAnnotation(name = "messages_home.yml", path = "plugins/LifeServer/messages/messages_home.yml")
    private File messagesHomeFile;

    @YamlAnnotation(nameFile = "messages_home.yml")
    private YamlConfiguration messagesHomeYaml;

    @FileAnnotation(name = "messages_spawn.yml", path = "plugins/LifeServer/messages/messages_spawn.yml")
    private File messagesSpawnFile;

    @YamlAnnotation(nameFile = "messages_spawn.yml")
    private YamlConfiguration messagesSpawnYaml;

    @FileAnnotation(name = "warp_gui_config.yml", path = "plugins/LifeServer/warp_gui_config.yml")
    private File warpFile;

    @YamlAnnotation(nameFile = "warp_gui_config.yml")
    private YamlConfiguration warpYaml;

}