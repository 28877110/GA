package org.zjgsu.example;


import com.google.common.base.Charsets;
import fminimax.Class1;
import org.apache.commons.io.FileUtils;
import org.zjgsu.algorithm.ga.cross.Cross;
import org.zjgsu.algorithm.ga.fitness.Fitness;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.CloneUtil;
import org.zjgsu.algorithm.ga.utils.Parameter;
import org.zjgsu.algorithm.ga.variation.Variation;

import java.io.File;
import java.util.List;


/**
 * Hello world!
 *
 */
public class Example
{
    public static void main( String[] args ) throws Exception {

        Parameter.φ = 0.05;

        Class1 fminimax = new Class1();

        Process process = Process3.generate();

        Population population = Population.init(process);

        Fitness.compute(population, fminimax);

        List<Chromsome> chromsomeList = population.getChromsomeList();

//        Chromsome chromsome2 = new Chromsome(process, new Integer[]{4,4,4,4,6,3,2,3,4,100});
//        chromsomeList.set(0, chromsome2);
//        for (int i = 0; i < chromsomeList.size(); i++) {
//            Chromsome chromsome2 = new Chromsome(process, new Integer[]{0,0,0,0,0,0,0,0,0,100});
//            chromsomeList.set(i, chromsome2);
//        }


        Integer result = null;

        int index = 0;

        Integer[] costHistory = new Integer[Parameter.MAX_ITERATIONS];
        Double[] wHistory = new Double[Parameter.MAX_ITERATIONS];

        for (int i = 0; i < Parameter.MAX_ITERATIONS; i++) {

//            if (result == null) {
//                result = Fitness.compute(population, fminimax);
//            } else {
                result = Fitness.compute2(population, fminimax);
//            }

            Cross.cross2(population);

            Variation.variation2(population);

            population.sort();

            //精英保留
            if (Fitness.best != null) {
                Chromsome elite = (Chromsome) CloneUtil.cloneObject(Fitness.best);
                chromsomeList.set(chromsomeList.size() - 1, elite);

                int num = chromsomeList.size() / 10;
                for (int j = 1; j <= num; j++) {
                    Chromsome chromsome = new Chromsome(process);
                    chromsomeList.set(chromsomeList.size() - j - 1, chromsome);
                }
            }

            System.out.println(Fitness.bestCost);
            System.out.println(i);

//            if (result == null) {
//                index = 0;
//            } else if (result >= Fitness.bestCost) {
//                index++;
//            } else if (result < Fitness.bestCost) {
//                index = 0;
//            }

            if (i == 342) {
//                Chromsome chromsome2 = new Chromsome(process, new Integer[]{6,7,4,6});
//                chromsomeList.set(0, chromsome1);
            }

            FileUtils.write(new File("result/example/example1"), Fitness.bestCost + ", ", Charsets.UTF_8, true);


            if (index > Parameter.MAX_ATTEMPT_TIME) {
                break;
            }
        }

        FileUtils.write(new File("result/example/example1"), "GA:" + "\r\n", Charsets.UTF_8, true);
        FileUtils.write(new File("result/example/example1"), Fitness.returnStr(), Charsets.UTF_8, true);


        Fitness.reset();
    }

}
