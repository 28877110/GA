package org.zjgsu.algorithm.ga.fitness;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;

import java.util.List;

/**
 * 适应度
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Fitness {

    /**
     * 计算适应度函数
     * @param population
     */
    public static void compute(Population population) {
        Process process = population.getProcess();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Chromsome> chromsomeList = population.getChromsomeList();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Integer[] resourceNum = chromsome.getResourceNum();
            Double[][] resourceAllocation = chromsome.getResourceAllocation();
            Double[] p = new Double[activityList.size()];
            for (int j = 0; j < activityList.size(); j++) {
                Double total = 0d;
                for (int k = 0; k < resourceList.size(); k++) {
                    Resource resource = resourceList.get(k);
                    Double denominator = 0d;
                    for (int l = 0; l < activityList.size(); l++) {
                        Integer dealTime = resourceActivityDealTime[l][k];
                        Double allocRate = resourceAllocation[l][k];
                        Double expect = activityList.get(l).getExpectation();
                        denominator += dealTime * allocRate * expect;
                    }

                    Double allocationRate = resourceAllocation[j][k];
                    Double molecular = resourceNum[k] * allocationRate;
                    Double result = molecular / denominator;
                    total += result;
                }
                p[j] = total;
            }

            //TODO 计算p中的最小值作为适应度
        }
    }

}
