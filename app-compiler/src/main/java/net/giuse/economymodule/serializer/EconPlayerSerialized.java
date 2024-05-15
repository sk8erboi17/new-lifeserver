package net.giuse.economymodule.serializer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class EconPlayerSerialized {
    private final UUID uuid;
    private final double balance;
}
