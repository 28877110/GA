package org.zjgsu.algorithm.ga.variation;

import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.fitness.Fitness;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.Parameter;

/**
 * 变异
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Variation {

    public static void variation(Population population) {
        List<Chromsome> chromsomeList = population.getChromsomeList();
        Process process = population.getProcess();
        Random random = new Random();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Double p = random.nextDouble();
            if (p < Parameter.VARIATION_PROB) {
                Integer[] resourceCount = chromsome.getResourceCount();
                List<Resource> resourceList = process.getResourceList();
                Integer temp = 0;
                for (int j = 0; j < resourceList.size(); j++) {
                    temp += resourceCount[j] * resourceList.get(j).getCost();
                }
                if (temp >= Fitness.bestCost) {
                    Integer index = random.nextInt(resourceCount.length);
                    resourceCount[index]--;
                } else {
                    Integer index = random.nextInt(resourceCount.length);
                    resourceCount[index]++;
                }
            }
        }

    }

}
