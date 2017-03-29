package org.zjgsu.algorithm.ga.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.logic.*;
import org.zjgsu.algorithm.ga.utils.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by wuhanqing on 2017/3/12.
 */
@Getter
@Setter
public class Process {

    //节点列表
    private List<Logic> process;

    private List<Activity> activityList;

    //资源列表
    private List<Resource> resourceList;

    //资源与活动之间的关系及处理时间均值
    private Integer[][] resourceActivityDealTime;

    //面向成本的初始化任务分配率
    private Double[] initCostResourceAllocationRate;

    /**
     * 生成业务过程实例
      */
    public void generate() {

        process = Lists.newArrayList();
        resourceList = Lists.newArrayList();
        activityList = Lists.newArrayList();

        Activity activity = new Activity();
        Node node = new Node(activity);
        process.add(node);

        Random random = new Random();

        //生成过程实例
        while (Activity.getNum() < Parameter.activity_num) {
            double pSequence = Parameter.P_SEQUENCE;
            double pInteration = pSequence + Parameter.P_INTERATION;
            double pAnd = pInteration + Parameter.P_AND;
            double pOr = pAnd + Parameter.P_OR;

            double p = random.nextDouble();
            if (p <= pSequence) {//顺序基本结构
                sequence(process);
            } else if (p > pSequence && p <= pInteration) {//循环基本结构
                interation(process);
            } else if (p > pInteration && p <= pAnd) {//并发基本结构
                and(process);
            } else {//选择基本结构
                or(process);
            }
        }
        //生成资源实例
        while (resourceList.size() < Parameter.resource_num) {
            Resource resource = new Resource();
            resourceList.add(resource);
        }
        //初始化资源与活动之间处理时间
        resourceActivityDealTime = new Integer[Resource.getNum()][Activity.getNum()];
        for (int i = 0; i < Activity.getNum(); i++) {
            List<Integer> list = Lists.newArrayList();
            for (int j = 0; j < Resource.getNum(); j++) {
                Integer dealTime = random.nextInt(Parameter.max_deal_time) + 1;
                list.add(dealTime);
            }

            /**
             * 资源数>2时，资源可处理活动数 > 2, <资源数-1
             * 资源数=2时，资源可处理活动数 > 0, <=2
             */
            Integer num;
            if (Resource.getNum() > 2) {
                num = 2 + random.nextInt(Resource.getNum() - 2);
            } else if (Resource.getNum() == 2) {
                num = random.nextInt(2) + 1;
            } else {
                num = Resource.getNum();
            }
            Integer cantDeal = Resource.getNum() - num;
            for (int j = 0; j < cantDeal; j++) {
                list.set(j, null);
            }
            Collections.shuffle(list);
            for (int j = 0; j < Resource.getNum(); j++) {
                resourceActivityDealTime[j][i] = list.get(j);
            }
        }
        activityList = Activity.getList();

        //面向成本的初始化任务分配率
        List<Double> initCostResourceAllocationRate = new ArrayList<>(activityList.size() * resourceList.size());
        for (int i = 0; i < activityList.size() * resourceList.size(); i++) {
            initCostResourceAllocationRate.add(null);
        }

        for (int i = 0; i < Activity.getNum(); i++) {
            int index = -1;
            int dealTime = Integer.MAX_VALUE;
            for (int j = 0; j < Resource.getNum(); j++) {
                if (null != resourceActivityDealTime[j][i]) {
                    if (resourceActivityDealTime[j][i] < dealTime) {
                        dealTime = resourceActivityDealTime[j][i];
                        index = j;
                    }
                } else {
                    initCostResourceAllocationRate.set(i + j * activityList.size(), null);
                }
            }
            for (int j = 0; j < Resource.getNum(); j++) {
                if (index == j) {
                    initCostResourceAllocationRate.set(i + j * activityList.size(), 1d);
                } else if (null == resourceActivityDealTime[j][i]) {
                    initCostResourceAllocationRate.set(i + j * activityList.size(), null);
                } else {
                    initCostResourceAllocationRate.set(i + j * activityList.size(), 0d);
                }
            }
        }
        initCostResourceAllocationRate.removeAll(Collections.singleton(null));
        this.initCostResourceAllocationRate = initCostResourceAllocationRate.toArray(new Double[]{});
    }

    /**
     * 顺序操作
     * @param process
     */
    private void sequence(List<Logic> process) {
        Random random = new Random();
        Integer index = random.nextInt(process.size());
        Logic logic = process.get(index);
        if (logic instanceof Sequence) {
            //如果为顺序结构，则在index后添加一个logic元素
            Node node = new Node(new Activity());
            process.add(index + 1, node);
        } else if (logic instanceof And) {
            //当并发结构时，递归sequence(),来进一步寻找新增顺序结构的位置
            List<Logic> subProcess = logic.getLogicList();
            sequence(subProcess);
        } else if (logic instanceof Or) {
            List<Logic> subProcess = logic.getLogicList();
            sequence(subProcess);
        } else if (logic instanceof Interation) {
            //TODO
            Logic l = ((Interation) logic).getLogic();
            if (l instanceof Node) {
                Sequence sequence = new Sequence();
                Node node1 = (Node) l;
                Node node2 = new Node(new Activity());
                List<Logic> logicList = Lists.newArrayList();
                logicList.add(node1);
                logicList.add(node2);
                sequence.setLogicList(logicList);
                ((Interation) logic).setLogic(sequence);
            } else {
                List<Logic> logicList = l.getLogicList();
                sequence(logicList);
            }
        } else if (logic instanceof Node) {
            Sequence sequence = new Sequence();
            Node node1 = (Node) logic;
            Node node2 = new Node(new Activity());
            List<Logic> logicList = Lists.newArrayList();
            logicList.add(node1);
            logicList.add(node2);
            sequence.setLogicList(logicList);
            process.set(index, sequence);
        }

    }

