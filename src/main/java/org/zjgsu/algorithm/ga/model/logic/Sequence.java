package org.zjgsu.algorithm.ga.model.logic;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.enums.LogicEnum;
import org.zjgsu.algorithm.ga.model.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Sequence extends Logic {

    public Sequence() {
        this.excuteRate = 1.0;
        this.setLogicEnum(LogicEnum.Sequence);
        this.setLogicList(new ArrayList<Logic>());
    }

}
