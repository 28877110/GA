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
public class Process2 {

    public static Process generate() {
        Process process = new Process();
        List<Logic> p1 = Lists.newArrayList();
        List<Logic> p2 = Lists.newArrayList();
        Sequence sequence = new Sequence();
        sequence.setExcuteRate(1);
        sequence.setLogicEnum(LogicEnum.Sequence);
        sequence.setLogicList(p2);

        And c1 = new And();
        Or c2 = new Or();
        Interation c6 = new Interation(0.2);
        Or c7 = new Or();

        Node a1 = new Node(new Activity(), sequence);
        Node a2 = new Node(new Activity(), c1);
        Node a3 = new Node(new Activity(1.25), c6);
        Node a4 = new Node(new Activity(0.2), c2);
        Node a5 = new Node(new Activity(0.3), c2);
        And c3 = new And(0.5);
        Node a4_ = new Node(new Activity(0.5), c3);
        Node a5_ = new Node(new Activity(0.5), c3);
        Node a6 = new Node(new Activity(1.25), c6);
        Node a7 = new Node(new Activity(1.0), a2);
        Node a8 = new Node(new Activity(0.25), c7);
        Node a9 = new Node(new Activity(1.0), c7);
        Node a10 = new Node(new Activity(1.25), c7);
        Node a11 = new Node(new Activity(1.0), c6);
        Node a12 = new Node(new Activity(1.0), a1);


        List<Resource> resourceList = Lists.newArrayList();
        Resource r1 = new Resource();
        r1.setCost(12);
        r1.setCountLimit(50);
        Resource r2 = new Resource();
        r2.setCost(12);
        r2.setCountLimit(50);
        Resource r3 = new Resource();
        r3.setCost(10);
        r3.setCountLimit(50);
        Resource r4 = new Resource();
        r4.setCost(12);
        r4.setCountLimit(50);
        Resource r5  = new Resource();
        r5.setCost(11);
        r5.setCountLimit(50);
        resourceList.add(r1);
        resourceList.add(r2);
        resourceList.add(r3);
        resourceList.add(r4);

        p2.add(a1);
        p1.add(sequence);

        Integer[][] resourceActivityDealTime = new Integer[resourceList.size()][Activity.getNum()];
        resourceActivityDealTime[0] = new Integer[]{7, null, null, null, 5, 8, null, 2, 4, 5, null, 6, null, 5};
        resourceActivityDealTime[1] = new Integer[]{6, 3, 5, 2, null, 8, 1, null, null, 3, 2};
        resourceActivityDealTime[2] = new Integer[]{null, 2, 7, 2, null, null, 8, 1, null, null, 2, null, 2};
        resourceActivityDealTime[3] = new Integer[]{null, null, 6, 2, null, 6, 7, 1, null, 3, 2, null, 2};
        resourceActivityDealTime[4] = new Integer[]{null, null, 7, 3, 5, null, 8, 1, 2, null, 1, 8, 3, 5};
        Double[] initCostResourceAllocationRate = new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,  1.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,  1.0,0.0,1.0,1.0,1.0,0.0,1.0,   0.0,0.0,1.0,0.0,0.0,1.0,0.0,0.0,   0.0,0.0,1.0,0.0,0.0,1.0,1.0,0.0,0.0,1.0};

        process.setProcess(p1);
        process.setResourceList(resourceList);
        process.setActivityList(Activity.getList());
        process.setResourceActivityDealTime(resourceActivityDealTime);
        process.setInitCostResourceAllocationRate(initCostResourceAllocationRate);

        return process;
    }

}
