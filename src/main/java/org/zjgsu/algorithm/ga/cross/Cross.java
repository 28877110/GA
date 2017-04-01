package org.zjgsu.algorithm.ga.cross;

import java.util.List;
import java.util.Random;

import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.Parameter;

/**
 * 交叉
 * Created by wuhanqing on 2017/3/8.
 */
public class Cross {

    public static void cross(Population population) {

        List<Chromsome> chromsomeList = population.getChromsomeList();
        Random random = new Random();
        for (int i = 0; i < chromsomeList.size() / 2; i++) {
            Double p = random.nextDouble();
            if (p < Parameter.CROSS_PROB) {
                Chromsome chromsome1 = chromsomeList.get(i);
                Chromsome chromsome2 = chromsomeList.get(i + chromsomeList.size() / 2);
                Integer[] resourceCount1 = chromsome1.getResourceCount();
                Integer[] resourceCount2 = chromsome2.getResourceCount();

                Integer index1 = random.nextInt(resourceCount1.length);
                Integer index2 = random.nextInt(resourceCount2.length);

                if (index1 > index2) {
                    for (int j = index2; j <= index1; j++) {
                        Integer temp = resourceCount1[j];
                        resourceCount1[j] = resourceCount2[j];
                        resourceCount2[j] = temp;
                    }
                } else if (index2 > index1) {
                    for (int j = index1; j <= index2; j++) {
                        Integer temp = resourceCount1[j];
                        resourceCount1[j] = resourceCount2[j];
                        resourceCount2[j] = temp;
                    }
                }
            }
        }

    }

}
