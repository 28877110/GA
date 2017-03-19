package org.zjgsu.algorithm.ga.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by wuhanqing on 2017/3/12.
 */
@Getter
@Setter
public class Activity {

    //当前节点Id
    private Integer id;

    //期望
    private Double expectation;

    private static List<Activity> list = Lists.newArrayList();

    private static Integer num = 0;

    public Activity() {
        this(1.0);
    }

    public Activity(Double expectation) {
        this.id = num++;
        this.expectation = expectation;
        list.add(this);
    }

    public static Integer getNum() {
        return num;
    }

}
