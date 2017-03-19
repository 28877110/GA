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
public abstract class Logic {

    private List<Logic> logicList;

    protected double excuteRate;

    private LogicEnum logicEnum;

}
