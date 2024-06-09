package net.giuse.kitmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class PlayerKit {

    private String playerUuid;

    private String kitName;

    private int kitCooldown;
}
