package com.github.zabbum.oelremakeserver.model.kits;

import lombok.Data;

@Data
public class BuyProductsKit {
    private String gameId;
    private Integer playerId;
    private String industryClassName;
    private Integer industryId;
    private Integer productAmount;
    private Integer oilfieldId;
}
