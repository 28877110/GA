package org.zjgsu.algorithm.ga.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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

    private static Map<Integer, Activity> idToActivity = Maps.newHashMap();

    private static List<Activity> list = Lists.newArrayList();

    private static Integer num = 0;

    public Activity() {
        this(1.0);
    }

    public Activity(Double expectation) {
        this.id = num++;
        this.expectation = expectation;
        list.add(this);
        idToActivity.put(id, this);
    }

    public static Integer getNum() {
        return num;
    }

    public static List<Activity> getList() {
        return list;
    }

    public static Activity getActivity(Integer id) {
        return idToActivity.get(id);
    }
}
