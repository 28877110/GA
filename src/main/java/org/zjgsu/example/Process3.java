package org.zjgsu.example;

import com.google.common.collect.Lists;
import org.zjgsu.algorithm.ga.enums.LogicEnum;
import org.zjgsu.algorithm.ga.model.Activity;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.Resource;
import org.zjgsu.algorithm.ga.model.logic.*;

import java.util.List;

/**
 * Created by wuhanqing on 2017/4/4.
 */
public class Process3 {

    public static Process generate() {

        Process process = new Process();
        List<Logic> p1 = Lists.newArrayList();
        List<Logic> p2 = Lists.newArrayList();
        Sequence sequence = new Sequence();
        sequence.setExcuteRate(1);
        sequence.setLogicEnum(LogicEnum.Sequence);
        sequence.setLogicList(p2);

        And c1 = new And();
        Interation c3 = new Interation(0.08);
        Interation c5 = new Interation(0.04);
        Interation c7 = new Interation(0.06);

        Node a1 = new Node(new Activity(), c1);
        Node a2 = new Node(new Activity(), a1);
        Node a3 = new Node(new Activity(), a2);
        Node a4 = new Node(new Activity(1/0.92), c3);
        Node a5 = new Node(new Activity(0.08), a4);
        Node a6 = new Node(new Activity(), c1);
        Node a7 = new Node(new Activity(), a6);
        Node a8 = new Node(new Activity(), a7);
        Node a9 = new Node(new Activity(1/0.96), a8);
        Node a10 = new Node(new Activity(0.04), a9);
        Node a11 = new Node(new Activity(), a9);

        Node a12 = new Node(new Activity(), c1);
        Node a13 = new Node(new Activity(), a12);

        Node a14 = new Node(new Activity(), c1);
        Node a15 = new Node(new Activity(1/0.94), a14);
        Node a16 = new Node(new Activity(0.06), a15);


        List<Resource> resourceList = Lists.newArrayList();
        Resource r1 = new Resource();
        r1.setCost(40);
        r1.setCountLimit(20);
        Resource r2 = new Resource();
        r2.setCost(60);
        r2.setCountLimit(20);
        Resource r3 = new Resource();
        r3.setCost(30);
        r3.setCountLimit(20);
        Resource r4 = new Resource();
        r4.setCost(40);
        r4.setCountLimit(20);
        Resource r5  = new Resource();
        r5.setCost(35);
        r5.setCountLimit(20);
        Resource r6  = new Resource();
        r6.setCost(40);
        r6.setCountLimit(20);
        Resource r7  = new Resource();
        r7.setCost(50);
        r7.setCountLimit(20);
        Resource r8  = new Resource();
        r8.setCost(60);
        r8.setCountLimit(20);
        Resource r9  = new Resource();
        r9.setCost(40);
        r9.setCountLimit(20);
        Resource r10  = new Resource();
        r10.setCost(0);
        r10.setCountLimit(500);
        resourceList.add(r1);
        resourceList.add(r2);
        resourceList.add(r3);
        resourceList.add(r4);
        resourceList.add(r5);
        resourceList.add(r6);
        resourceList.add(r7);
        resourceList.add(r8);
        resourceList.add(r9);
        resourceList.add(r10);

        Integer[][] resourceActivityDealTime = new Integer[resourceList.size()][Activity.getNum()];
        resourceActivityDealTime[0] = new Integer[]{80, null, null, null, null, 90, null, null, null, null, null, 30, null, null, null, null};
        resourceActivityDealTime[1] = new Integer[]{60, null, null, null, null, 40, null, null, null, null, null, 25, null, null, null, null};
        resourceActivityDealTime[2] = new Integer[]{null, 100, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        resourceActivityDealTime[3] = new Integer[]{null, 80, null, null, null, null, 50, null, null, null, null, null, null, null, null, null};
        resourceActivityDealTime[4] = new Integer[]{null, null, 70, null, 10, null, null, 60, null, 12, null, null, null, 150, null, 16};
        resourceActivityDealTime[5] = new Integer[]{null, null, 60, null, 10, null, null, 40, null, 12, null, null, null, 130, null, 16};
        resourceActivityDealTime[6] = new Integer[]{null, null, 50, null, null, null, null, 30, null, null, null, null, null, null, null, null};
        resourceActivityDealTime[7] = new Integer[]{null, null, null, null, null, null, null, null, null, null, 60, null, null, null, null, null};
        resourceActivityDealTime[8] = new Integer[]{null, null, null, null, null, null, null, null, null, null, null, null, 80, null, null, null};
        resourceActivityDealTime[9] = new Integer[]{null, null, null, 240, null, null, null, null, 240, null, null, null, null, null, 240, null};
        Double[] initCostResourceAllocationRate =
                new Double[]{
                0.5, 0.0, 1.0,   0.5, 1.0, 0.0,   0.7222,   0.2778, 1.0,   0.0, 1.0, 0.0, 1.0, 0.8447, 1.0,
                        0.7394, 0.0, 0.0, 0.0, 0.1553, 0.0,    0.2606, 1.0,   1.0,   1.0,   1.0, 1.0, 1.0
                };

        p2.add(c1);
        p1.add(sequence);

        process.setProcess(p1);
        process.setResourceList(resourceList);
        process.setActivityList(Activity.getList());
        process.setResourceActivityDealTime(resourceActivityDealTime);
        process.setInitCostResourceAllocationRate(initCostResourceAllocationRate);

        return process;
    }

}
