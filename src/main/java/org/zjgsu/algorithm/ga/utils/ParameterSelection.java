package org.zjgsu.algorithm.ga.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wuhanqing on 2017/4/3.
 */
@Getter
public class ParameterSelection {

    private Object[][] table = new Object[25][6];

    public ParameterSelection() {
        table[0] = new Object[]{20, 0.7, 0.5, 0.005, 300, 0.05};
        table[1] = new Object[]{20, 0.75, 0.6, 0.010, 600, 0.1};
        table[2] = new Object[]{20, 0.8, 0.7, 0.015, 900, 0.15};
        table[3] = new Object[]{20, 0.85, 0.8, 0.02, 1200, 0.2};
        table[4] = new Object[]{20, 0.9, 0.9, 0.025, 1500, 0.25};

        table[5] = new Object[]{30, 0.7, 0.6, 0.015, 1200, 0.25};
        table[6] = new Object[]{30, 0.75, 0.7, 0.02, 1500, 0.05};
        table[7] = new Object[]{30, 0.8, 0.8, 0.025, 300, 0.1};
        table[8] = new Object[]{30, 0.85, 0.9, 0.005, 600, 0.15};
        table[9] = new Object[]{30, 0.9, 0.5, 0.01, 900, 0.2};

        table[10] = new Object[]{40, 0.7, 0.7, 0.025, 600, 0.2};
        table[11] = new Object[]{40, 0.75, 0.8, 0.005, 900, 0.25};
        table[12] = new Object[]{40, 0.8, 0.9, 0.01, 1200, 0.05};
        table[13] = new Object[]{40, 0.85, 0.5, 0.015, 1500, 0.1};
        table[14] = new Object[]{40, 0.9, 0.6, 0.02, 300, 0.15};

        table[15] = new Object[]{50, 0.7, 0.8, 0.01, 1500, 0.15};
        table[16] = new Object[]{50, 0.75, 0.9, 0.015, 300, 0.2};
        table[17] = new Object[]{50, 0.8, 0.5, 0.02, 600, 0.25};
        table[18] = new Object[]{50, 0.85, 0.6, 0.025, 900, 0.05};
        table[19] = new Object[]{50, 0.9, 0.7, 0.005, 1200, 0.1};

        table[20] = new Object[]{60, 0.7, 0.9, 0.02, 900, 0.1};
        table[21] = new Object[]{60, 0.75, 0.5, 0.025, 1200, 0.15};
        table[22] = new Object[]{60, 0.8, 0.6, 0.005, 1500, 0.2};
        table[23] = new Object[]{60, 0.85, 0.7, 0.01, 300, 0.25};
        table[24] = new Object[]{60, 0.9, 0.8, 0.015, 600, 0.05};
    }

}
