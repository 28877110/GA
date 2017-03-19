package org.zjgsu.algorithm.ga.population;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;


/**
 * Created by wuhanqing on 2017/3/19.
 */
@Getter
@Setter
public class Chromsome {

    //资源数量
    private Integer[] resourceNum;

    //资源配置
    private Double[][] resourceAllocation;



    private Integer fitness;

    public Chromsome(Process process) {
        Integer resourceSize = process.getResourceList().size();
        resourceNum = new Integer[resourceSize];
        resourceAllocation = new Double[Activity.getNum()][resourceSize];

        for (int i = 0; i < resourceSize; i++) {
            resourceNum[i] = 0;

            //面向成本的资源分配方案
            Integer minDealTime = Integer.MAX_VALUE;
            Integer index = null;
            Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
            for (int j = 0; j < Activity.getNum(); j++) {
                Integer dealTime = resourceActivityDealTime[j][i];
                if (dealTime < minDealTime && dealTime > 0) {
                    minDealTime = dealTime;
                }
            }
            for (int j = 0; j < Activity.getNum(); j++) {
                if (j == index) {
                    resourceAllocation[j][i] = 1d;
                } else {
                    resourceAllocation[j][i] = 0d;
                }
            }
        }
    }

}
