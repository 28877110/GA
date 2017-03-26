package org.zjgsu.algorithm.ga.fitness;

import com.google.common.collect.Lists;
import com.mathworks.toolbox.javabuilder.MWException;
import fmincon.Class1;
import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.Parameter;

import java.util.Collections;
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
    public static void compute(Population population) throws MWException {
        Process process = population.getProcess();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Chromsome> chromsomeList = population.getChromsomeList();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Integer[] resourceNum = chromsome.getResourceNum();

            //TODO 调用fmincon
            Double[][] resourceAllocation = chromsome.getResourceAllocation();
            String funStr = chromsome.getFunStr(process);
            Integer[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();

            Class1 fmincon = new Class1();
            fmincon.myfmincon(4, funStr, initCostResourceAllocationRate);


            List<Double> p = Lists.newArrayList();
            for (int j = 0; j < activityList.size(); j++) {
                Double total = 0d;
                for (int k = 0; k < resourceList.size(); k++) {
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
                p.add(total);
            }

            //TODO 计算minP中的最小值p > φ的情况下，资源数量对应的成本作为适应度
            Double minP = Collections.min(p);
            Integer total = Integer.MAX_VALUE;
            if (minP > Parameter.φ) {
                total = 0;
                Integer[] rn = chromsome.getResourceNum();
                for (int j = 0; j < rn.length; j++) {
                    Integer count = rn[j] * resourceList.get(j).getCount();
                    total += count;
                }
            }
            chromsome.setFitness(total);

            if (null == population.getBest()) {
                population.setBest(chromsome);
            } else if (chromsome.getFitness() < population.getBest().getFitness()) {
                population.setBest(chromsome);
            }
        }
    }

}
