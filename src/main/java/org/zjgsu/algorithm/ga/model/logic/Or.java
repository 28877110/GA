package org.zjgsu.algorithm.ga.model.logic;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.enums.LogicEnum;
import org.zjgsu.algorithm.ga.model.Activity;

import java.util.List;

/**
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Or extends Logic {

    public Or() {
        this.setLogicEnum(LogicEnum.Or);
    }

}
