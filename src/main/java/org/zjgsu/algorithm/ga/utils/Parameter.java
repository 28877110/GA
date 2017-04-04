package org.zjgsu.algorithm.ga.utils;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * Created by wuhanqing on 2017/3/11.
 */
public class Parameter {

    public static Integer POPULATION_SIZE = 40;

    public static Integer MAX_ATTEMPT_TIME = 1500;

    public static Double VARIATION_PROB = 0.4;
    public static Double CROSS_PROB = 0.7;
    public static Double BIASES = 0.7;
    public static Double MIGRANT = 0.1;

//    public static Integer MAX_ITERATIONS = 20 * 5000 / POPULATION_SIZE; // MAX_ITERATIONS * POPULATION_SIZE
    public static Integer MAX_ITERATIONS = 9000; // MAX_ITERATIONS * POPULATION_SIZE

    //均匀分布上下限参数
    public static double α = 1;
    public static double β = 1;

    //二项分布参数
    public static double p = 1;
    public static double n = 1;

    //活动数量
    public static int ACTIVITY_NUM = 10;
    //资源数量
    //public static int resource_num = 3;


    public static int max_count = 40;

    public static Double φ;

    public static Integer LIMIT_COST;

    //分别表示嵌套生成时是顺序、循环、并发和选择基本结构的概率, 4者之和为1
    //Parameter1
    public static double P_SEQUENCE = 0.5;
    public static double P_AND = 0.3;
    public static double P_OR = 0.2;
    public static double P_INTERATION = 0.1;

    //每次新增活动时，生成顺序，And，Or的数量，Parameter2
    public static int numActivityInSequenceAndOr() {
        Integer num = binomialDistribution(6, 0.2);
        if (num == 0 || num == 1) { //(不为0的二项分布随机整数)
            num = numActivityInSequenceAndOr();
        }
        return num;
    }

    //带有反馈活动的循环Parameter3
    public static double LOOP_FEEDBACK_PROB = 0.2;

    //TODO Parameter4

    //退出循环的概率Parameter5
    public static double getExitLoopProb() {
        Random random = new Random();
        Double p = random.nextDouble() * 0.19 + 0.8; // 0.8 - 0.99
        return p;
    }

    //活动可供选择的资源数Parameter6
    public static int alternativeResourcesForActivityNum() {
        Integer num = binomialDistribution(2, 0.2);
        if (num == 0) { //(不为0的二项分布随机整数)
            num = alternativeResourcesForActivityNum();
        }
        return num;
    }

    //资源种类数Parameter7（根据活动数量求一定比例的资源数量）
    public static int getResourceNum() {
        Random random = new Random();
        Double p = random.nextDouble() * 0.4 + 0.2;
        Double resource = ACTIVITY_NUM * p;

        return (int) Math.ceil(resource);
    }

    //活动处理时间Parameter9
    public static int getMaxActivityDealTime() {
        Random random = new Random();
        return random.nextInt(15) + 1; //1 - 15的均匀分布
    }
    //成本参数范围Parameter10
    public static int getMaxCost() {
        Random random = new Random();
        return random.nextInt(60) + 10; //10 - 70的均匀分布
    }

    //成本参数范围Parameter11
    public static double getMaxAbility() {
        if (φ == null) {
            Random random = new Random();
//            φ = random.nextDouble() * (20 / 8) + (5 / 8); // 5/8 - 25/8的均匀分布
            φ = random.nextDouble() + 0.5; // 0.5 + 1.5的均匀分布
        }
//        return φ;
        return φ;
    }

    //成本参数范围Parameter12
    public static double getUpperCost() {
        if (LIMIT_COST == null) {
            Random random = new Random();
//            φ = random.nextDouble() * (20 / 8) + (5 / 8); // 5/8 - 25/8的均匀分布
            LIMIT_COST = random.nextInt(800) + 1 + 400; // 0.5 + 1.5的均匀分布
        }
        return LIMIT_COST;
    }

    //求排列组合数方法
    public static Integer c(int n, int m) {
        if (m > n) {
            return null;
        }
        if (m == 0 || m == n) {
            return 1;
        }
        int temp1 = 1;
        for (int i = m; i > 0; i--) {
            temp1 = n * temp1;
            n--;
        }
        int temp2 = 1;
        while (m != 0) {
            temp2 = m * temp2;
            m--;
        }
        return temp1 / temp2;
    }

    //二项分布随机数方法
    public static Integer binomialDistribution(int n, double p) {
        List<Double> probList = Lists.newArrayList();
        double index = 0;
        for (int i = 0; i <= n; i++) {
            Integer num = c(n, i);

            double temp1 = 1;
            for (int j = 0; j < n - i; j++) {
                temp1 = (1 - p) * temp1;
            }
            double temp2 = 1;
            for (int j = 0; j < i; j++) {
                temp2 = p * temp2;
            }
            Double prob = num * temp1 * temp2;
            index += prob;
            probList.add(index);
        }
        Random random = new Random();
        double d = random.nextDouble();
        for (int i = 0; i < probList.size(); i++) {
            if (i == 0) {
                if (d > 0 && d < probList.get(i)) {
                    return i;
                }
            } else {
                if (d > probList.get(i - 1) && d < probList.get(i)) {
                    return i;
                }
            }
        }
        return null;
    }

}
