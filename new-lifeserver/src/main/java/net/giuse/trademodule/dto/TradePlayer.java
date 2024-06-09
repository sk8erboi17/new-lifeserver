package net.giuse.trademodule.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TradePlayer {
    private UUID playerUuid;
    private boolean isInTrade;
}
