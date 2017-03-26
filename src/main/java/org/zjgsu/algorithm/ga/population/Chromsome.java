package org.zjgsu.algorithm.ga.population;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;

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
                if (null != dealTime && dealTime < minDealTime && dealTime > 0) {
                    minDealTime = dealTime;
                    index = j;
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

    public String getFunStr(Process process) {
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < activityList.size(); i++) {
            builder.append("-(");
            Activity activity = activityList.get(i);
            Double expectation = activity.getExpectation();

            boolean flag1 = false;
            for (int j = 0; j < resourceList.size(); j++) {
                Resource resource = resourceList.get(j);
                //r1.qnt x 任务分配率
                if (flag1) {
                    builder.append("+");
                } else {
                    flag1 = true;
                }
                builder.append(resource.getCount()).append("*").append("x(").append(1 + j * activityList.size()).append(")");
                builder.append("/(");

                boolean flag2 = false;
                for (int k = 0; k < activityList.size(); k++) {
                    Integer dealTime = resourceActivityDealTime[j][k];
                    if (null != dealTime && 0 != dealTime) {
                        if (flag2) {
                            builder.append("+");
                        } else {
                            flag2 = true;
                        }
                        builder.append(expectation * dealTime).append("*").append("x(").append(1 + k + j * activityList.size()).append(")");
                    }
                }
                builder.append(")");
            }
            builder.append(");");
        }
        builder.append("]");

        return builder.toString();
    }

}
