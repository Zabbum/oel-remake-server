package com.github.zabbum.oelremakeserver.model.kits;

import lombok.Data;

@Data
public class SabotageKit {
    private String gameId;
    private Integer playerId;
    private String plantClassName;
    private Integer plantId;
}
