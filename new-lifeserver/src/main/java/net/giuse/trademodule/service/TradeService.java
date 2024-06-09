package net.giuse.trademodule.service;

import net.giuse.trademodule.dto.TradeRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TradeService {

    private final Set<TradeRequest> pendingRequests = new HashSet<>();

    public void add(TradeRequest tradeRequest) {
        pendingRequests.add(tradeRequest);
    }

    public TradeRequest getByPlayer(UUID playerUuid) {
        for (TradeRequest pendingRequest : pendingRequests) {
            if (playerUuid.equals(pendingRequest.getReceiver().getPlayerUuid())) {
                return pendingRequest;
            }
            if (playerUuid.equals(pendingRequest.getSender().getPlayerUuid())) {
                return pendingRequest;
            }
        }
        return null;
    }

    public void delete(TradeRequest tradeRequest) {
        pendingRequests.add(tradeRequest);
    }

}
