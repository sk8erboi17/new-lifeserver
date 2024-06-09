package net.giuse.signmodule;


import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.modules.AbstractModule;
import net.giuse.signmodule.databases.SignPreviewRepository;
import net.giuse.signmodule.files.SignFileManager;
import net.giuse.signmodule.service.SignService;
import org.bukkit.Bukkit;

import javax.inject.Inject;


public class SignModule extends AbstractModule {

    @Getter
    private SignFileManager fileSigns;

    @Inject
    private Injector injector;

    @SneakyThrows
    @Override
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Trade...");
        ReflectionsFiles.loadFiles(fileSigns = new SignFileManager());
        injector.register(SignFileManager.class, fileSigns);
        injector.getSingleton(SignPreviewRepository.class).createTable();
        injector.getSingleton(SignService.class);
    }

    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Trades...");
    }

    @Override
    public void reload() {
        fileSigns.setFile(fileSigns.getSignFile());
        fileSigns.setYamlConfiguration(fileSigns.getSignYaml());
        fileSigns.reload();
    }

}

