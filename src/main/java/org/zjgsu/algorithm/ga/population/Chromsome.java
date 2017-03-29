package org.zjgsu.algorithm.ga.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
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
    private Integer[] resourceCount;

    //资源配置
    private Double[][] resourceAllocation;

    private Integer fitness;

    private String funStr;

    private Double[][] aeq;

    private Double[] beq;

    private Double[] lb;

    private Double[] ub;

    Map<Integer, Integer> xValue = Maps.newHashMap();

    public Chromsome(Process process) {
        List<Resource> resourceList =process.getResourceList();
        Integer resourceSize = resourceList.size();
        resourceCount = new Integer[resourceSize];
        resourceAllocation = new Double[resourceSize][Activity.getNum()];

        for (int i = 0; i < Activity.getNum(); i++) {
            //面向成本的资源分配方案
            Integer minDealTime = Integer.MAX_VALUE;
            Integer index = null;
            Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
            for (int j = 0; j < resourceSize; j++) {
                Integer dealTime = resourceActivityDealTime[j][i];
                if (null != dealTime && dealTime < minDealTime && dealTime > 0) {
                    minDealTime = dealTime;
                    index = j;
                }
            }
            for (int j = 0; j < resourceSize; j++) {
                if (j == index) {
                    resourceAllocation[j][i] = 1d;
                } else {
                    resourceAllocation[j][i] = 0d;
                }
            }
        }

        Random random = new Random();
        for (int i = 0; i < resourceSize; i++) {
            Integer limit = resourceList.get(i).getCountLimit();
            resourceCount[i] = random.nextInt(limit) + 1;
            //resourceCount[i] = 6;
        }

        this.funStr = getFunStr(process);
        this.aeq = getAeq(process);
        this.beq = getBeq(process);
        this.lb = getLb(process);
        this.ub = getUb(process);
    }

    public String getFunStr(Process process) {
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();

        int index = 1;
        for (int i = 0; i < resourceList.size(); i++) {
            for (int j = 0; j < activityList.size(); j++) {
                Integer dealTime = resourceActivityDealTime[i][j];
                int idx = j + i * activityList.size();
                if (dealTime != null) {
                    xValue.put(idx, index);
                    index++;
                }
            }
        }


        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < activityList.size(); i++) {
            builder.append("-(");
            Activity activity = activityList.get(i);
            Double expectation = activity.getExpectation();

            boolean flag1 = false;
            for (int j = 0; j < resourceCount.length; j++) {
                if (xValue.get(i + j * activityList.size()) == null) {
                    continue;
                }

                //r1.qnt x 任务分配率
                if (flag1) {
                    builder.append("+");
                } else {
                    flag1 = true;
                }
                builder.append(resourceCount[j]).append("*").append("x(").append(xValue.get(i + j * activityList.size())).append(")");
                builder.append("/(");

                boolean flag2 = false;
                for (int k = 0; k < activityList.size(); k++) {
                    Double activityExpectation = activityList.get(k).getExpectation();
                    if (xValue.get(k + j * activityList.size()) == null) {
                        continue;
                    }
                    Integer dealTime = resourceActivityDealTime[j][k];
                    if (null != dealTime && 0 != dealTime) {
                        if (flag2) {
                            builder.append("+");
                        } else {
                            flag2 = true;
                        }
                        builder.append(activityExpectation * dealTime).append("*").append("x(").append(xValue.get(k + j * activityList.size())).append(")");
                    }
                }
                builder.append(")");
            }
            builder.append(");");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }

    public Double[][] getAeq(Process process) {
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Double[][] aeq = new Double[activityList.size()][];

        for (int i = 0; i < activityList.size(); i++) {
            List<Double> aeqSingle = new ArrayList<>(activityList.size() * resourceList.size());
            for (int j = 0; j < activityList.size() * resourceList.size(); j++) {
                aeqSingle.add(null);
            }
            for (int j = 0; j < activityList.size(); j++) {
                for (int k = 0; k < resourceList.size(); k++) {
                    Integer dealTime = resourceActivityDealTime[k][j];
                    if (null != dealTime) {
                        if (j == i) {
                            aeqSingle.set(j + k * activityList.size(), 1d);
                        } else {
                            aeqSingle.set(j + k * activityList.size(), 0d);
                        }
                    } else {
                        aeqSingle.set(j + k * activityList.size(), null);
                    }
                }
            }
            aeqSingle.removeAll(Collections.singleton(null));
            aeq[i] = aeqSingle.toArray(new Double[]{});
        }

        return aeq;
    }

    public Double[] getBeq(Process process) {
        List<Activity> activityList = process.getActivityList();
        Double[] beq = new Double[activityList.size()];
        for (int i = 0; i < activityList.size(); i++) {
            beq[i] = 1d;
        }
        return beq;
    }

    public Double[] getLb(Process process) {
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();
        Double[] lb = new Double[initCostResourceAllocationRate.length];
        for (int i = 0; i < initCostResourceAllocationRate.length; i++) {
            lb[i] = 0d;
        }
        return lb;
    }

    public Double[] getUb(Process process) {
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();
        Double[] ub = new Double[initCostResourceAllocationRate.length];
        for (int i = 0; i < initCostResourceAllocationRate.length; i++) {
            ub[i] = 1d;
        }
        return ub;
    }

}
