package org.zjgsu.algorithm.ga.model.logic;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.enums.LogicEnum;
import org.zjgsu.algorithm.ga.model.Activity;

/**
 * Created by wuhanqing on 2017/3/13.
 */
@Getter
@Setter
public class Node extends Logic {

    private Activity activity;

    public Node(Activity activity) {
        this.activity = activity;
        this.excuteRate = 1.0;
        activity.setExpectation(1.0);
        this.setLogicEnum(LogicEnum.And);
    }

    public Node(Activity activity, double excuteRate) {
        this.activity = activity;
        this.excuteRate = excuteRate;
        activity.setExpectation(excuteRate);
        this.setLogicEnum(LogicEnum.And);
    }

}
