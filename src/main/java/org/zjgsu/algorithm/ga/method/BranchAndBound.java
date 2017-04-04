package org.zjgsu.algorithm.ga.method;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import fmincon.Class1;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.utils.Parameter;

/**
 * 分支定界
 * Created by wuhanqing on 2017/3/31.
 */
@Getter
@Setter
public class BranchAndBound {

    private Double bestCost;

    private Map<Integer, Integer> xValue = Maps.newHashMap();

    Class1 fminicon = new Class1();

    double[] bestResources;

    public BranchAndBound() throws MWException {}

    public Double execute(Process process) throws MWException, IOException {
        List<Resource> resourceList = process.getResourceList();
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();
        String funStr = getFminconFunStr(process);
        Double[] x0 = process.getInitCostResourceAllocationRate();
        List<Double> list = Lists.newArrayList(x0);
        for (int i = 0; i < resourceList.size(); i++) {
            Integer countLimit = resourceList.get(i).getCountLimit();
            list.add(countLimit.doubleValue());
        }
        x0 = list.toArray(new Double[]{});
        for (int i = 0; i < x0.length; i++) {
            x0[i] = 0d;
        }
        Double[][] a = getA(process);
        Double[] b = getB(process);
        Double[][] aeq = getAeq(process);
        Double[] beq = getBeq(process);
        Double[] lb = getLb(process);
        Double[] ub = getUb(process);

        Object[] resultArray = fminicon.myfmincon(4, funStr, x0, a, b, aeq, beq, lb, ub);
        MWNumericArray result0 = (MWNumericArray) resultArray[0];
        MWNumericArray result3 = (MWNumericArray) resultArray[2];
        if (result3.getInt() <= 0) {
            return null;
        }
        double[] result = result0.getDoubleData();
        double[] resources = new double[resourceList.size()];
        int index = 0;
        for (int i = resourceList.size(); i > 0; i--) {
            double resource = result[result.length - i];
            resources[index++] = resource;
        }
        List<Integer> indexList = Lists.newArrayList();
        for (int i = 0; i < resources.length; i++) {
            double resource = resources[i];
            if (resource % 1 == 0) {

            } else {
                indexList.add(i);
            }
        }
        if (indexList.size() > 0) {
            Random random = new Random();
            int one = random.nextInt(indexList.size());
            one = indexList.get(one);
            double c = Math.ceil(resources[one]);
            double f = Math.floor(resources[one]);
            Double[] lb1 = Arrays.copyOf(lb, lb.length);
            Double[] ub1 = Arrays.copyOf(ub, ub.length);
            lb1[initCostResourceAllocationRate.length + one] = c;
            ub1[initCostResourceAllocationRate.length + one] = f;
            doBranchAndBound(process, lb, ub1);
            doBranchAndBound(process, lb1, ub);
        } else {
            //整数解
            Double cost = 0d;
            for (int i = 0; i < resources.length; i++) {
                cost += resources[i] * resourceList.get(i).getCost();
            }
            if (bestCost == null || bestCost > cost) {
                bestCost = cost;
            }
            //结束该分支
        }
        return bestCost;
    }

    public void doBranchAndBound(Process process, Double[] lb, Double[] ub) throws MWException, IOException {
        List<Resource> resourceList = process.getResourceList();
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();
        String funStr = getFminconFunStr(process);
        Double[] x0 = process.getInitCostResourceAllocationRate();
        List<Double> list = Lists.newArrayList(x0);
        for (int i = 0; i < resourceList.size(); i++) {
            Integer countLimit = resourceList.get(i).getCountLimit();
            list.add(countLimit.doubleValue());
        }
        x0 = list.toArray(new Double[]{});
        Double[][] a = getA(process);
        Double[] b = getB(process);
        Double[][] aeq = getAeq(process);
        Double[] beq = getBeq(process);
        if (lb == null) {
            Double[] l = getLb(process);
            lb = new Double[l.length];
            for (int i = 0; i < l.length; i++) {
                lb[i] = l[i].doubleValue();
            }
        }
        if (ub == null) {
            Double[] u = getUb(process);
            ub = new Double[u.length];
            for (int i = 0; i < u.length; i++) {
                ub[i] = u[i].doubleValue();
            }
        }

        Object[] resultArray = fminicon.myfmincon(4, funStr, x0, a, b, aeq, beq, lb, ub);
        MWNumericArray result0 = (MWNumericArray) resultArray[0];
        MWNumericArray result3 = (MWNumericArray) resultArray[2];
        if (result3.getInt() <= 0) {
            return;
        }
        double[] result = result0.getDoubleData();
        double[] resources = new double[resourceList.size()];
        int index = 0;
        for (int i = resourceList.size(); i > 0; i--) {
            double resource = result[result.length - i];
            resources[index++] = resource;
        }
        List<Integer> indexList = Lists.newArrayList();
        for (int i = 0; i < resources.length; i++) {
            double resource = resources[i];
            if (resource % 1 == 0) {

            } else {
                indexList.add(i);
            }
        }
        if (indexList.size() > 0) {
            Random random = new Random();
            int one = random.nextInt(indexList.size());
            one = indexList.get(one);
            double f = Math.floor(resources[one]);
            double c = Math.ceil(resources[one]);
            Double[] lb1 = Arrays.copyOf(lb, lb.length);
            Double[] ub1 = Arrays.copyOf(ub, ub.length);
            lb1[initCostResourceAllocationRate.length + one] = c;
            ub1[initCostResourceAllocationRate.length + one] = f;
            doBranchAndBound(process, lb, ub1);
            doBranchAndBound(process, lb1, ub);
        } else {
            //整数解
            Double cost = 0d;
            for (int i = 0; i < resources.length; i++) {
                cost += resources[i] * resourceList.get(i).getCost();
            }
            if (bestCost == null || bestCost > cost) {
                bestResources = resources;
                bestCost = cost;
                FileUtils.write(new File("result/branchandbound/" + aeq.length), bestCost + ":  ", Charsets.UTF_8, true);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < resources.length; i++) {
                    builder.append(resources[i]).append(", ");
                }
                FileUtils.write(new File("result/branchandbound/" + aeq.length), builder.toString() + "\r\n", Charsets.UTF_8, true);
            }
            //结束该分支
        }

    }


    public Double[][] getA(Process process) {
        DecimalFormat df=new DecimalFormat(".####");
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
                            list.add(Double.valueOf(df.format(d)));
                        } else {
                            list.add(0d);
                        }
                    }
                }
            }
            for (int j = 0; j < resourceList.size(); j++) {
                if (j == i) {
                    list.add(Double.valueOf(df.format(-1 / Parameter.getMaxAbility())));
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
        List<Resource> resourceList = process.getResourceList();
        Double[] lb = new Double[initCostResourceAllocationRate.length + resourceList.size()];
        for (int i = 0; i < initCostResourceAllocationRate.length + resourceList.size(); i++) {
            lb[i] = 0d;
        }
        return lb;
    }

    public Double[] getUb(Process process) {
        Double[] initCostResourceAllocationRate = process.getInitCostResourceAllocationRate();
        List<Resource> resourceList = process.getResourceList();
        Double[] ub = new Double[initCostResourceAllocationRate.length + resourceList.size()];
        for (int i = 0; i < initCostResourceAllocationRate.length; i++) {
            ub[i] = 1d;
        }
        for (int i = 0; i < resourceList.size(); i++) {
            ub[initCostResourceAllocationRate.length + i] = resourceList.get(i).getCountLimit().doubleValue();
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
