package com.github.zabbum.oelremakeserver.model.kits;

import lombok.Data;

@Data
public class BuyIndustryKit {
    private String gameId;
    private Integer playerId;
    private String industryClassName;
    private Integer industryId;
    private Integer productPrice;
}
