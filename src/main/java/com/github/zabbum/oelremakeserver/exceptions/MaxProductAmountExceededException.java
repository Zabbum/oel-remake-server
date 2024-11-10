package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelremakecomponents.plants.industries.AbstractIndustry;

public class MaxProductAmountExceededException extends RuntimeException {
    public MaxProductAmountExceededException(Integer productAmountRequested, AbstractIndustry industry) {
        super("Requested product amount: " + productAmountRequested + ", but max is " + industry.getMaxProductAmountToBuy() + "!");
    }
}
