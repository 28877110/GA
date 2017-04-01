package org.zjgsu.algorithm.ga.method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import fmincon.Class1;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.utils.Parameter;

/**
 * 分支定界
 * Created by wuhanqing on 2017/3/31.
 */
public class BranchAndBound {

    private Map<Integer, Integer> xValue = Maps.newHashMap();
    Class1 fminicon = new Class1();

    public BranchAndBound() throws MWException {}

    public void execute(Process process) throws MWException {
        List<Resource> resourceList = process.getResourceList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Activity> activityList = process.getActivityList();
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();

        String funStr = getFminconFunStr(process);
        Double[] x0 = process.getInitCostResourceAllocationRate();
        List<Double> list = Lists.newArrayList(x0);
        for (int i = 0; i < resourceList.size(); i++) {
            list.add(6d);
        }
        x0 = list.toArray(new Double[]{});
        Double[][] a = getA(process);
        Double[] b = getB(process);
        Double[][] aeq = getAeq(process);
        Double[] beq = getBeq(process);
        Double[] lb = getLb(process);
        Double[] ub = getUb(process);

        Object[] resultArray = fminicon.myfmincon(1, funStr, x0, a, b, aeq, beq, lb, ub);
        MWNumericArray result0 = (MWNumericArray) resultArray[0];
        double[] result = result0.getDoubleData();
        double[] resources = new double[resourceList.size()];
        int index = 0;
        for (int i = resourceList.size(); i >= 0; i--) {
            double resource = result[result.length - i];
            resources[index++] = resource;
        }
        Random random = new Random();
        int one = random.nextInt(resources.length);
        while (resources[one] <= 0) {
            one = random.nextInt(resources.length);
        }
        double f = Math.floor(one);
        double c = Math.ceil(one);




        System.out.println();

    }

    public void doBranchAndBound(Process process, double[] resources) {

    }


    public Double[][] getA(Process process) {
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();

        Double[][] a = new Double[resourceList.size()][];
        for (int i = 0; i < resourceList.size(); i++) {
            List<Double> list = Lists.newArrayList();
            for (int j = 0; j < resourceList.size(); j++) {
                for (int k = 0; k < activityList.size(); k++) {
                    if (resourceActivityDealTime[j][k] != null) {
                        if (j == i) {
                            Double d = resourceActivityDealTime[j][k] * activityList.get(k).getExpectation();
                            list.add(d);
                        } else {
                            list.add(0d);
                        }
                    }
                }
            }
            for (int j = 0; j < resourceList.size(); j++) {
                if (j == i) {
                    list.add(-1 / Parameter.getMaxAbility());
                } else {
                    list.add(0d);
                }
            }
            a[i] = list.toArray(new Double[]{});
        }
        return a;
    }

    public Double[] getB(Process process) {
        List<Resource> resourceList = process.getResourceList();
        Double[] b = new Double[resourceList.size()];
        for (int i = 0; i < resourceList.size(); i++) {
            b[i] = 0d;
        }
        return b;
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
            //x20 ~ x23都是0
            for (int j = 0; j < resourceList.size(); j++) {
                aeqSingle.add(0d);
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

    public String getFminconFunStr(Process process) {
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();

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
        for (int j = 0; j < resourceList.size(); j++) {
            Integer cost = resourceList.get(j).getCost();
            builder.append(cost).append("*x(").append(initCostResourceAllocationRate.length + j + 1).append(")+");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
