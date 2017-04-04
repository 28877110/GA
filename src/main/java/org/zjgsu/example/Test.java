package org.zjgsu.example;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by wuhanqing on 2017/4/4.
 */
public class Test {

    public static void main(String[] args) {
        String str = "0.4659348081597093\n" +
                "0.47651001030186513\n" +
                "0.48725350151299474\n" +
                "0.5073512355516834\n" +
                "0.491881300956764\n" +
                "0.47651001030186513\n" +
                "0.4783943469400312\n" +
                "0.48725350151299474\n" +
                "0.47651001030186513\n" +
                "0.48664302185569636";
        String[] s = str.split("\n");
        List<String> sList = Lists.newArrayList(s);
        Collections.shuffle(sList);
        for (int i = 0; i < sList.size(); i++) {
            System.out.println(sList.get(i));
        }
    }

}
