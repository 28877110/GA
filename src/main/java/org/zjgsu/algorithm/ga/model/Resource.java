package org.zjgsu.algorithm.ga.model;

import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.utils.Parameter;

import java.util.Random;

/**
 * Created by wuhanqing on 2017/3/8.
 */
@Getter
@Setter
public class Resource {

    //当前资源Id
    private Integer id;

    private Integer countLimit;

    private Integer cost;

    private static Integer num = 0;

    public Resource() {
        Random random = new Random();
        this.id = num++;
        this.countLimit = random.nextInt(Parameter.max_count) + 1;
        this.cost = Parameter.getMaxCost();
    }

    public static Integer getNum() {
        return num;
    }
}
