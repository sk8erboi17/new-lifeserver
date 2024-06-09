package net.giuse.trademodule.dto;

import lombok.Data;
import net.giuse.trademodule.dto.enums.TradeRequestEnum;

@Data
public class TradeRequest {

    private TradePlayer sender;

    private TradePlayer receiver;

    private TradeRequestEnum type;

}
