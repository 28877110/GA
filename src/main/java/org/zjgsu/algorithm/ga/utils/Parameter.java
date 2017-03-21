package org.zjgsu.algorithm.ga.utils;

/**
 * Created by wuhanqing on 2017/3/11.
 */
public class Parameter {

    public static Integer POPULATION_SIZE = 20;

    public static Integer MAX_ITERATIONS = 1000;

    //分别表示嵌套生成时是顺序、循环、并发和选择基本结构的概率, 4者之和为1
    public static double P_SEQUENCE = 0.3;
    public static double P_INTERATION = 0.1;
    public static double P_AND = 0.3;
    public static double P_OR = 0.3;

    //均匀分布上下限参数
    public static double α = 1;
    public static double β = 1;

    //二项分布参数
    public static double p = 1;
    public static double n = 1;

    //活动数量
    public static int activity_num = 10;
    //资源数量
    public static int resource_num = 10;


    public static int max_cost = 40;
    public static int max_deal_time = 20;

    public static double φ = 0.5;

}