    /**
     * 循环操作
     * @param process
     */
    private void interation(List<Logic> process) {
        Random random = new Random();
        Integer index = random.nextInt(process.size());
        Logic logic = process.get(index);
        if (logic instanceof Sequence) {
            List<Logic> logicList = logic.getLogicList();
            interation(logicList);
        } else if (logic instanceof And) {
            List<Logic> logicList = logic.getLogicList();
            interation(logicList);
        } else if (logic instanceof Or) {
//            List<Logic> logicList = logic.getLogicList();
//            interation(logicList);
        } else if (logic instanceof Interation) {
            //循环内部不能嵌套循环，防止实例过于复杂
            return;
        } else if (logic instanceof Node) {
            double excuteRate = random.nextDouble();
            Interation interation = new Interation(excuteRate);
            Node node = (Node) logic;
            node.setExcuteRate(excuteRate);
            node.getActivity().setExpectation(excuteRate);
            interation.setLogic(node);
            process.set(index, interation);
            if (index + 1 == process.size()) {
                Node node1 = new Node(new Activity(1 - excuteRate), 1 - excuteRate);
                process.add(index + 1, node1);
            } else if (process.get(index + 1) instanceof Node) {
                process.get(index + 1).setExcuteRate(1 - excuteRate);
                ((Node) process.get(index + 1)).getActivity().setExpectation(1 - excuteRate);
            }
        }
    }

    /**
     * And操作
     * @param process
     */
    private void and(List<Logic> process) {
        Random random = new Random();
        Integer index = random.nextInt(process.size());
        Logic logic = process.get(index);
        if (logic instanceof Sequence) {
            List<Logic> logicList = logic.getLogicList();
            and(logicList);
        } else if (logic instanceof And) {
            List<Logic> logicList = logic.getLogicList();
            and(logicList);
        } else if (logic instanceof Or) {
//            List<Logic> logicList = logic.getLogicList();
//            and(logicList);
        } else if (logic instanceof Interation) {
//            Logic l = ((Interation) logic).getLogic();
//            if (l instanceof Node) {
//                And and = new And();
//                Node node1 = (Node) l;
//                Node node2 = new Node(new Activity(l.getExcuteRate())); //TODO Mark
//                List<Logic> logicList = Lists.newArrayList();
//                logicList.add(node1);
//                logicList.add(node2);
//                and.setLogicList(logicList);
//                ((Interation) logic).setLogic(and);
//            } else {
//                List<Logic> logicList = l.getLogicList();
//                and(logicList);
//            }
        } else if (logic instanceof Node) {
            And and = new And();
            Node node1 = (Node) logic;
            Node node2 = new Node(new Activity(logic.getExcuteRate())); //TODO Mark
            List<Logic> logicList = Lists.newArrayList();
            logicList.add(node1);
            logicList.add(node2);
            and.setLogicList(logicList);
            process.set(index, and);
        }
    }

    /**
     * Or操作
     * @param process
     */
    private void or(List<Logic> process) {
        Random random = new Random();
        Integer index = random.nextInt(process.size());
        Logic logic = process.get(index);
        if (logic instanceof Sequence) {
            List<Logic> logicList = logic.getLogicList();
            or(logicList);
        } else if (logic instanceof And) {
//            List<Logic> logicList = logic.getLogicList();
//            or(logicList);
        } else if (logic instanceof Or) {
//            List<Logic> logicList = logic.getLogicList();
//            or(logicList);
        } else if (logic instanceof Interation) {
//            Logic l = ((Interation) logic).getLogic();
//            if (l instanceof Node) {
//                double p1 = random.nextDouble();
//                double p2 = 1 - p1;
//                Or or = new Or();
//                Node node1 = (Node) l;
//                node1.setExcuteRate(p1);
//                node1.getActivity().setExpectation(p1);
//                Node node2 = new Node(new Activity(p2), p2);
//                List<Logic> logicList = Lists.newArrayList();
//                logicList.add(node1);
//                logicList.add(node2);
//                or.setLogicList(logicList);
//                ((Interation) logic).setLogic(or);
//            } else {
//                List<Logic> logicList = l.getLogicList();
//                or(logicList);
//            }
        } else if (logic instanceof Node) {
            double p1 = random.nextDouble();
            double p2 = 1 - p1;
            Or or = new Or();
            Node node1 = (Node) logic;
            node1.setExcuteRate(p1);
            node1.getActivity().setExpectation(p1);
            Node node2 = new Node(new Activity(p2), p2);
            List<Logic> logicList = Lists.newArrayList();
            logicList.add(node1);
            logicList.add(node2);
            or.setLogicList(logicList);
            process.set(index, or);
        }
    }

}
