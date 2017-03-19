package org.zjgsu.algorithm.ga.model.logic;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.enums.LogicEnum;

/**
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Interation extends Logic {

    private Logic logic;

    public Interation(double excuteRate) {
        this.excuteRate = excuteRate;
        this.setLogicEnum(LogicEnum.Interation);
    }

}
