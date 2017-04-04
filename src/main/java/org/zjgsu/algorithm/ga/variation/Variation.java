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

    public static double variationProb = Parameter.VARIATION_PROB.doubleValue();

    public static void variation(Population population) {
        List<Chromsome> chromsomeList = population.getChromsomeList();
        Process process = population.getProcess();
        Random random = new Random();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Double p = random.nextDouble();

            if (Fitness.bestCost == null) {
                Parameter.VARIATION_PROB = 1.0;
            } else {
                Parameter.VARIATION_PROB = variationProb;
            }

            if (p < Parameter.VARIATION_PROB) {
                Integer[] resourceCount = chromsome.getResourceCount();
                List<Resource> resourceList = process.getResourceList();
                Integer temp = 0;
                for (int j = 0; j < resourceList.size(); j++) {
                    temp += resourceCount[j] * resourceList.get(j).getCost();
                }

//                Integer index = random.nextInt(resourceCount.length);
//                Integer r = random.nextInt(resourceList.get(index).getCountLimit()) + 1;
//                resourceCount[index] = r;
                if (null == Fitness.bestCost) {
                    Integer index = random.nextInt(resourceCount.length);
                    Integer countLimit = resourceList.get(index).getCountLimit();
                    Integer count = random.nextInt(countLimit + 1);
//                    resourceCount[index] = count;
                    resourceCount[index]++;
                } else if (temp >= Fitness.bestCost) {
                    Integer index = random.nextInt(resourceCount.length);
                    while (resourceCount[index] <= 0) {
                        index = random.nextInt(resourceCount.length);
                    }
                    resourceCount[index]--;
                } else {
                    Integer index = random.nextInt(resourceCount.length);
                    resourceCount[index]++;
                }
            }
        }

    }

    public static void variation2(Population population) {
        List<Chromsome> chromsomeList = population.getChromsomeList();
        Process process = population.getProcess();
        Random random = new Random();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Double p = random.nextDouble();

            if (Fitness.bestCost == null) {
                Parameter.VARIATION_PROB = 1.0;
            } else {
                Parameter.VARIATION_PROB = variationProb;
            }

            if (p < Parameter.VARIATION_PROB) {
                Integer[] resourceCount = chromsome.getResourceCount();
                List<Resource> resourceList = process.getResourceList();
                Integer temp = 0;
                for (int j = 0; j < resourceList.size(); j++) {
                    temp += resourceCount[j] * resourceList.get(j).getCost();
                }
                if (null == Fitness.bestCost) {
                    Integer index = random.nextInt(resourceCount.length);
                    Integer countLimit = resourceList.get(index).getCountLimit();
                    Integer count = random.nextInt(countLimit + 1);
                    resourceCount[index]++;
                } else if (temp >= Fitness.bestCost) {
                    Integer index = random.nextInt(resourceCount.length);
                    while (resourceCount[index] <= 0) {
                        index = random.nextInt(resourceCount.length);
                    }
                    resourceCount[index]--;
                } else {
                    Integer index = random.nextInt(resourceCount.length);
                    resourceCount[index]++;
                }
            }

            Integer index = random.nextInt(chromsome.getAeq().length);
            Integer[] resourceCount = chromsome.getResourceCount();
            Boolean b = true;
            Double d1 = null;
            Integer index1 = random.nextInt(resourceCount.length);
            while (b) {
                Double[][] resourceAllocation = chromsome.getResourceAllocation();
                d1 = resourceAllocation[index1][index];
                if (d1 != null) {
                    b = false;
                }
            }
            b = true;
            Double d2 = null;
            while (b) {
                Double[][] resourceAllocation = chromsome.getResourceAllocation();
                d2 = resourceAllocation[index1][index];
                if (d2 != null) {
                    b = false;
                }
            }
            if (d1 == null || d2 == null) {
                return;
            }

            Double tmp = random.nextDouble() * d1;
            d1 -= tmp;
            d2 += tmp;
        }

    }

    public static void variationQ2(Population population) {
        List<Chromsome> chromsomeList = population.getChromsomeList();
        Process process = population.getProcess();
        Random random = new Random();

        if (Fitness.bestMaxLimit == null) {
            Parameter.VARIATION_PROB = 1.0;
        } else {
            Parameter.VARIATION_PROB = variationProb;
        }

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
                if (null == Fitness.bestMaxLimit) {
                    Integer index = random.nextInt(resourceCount.length);
                    Integer countLimit = resourceList.get(index).getCountLimit();
                    Integer count = random.nextInt(countLimit + 1);
                    if (resourceCount[index] > count) {
                        resourceCount[index] -= count;
                    }
                } else if (temp >= Parameter.getUpperCost()) {
                    Integer index = random.nextInt(resourceCount.length);
                    while (resourceCount[index] <= 0) {
                        index = random.nextInt(resourceCount.length);
                    }
                    if (resourceCount[index] > 1) {
                        resourceCount[index]--;
                    }
                } else {
                    Integer index = random.nextInt(resourceCount.length);
                    resourceCount[index]++;
                }
            }

            Integer index = random.nextInt(chromsome.getAeq().length);
            Integer[] resourceCount = chromsome.getResourceCount();
            Boolean b = true;
            Double d1 = null;
            Integer index1 = random.nextInt(resourceCount.length);
            while (b) {
                Double[][] resourceAllocation = chromsome.getResourceAllocation();
                d1 = resourceAllocation[index1][index];
                if (d1 != null) {
                    b = false;
                }
            }
            b = true;
            Double d2 = null;
            while (b) {
                Double[][] resourceAllocation = chromsome.getResourceAllocation();
                d2 = resourceAllocation[index1][index];
                if (d2 != null) {
                    b = false;
                }
            }
            if (d1 == null || d2 == null) {
                return;
            }
            Double tmp = random.nextDouble() * d1;
            d1 -= tmp;
            d2 += tmp;
        }

    }

}
