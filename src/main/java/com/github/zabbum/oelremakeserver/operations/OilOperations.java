package com.github.zabbum.oelremakeserver.operations;

import java.util.Arrays;
import java.util.Random;

/**
 * Generate oil prices for each round
 */
public class OilOperations {
    public static Double[] generatePrices(int roundCount) {
        Double[] oilPrices = new Double[roundCount];
        Random random = new Random();

        // Initialize every value
        Arrays.fill(oilPrices, (double) 0);

        // First value
        // 7 - 13
        oilPrices[0] = (double) (random.nextInt(7) + 7);

        // Rest of values
        for (int i = 1; i < oilPrices.length; i++) {
            // Ensure value is correct
            //   Regenerate if less than 1
            while (oilPrices[i] < 1) {
                oilPrices[i] = oilPrices[i - 1] + random.nextInt(14) - 7;
            }
            //   If greater than 20, set to 20
            if (oilPrices[i] > 20) {
                oilPrices[i] = 20.0;
            }
        }

        return oilPrices;
    }
}
