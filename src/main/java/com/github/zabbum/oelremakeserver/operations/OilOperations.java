package com.github.zabbum.oelremakeserver.operations;

import java.util.*;

/**
 * Generate oil prices for each round
 */
public class OilOperations {
    public static List<Double> generatePrices(int roundCount) {
        List<Double> oilPrices = new ArrayList<>(Collections.nCopies(roundCount, 0.0));
        Random random = new Random();
        // First value
        // 7 - 13
        oilPrices.set(0, (double) (random.nextInt(7) + 7));

        // Rest of values
        for (int i = 1; i < roundCount; i++) {
            // Ensure value is correct
            //   Regenerate if less than 1
            while (oilPrices.get(i) < 1) {
                oilPrices.set(i, oilPrices.get(i-1) + random.nextInt(14) - 7);
            }
            //   If greater than 20, set to 20
            if (oilPrices.get(i) > 20) {
                oilPrices.set(i, 20.0);
            }
        }

        return oilPrices;
    }
}
