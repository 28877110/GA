package org.zjgsu;


import com.google.common.base.Charsets;
import fminimax.Class1;
import org.apache.commons.io.FileUtils;
import org.springframework.util.SerializationUtils;
import org.zjgsu.algorithm.ga.cross.Cross;
import org.zjgsu.algorithm.ga.fitness.Fitness;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.population.Chromsome;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.CloneUtil;
import org.zjgsu.algorithm.ga.utils.Parameter;
import org.zjgsu.algorithm.ga.utils.ParameterSelection;
import org.zjgsu.algorithm.ga.variation.Variation;

import java.io.File;
import java.util.List;


/**
 * Hello world!
 *
 */
public class Question2 {
    public static void main( String[] args ) throws Exception {

        String name = "example1";
        int size = 50;
        String file = "result/parameter/Q2/" + size + "/" + name;
        ParameterSelection ps = new ParameterSelection();
        Object[][] talbe = ps.getTable();
        for (int i = 0; i < 25; i++) {
            FileUtils.write(new File(file), name + "\r\n", Charsets.UTF_8, true);
            FileUtils.write(new File(file), "Parameters: " + (i + 1) + "\r\n", Charsets.UTF_8, true);
            FileUtils.write(new File(file), "upper limit: " + Parameter.getUpperCost() + "\r\n", Charsets.UTF_8, true);

            for (int j = 0; j < 10; j++) {
                Object[] parameters = talbe[i];
                Parameter.POPULATION_SIZE = (Integer) parameters[0];
                Parameter.CROSS_PROB = (Double) parameters[1];
                Parameter.BIASES = (Double) parameters[2];
                Parameter.VARIATION_PROB = (Double) parameters[3];
                Parameter.MAX_ATTEMPT_TIME = (Integer) parameters[4];
                Parameter.MIGRANT = (Double) parameters[5];

//            excute(10, "example1" + (i + 1));
//            FileUtils.write(new File(file), name + "\r\n", Charsets.UTF_8, true);
                excute(size, "example1", file);
            }
        }
    }

    public static void excute(Integer size, String name, String file) throws Exception {
        Class1 fminimax = new Class1();

        byte[] processByte = FileUtils.readFileToByteArray(new File("data/" + size + "/" + name));
        Process process = (Process) SerializationUtils.deserialize(processByte);

        Population population = Population.init(process);

        Fitness.computeQuestion2(population, fminimax);

        List<Chromsome> chromsomeList = population.getChromsomeList();

        Double result = null;

        int index = 0;
        int index2 = 0;

        for (int i = 0; i < 3000; i++) {

            Cross.crossQ2(population);

            Variation.variationQ2(population);

            if (index2 > 200) {
                Fitness.computeQuestion2(population, fminimax);
                index2 = 0;
            } else {
                result = Fitness.computeQuestion2(population, fminimax);
                index2++;
            }
//            result = population.getChromsomeList().get(0).getFitness();
            population.sort();

            //精英保留
            if (Fitness.best != null) {
                Chromsome elite = (Chromsome) CloneUtil.cloneObject(Fitness.best);
                chromsomeList.set(0, elite);

                int num = chromsomeList.size() / 10;
                for (int j = 1; j <= num; j++) {
                    Chromsome chromsome = new Chromsome(process);
                    chromsomeList.set(chromsomeList.size() - j, chromsome);
                }
            }

            System.out.println(Fitness.bestMaxLimit);
            System.out.println(i);

            if (result == null) {
                index++;
            } else {
//                FileUtils.write(new File(file), Fitness.returnQ2Str(), Charsets.UTF_8, true);
                index = 0;
                index2 = 0;
            }

            if (index > 2000) {
//                break;
            }
        }

        FileUtils.write(new File(file), String.valueOf(Fitness.bestMaxLimit), Charsets.UTF_8, true);
        FileUtils.write(new File(file), "\r\n", Charsets.UTF_8, true);
//        FileUtils.write(new File(file), "GA:" + "\r\n", Charsets.UTF_8, true);
//        FileUtils.write(new File(file), Fitness.returnStr(), Charsets.UTF_8, true);


        Fitness.reset();
    }
}
