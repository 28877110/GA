package org.zjgsu.algorithm.ga.fitness;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import fminimax.Class1;
import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.CloneUtil;
import org.zjgsu.algorithm.ga.utils.Parameter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 适应度
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Fitness {

    public static Chromsome best;

    //question1
    public static Integer bestCost;

    //question2
    public static Double bestMaxLimit;

    public static Map<String, Object[]> matlabCache = Maps.newHashMap();

    public static void reset() {
        best = null;
        bestCost = null;
        matlabCache = Maps.newHashMap();
    }

    /**
     * 计算适应度函数
     * @param population
     */
    public static Integer compute(Population population, Class1 fminimax)
            throws Exception {
        Process process = population.getProcess();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Chromsome> chromsomeList = population.getChromsomeList();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Map<Integer, Integer> xValue = chromsome.getXValue();
            Integer[] resourceCount = chromsome.getResourceCount();

            //TODO 调用fmincon
            Double[][] resourceAllocation = chromsome.getResourceAllocation();
            String funStr = chromsome.getFunStr();
            Double[] x0 = process.getInitCostResourceAllocationRate();
            Double[][] aeq = chromsome.getAeq();
            Double[] beq = chromsome.getBeq();
            Double[] lb = chromsome.getLb();
            Double[] ub = chromsome.getUb();

            Integer[] rc = chromsome.getResourceCount();
            StringBuilder builder = new StringBuilder();
            builder.append("|");
            for (int j = 0; j < rc.length; j++) {
                builder.append(rc[j]).append("|");
            }
            String key = builder.toString();
            Object[] resultArray = matlabCache.get(key);
            if (resultArray == null) {
                resultArray = fminimax.myfminimax(4, funStr, x0, aeq, beq, lb, ub);
                matlabCache.put(key, resultArray);
            }
            MWNumericArray result0 = (MWNumericArray)resultArray[0];
            MWNumericArray result2 = (MWNumericArray)resultArray[2];
            MWNumericArray result3 = (MWNumericArray)resultArray[3];
            if (result3.getInt() <= 0) {
                chromsome.setFitness(Integer.MAX_VALUE);
                continue;
            }
            double[] matlabAllocationRate = result0.getDoubleData();
            double maxCapacity = -result2.getDouble();

            //将fminimax得到的resourceAllocationRate赋值给chromsome中的resourceAllocation
            for (int j = 0; j < resourceAllocation.length; j++) {
                for (int k = 0; k < resourceAllocation[j].length; k++) {
                    if (null != resourceAllocation[j][k]) {
                        Integer index = xValue.get(j * activityList.size() + k);
                        if (null != index) {
                            resourceAllocation[j][k] = matlabAllocationRate[index - 1];
                        } else {
                            resourceAllocation[j][k] = 0d;
                        }
                    }
                }
            }

            List<Double> p = Lists.newArrayList();
            for (int j = 0; j < activityList.size(); j++) {
                double total = 0d;
                for (int k = 0; k < resourceList.size(); k++) {
                    Double denominator = 0d;
                    for (int l = 0; l < activityList.size(); l++) {
                        Integer dealTime = resourceActivityDealTime[k][l];
                        if (null == dealTime) {
                            dealTime = 0;
                        }
                        Double allocRate = resourceAllocation[k][l];
                        Double expect = activityList.get(l).getExpectation();
                        denominator += Double.valueOf(dealTime) * allocRate * expect;
                    }

                    Double allocationRate = resourceAllocation[k][j];
                    Double molecular = Double.valueOf(resourceCount[k]) * allocationRate;
                    Double result = molecular / denominator;
                    total += result;
                }
                p.add(total);
            }

            //TODO 计算minP中的最小值p > φ的情况下，资源数量对应的成本作为适应度
            Double minP = Collections.min(p);
            Integer total = null;
            if (minP > Parameter.φ) {
                total = 0;
                List<Resource> rList = process.getResourceList();
                for (int j = 0; j < rList.size(); j++) {
                    Integer count = rList.get(j).getCost() * resourceCount[j];
                    total += count;
                }
                chromsome.setFitness(total);
            } else {
                chromsome.setFitness(Integer.MAX_VALUE);
            }

            if (total == null) {
                continue;
            }
            if (null == Fitness.best) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                List<Resource> rList = process.getResourceList();
                Integer[] rCount = best.getResourceCount();
                Integer temp = 0;
                for (int j = 0; j < rList.size(); j++) {
                    temp += rList.get(j).getCost() * rCount[j];
                }
                Fitness.bestCost = temp;
            } else if (chromsome.getFitness() < Fitness.best.getFitness()) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                List<Resource> rList = process.getResourceList();
                Integer[] rCount = best.getResourceCount();
                Integer temp = 0;
                for (int j = 0; j < rList.size(); j++) {
                    temp += rList.get(j).getCost() * rCount[j];
                }
                Fitness.bestCost = temp;
                return Fitness.bestCost;
            }
        }
        return null;
    }

    /**
     * 计算适应度函数
     * @param population
     */
    public static Integer compute2(Population population, Class1 fminimax)
            throws Exception {
        Process process = population.getProcess();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Chromsome> chromsomeList = population.getChromsomeList();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Map<Integer, Integer> xValue = chromsome.getXValue();
            Integer[] resourceCount = chromsome.getResourceCount();

            Double[][] resourceAllocation = chromsome.getResourceAllocation();

            List<Double> p = Lists.newArrayList();
            for (int j = 0; j < activityList.size(); j++) {
                double total = 0d;
                for (int k = 0; k < resourceList.size(); k++) {
                    Double denominator = 0d;
                    for (int l = 0; l < activityList.size(); l++) {
                        Integer dealTime = resourceActivityDealTime[k][l];
                        if (null == dealTime) {
                            dealTime = 0;
                        }
                        Double allocRate = resourceAllocation[k][l];
                        Double expect = activityList.get(l).getExpectation();
                        denominator += Double.valueOf(dealTime) * allocRate * expect;
                    }

                    Double allocationRate = resourceAllocation[k][j];
                    Double molecular = Double.valueOf(resourceCount[k]) * allocationRate;
                    Double result = molecular / denominator;
                    total += result;
                }
                p.add(total);
            }

            //TODO 计算minP中的最小值p > φ的情况下，资源数量对应的成本作为适应度
            Double minP = Collections.min(p);

            Integer total = null;
            if (minP > Parameter.φ) {
                total = 0;
                List<Resource> rList = process.getResourceList();
                for (int j = 0; j < rList.size(); j++) {
                    Integer count = rList.get(j).getCost() * resourceCount[j];
                    total += count;
                }
                chromsome.setFitness(total);
            } else {
                chromsome.setFitness(Integer.MAX_VALUE);
            }

            if (total == null) {
                continue;
            }
            if (null == Fitness.best) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                List<Resource> rList = process.getResourceList();
                Integer[] rCount = best.getResourceCount();
                Integer temp = 0;
                for (int j = 0; j < rList.size(); j++) {
                    temp += rList.get(j).getCost() * rCount[j];
                }
                Fitness.bestCost = temp;
            } else if (chromsome.getFitness() < Fitness.best.getFitness()) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                List<Resource> rList = process.getResourceList();
                Integer[] rCount = best.getResourceCount();
                Integer temp = 0;
                for (int j = 0; j < rList.size(); j++) {
                    temp += rList.get(j).getCost() * rCount[j];
                }
                Fitness.bestCost = temp;
                return Fitness.bestCost;
            }
        }
        return null;
    }

    /**
     * 计算适应度函数
     * @param population
     */
    public static Double computeQuestion2(Population population, Class1 fminimax)
            throws Exception {
        Process process = population.getProcess();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Chromsome> chromsomeList = population.getChromsomeList();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Map<Integer, Integer> xValue = chromsome.getXValue();
            Integer[] resourceCount = chromsome.getResourceCount();

            Double[][] resourceAllocation = chromsome.getResourceAllocation();

            List<Double> p = Lists.newArrayList();
            for (int j = 0; j < activityList.size(); j++) {
                double total = 0d;
                for (int k = 0; k < resourceList.size(); k++) {
                    Double denominator = 0d;
                    for (int l = 0; l < activityList.size(); l++) {
                        Integer dealTime = resourceActivityDealTime[k][l];
                        if (null == dealTime) {
                            dealTime = 0;
                        }
                        Double allocRate = resourceAllocation[k][l];
                        Double expect = activityList.get(l).getExpectation();
                        denominator += Double.valueOf(dealTime) * allocRate * expect;
                    }

                    Double allocationRate = resourceAllocation[k][j];
                    Double molecular = Double.valueOf(resourceCount[k]) * allocationRate;
                    Double result = molecular / denominator;
                    total += result;
                }
                p.add(total);
            }

            //TODO 计算minP中的最小值p > φ的情况下，资源数量对应的成本作为适应度
            Double minP = Collections.min(p);
            chromsome.setFitness2(minP);

            Integer total = 0;
            List<Resource> rList = process.getResourceList();
            for (int j = 0; j < rList.size(); j++) {
                Integer count = rList.get(j).getCost() * resourceCount[j];
                total += count;
            }
            if (total > Parameter.getUpperCost()) {
                continue;
            }

            if (null == Fitness.best) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                Fitness.bestMaxLimit = minP;
            } else if (chromsome.getFitness2() > Fitness.best.getFitness2()) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                Fitness.bestMaxLimit = minP;
                return Fitness.bestMaxLimit;
            }
        }
        return null;
    }

    /**
     * 计算适应度函数
     * @param population
     */
    public static Double computeQuestion2MlB(Population population, Class1 fminimax)
            throws Exception {
        Process process = population.getProcess();
        List<Resource> resourceList = process.getResourceList();
        List<Activity> activityList = process.getActivityList();
        Integer[][] resourceActivityDealTime = process.getResourceActivityDealTime();
        List<Chromsome> chromsomeList = population.getChromsomeList();
        for (int i = 0; i < chromsomeList.size(); i++) {
            Chromsome chromsome = chromsomeList.get(i);
            Map<Integer, Integer> xValue = chromsome.getXValue();
            Integer[] resourceCount = chromsome.getResourceCount();

            //TODO 调用fmincon
            Double[][] resourceAllocation = chromsome.getResourceAllocation();
            String funStr = chromsome.getFunStr();
            Double[] x0 = process.getInitCostResourceAllocationRate();
            Double[][] aeq = chromsome.getAeq();
            Double[] beq = chromsome.getBeq();
            Double[] lb = chromsome.getLb();
            Double[] ub = chromsome.getUb();

            Integer[] rc = chromsome.getResourceCount();
            StringBuilder builder = new StringBuilder();
            builder.append("|");
            for (int j = 0; j < rc.length; j++) {
                builder.append(rc[j]).append("|");
            }
            String key = builder.toString();
            Object[] resultArray = matlabCache.get(key);
            if (resultArray == null) {
                resultArray = fminimax.myfminimax(4, funStr, x0, aeq, beq, lb, ub);
                matlabCache.put(key, resultArray);
            }
            MWNumericArray result0 = (MWNumericArray)resultArray[0];
            MWNumericArray result2 = (MWNumericArray)resultArray[2];
            MWNumericArray result3 = (MWNumericArray)resultArray[3];
            if (result3.getInt() <= 0) {
                chromsome.setFitness(Integer.MAX_VALUE);
                continue;
            }
            double[] matlabAllocationRate = result0.getDoubleData();
            double maxCapacity = -result2.getDouble();

            //将fminimax得到的resourceAllocationRate赋值给chromsome中的resourceAllocation
            for (int j = 0; j < resourceAllocation.length; j++) {
                for (int k = 0; k < resourceAllocation[j].length; k++) {
                    if (null != resourceAllocation[j][k]) {
                        Integer index = xValue.get(j * activityList.size() + k);
                        if (null != index) {
                            resourceAllocation[j][k] = matlabAllocationRate[index - 1];
                        } else {
                            resourceAllocation[j][k] = 0d;
                        }
                    }
                }
            }

            List<Double> p = Lists.newArrayList();
            for (int j = 0; j < activityList.size(); j++) {
                double total = 0d;
                for (int k = 0; k < resourceList.size(); k++) {
                    Double denominator = 0d;
                    for (int l = 0; l < activityList.size(); l++) {
                        Integer dealTime = resourceActivityDealTime[k][l];
                        if (null == dealTime) {
                            dealTime = 0;
                        }
                        Double allocRate = resourceAllocation[k][l];
                        Double expect = activityList.get(l).getExpectation();
                        denominator += Double.valueOf(dealTime) * allocRate * expect;
                    }

                    Double allocationRate = resourceAllocation[k][j];
                    Double molecular = Double.valueOf(resourceCount[k]) * allocationRate;
                    Double result = molecular / denominator;
                    total += result;
                }
                p.add(total);
            }

            //TODO 计算minP中的最小值p > φ的情况下，资源数量对应的成本作为适应度
            Double minP = Collections.min(p);
            chromsome.setFitness2(minP);

            Integer total = 0;
            List<Resource> rList = process.getResourceList();
            for (int j = 0; j < rList.size(); j++) {
                Integer count = rList.get(j).getCost() * resourceCount[j];
                total += count;
            }
            if (total > Parameter.getUpperCost()) {
                continue;
            }

            if (null == Fitness.best) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                Fitness.bestMaxLimit = minP;
            } else if (chromsome.getFitness2() > Fitness.best.getFitness2()) {
                Fitness.best = (Chromsome) CloneUtil.cloneObject(chromsome);
                Fitness.bestMaxLimit = minP;
                return Fitness.bestMaxLimit;
            }
        }
        return null;
    }

    public static String returnStr() {
        StringBuilder builder = new StringBuilder();
        if (bestCost != null) {
            builder.append(bestCost);
            builder.append(":  ");
        }
        if (best != null) {
            Integer[] resourceCount = best.getResourceCount();
            builder.append("[");
            for (int i = 0; i < resourceCount.length; i++) {
                builder.append(resourceCount[i]).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length() - 1);
            builder.append("]");
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public static String returnQ1Str() {
        StringBuilder builder = new StringBuilder();
        if (bestCost != null) {
            builder.append(bestCost);
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public static String returnQ2Str() {
        StringBuilder builder = new StringBuilder();
        if (bestMaxLimit != null) {
            builder.append(bestMaxLimit);
            builder.append(":  ");
        }
        if (best != null) {
            Integer[] resourceCount = best.getResourceCount();
            builder.append("[");
            for (int i = 0; i < resourceCount.length; i++) {
                builder.append(resourceCount[i]).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length() - 1);
            builder.append("]");
            builder.append("\r\n");
        }
        return builder.toString();
    }
}
