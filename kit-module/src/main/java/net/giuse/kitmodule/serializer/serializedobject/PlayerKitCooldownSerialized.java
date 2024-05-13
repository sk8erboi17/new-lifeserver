package net.giuse.kitmodule.serializer.serializedobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.kitmodule.cooldownsystem.PlayerKitCooldown;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerKitCooldownSerialized {
    private final UUID uuidPlayer;
    private final PlayerKitCooldown playerKitCooldown;
}
