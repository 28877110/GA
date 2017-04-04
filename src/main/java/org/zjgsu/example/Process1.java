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
public class Process1 {


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
        Interation c4 = new Interation(0.2);
        Node a1 = new Node(new Activity(), sequence);
        Node a2 = new Node(new Activity(), c1);
        Node a3 = new Node(new Activity(0.4), c2);
        Node a4 = new Node(new Activity(0.6), c2);
        Node a5 = new Node(new Activity(), c1);
        Node a6 = new Node(new Activity(), a2);
        Node a7 = new Node(new Activity(1.25), c4);
        Node a8 = new Node(new Activity(), a1);

        List<Logic> listA1 = Lists.newArrayList();
        listA1.add(c1);
        listA1.add(a8);
        a1.setLogicList(listA1);

        List<Logic> listC1 = Lists.newArrayList();
        listC1.add(a2);
        listC1.add(a5);
        c1.setLogicList(listC1);

        List<Logic> listC2 = Lists.newArrayList();
        listC2.add(a3);
        listC2.add(a4);
        c2.setLogicList(listC2);

        List<Logic> listA2 = Lists.newArrayList();
        listA2.add(c2);
        listA2.add(a6);
        a2.setLogicList(listA2);

        List<Logic> listA5 = Lists.newArrayList();
        listA5.add(c4);
        a5.setLogicList(listA5);

        List<Logic> listC4 = Lists.newArrayList();
        listC4.add(a7);
        c4.setLogicList(listC4);

        p2.add(a1);
        p1.add(sequence);

        List<Resource> resourceList = Lists.newArrayList();
        Resource r1 = new Resource();
        r1.setCost(20);
        r1.setCountLimit(22);
        Resource r2 = new Resource();
        r2.setCost(30);
        r2.setCountLimit(17);
        Resource r3 = new Resource();
        r3.setCost(25);
        r3.setCountLimit(19);
        Resource r4 = new Resource();
        r4.setCost(22);
        r4.setCountLimit(24);
        resourceList.add(r1);
        resourceList.add(r2);
        resourceList.add(r3);
        resourceList.add(r4);

        Integer[][] resourceActivityDealTime = new Integer[resourceList.size()][Activity.getNum()];
        resourceActivityDealTime[0] = new Integer[]{14, null, 8, null, null, 10, 12, null};
        resourceActivityDealTime[1] = new Integer[]{8, null, null, 12, 8, null, 4, 2};
        resourceActivityDealTime[2] = new Integer[]{null, 6, 5, null, 12, null, 8, 4};
        resourceActivityDealTime[3] = new Integer[]{8, 14, null, 6, null, 18, null, 3};
        Double[] initCostResourceAllocationRate = new Double[]{0d,0d,1d,0d,0d,0d,1d,1d,1d,1d,1d,0d,0d,0d,1d,0d,1d,0d,0d};

        process.setProcess(p1);
        process.setResourceList(resourceList);
        process.setActivityList(Activity.getList());
        process.setResourceActivityDealTime(resourceActivityDealTime);
        process.setInitCostResourceAllocationRate(initCostResourceAllocationRate);


        return process;
    }


}
