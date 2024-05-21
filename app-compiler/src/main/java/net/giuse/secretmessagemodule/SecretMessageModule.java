package net.giuse.secretmessagemodule;

import ch.jalu.injector.Injector;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.api.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.services.Services;
import net.giuse.secretmessagemodule.files.FileManager;
import net.giuse.secretmessagemodule.messageloader.MessageLoaderSecret;
import net.giuse.secretmessagemodule.process.SecretChatProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public final class SecretMessageModule extends Services {
    @Getter
    private final ArrayList<SecretChatBuilder> secretsChats = new ArrayList<>();
    @Getter
    private final Set<Player> playerMsgToggle = new HashSet<>();
    @Getter
    private final Set<Player> playerSocialSpy = new HashSet<>();
    @Inject
    private Injector injector;
    @Getter
    private FileManager fileManager;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eSecretChatModule§9] §7Loading SecretChats...");
        injector.register(SecretChatProcess.class, injector.newInstance(SecretChatProcess.class));
        //Load Files
        ReflectionsFiles.loadFiles(fileManager = new FileManager());
        MessageLoaderSecret messageLoaderSecret = injector.getSingleton(MessageLoaderSecret.class);
        messageLoaderSecret.load();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eSecretChatModule§9] §7Unloading SecretChats...");
    }

    /*
     * Search from UUID a Player which is a sender
     */
    public SecretChatBuilder getSenderSecretChat(UUID uuid) {
        return secretsChats.stream()
                .filter(secretsChats -> secretsChats.getSender().getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

    /*
     * Search from UUID a Player which is a Receiver
     */
    public SecretChatBuilder getReceiverSecretChat(UUID uuid) {
        return secretsChats.stream()
                .filter(secretsChats -> secretsChats.getReceiver().getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }


}
