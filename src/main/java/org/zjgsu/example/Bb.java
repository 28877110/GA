package org.zjgsu.example;


import com.google.common.base.Charsets;
import fminimax.Class1;
import org.apache.commons.io.FileUtils;
import org.springframework.util.SerializationUtils;
import org.zjgsu.algorithm.ga.cross.Cross;
import org.zjgsu.algorithm.ga.fitness.Fitness;
import org.zjgsu.algorithm.ga.method.BranchAndBound;
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
public class Bb
{
    public static void main( String[] args ) throws Exception {

        for (int i = 0; i < 25; i++) {
            String name = "example1";
            int size = 10;
            String file = "result/parameter/bb/" + size + "/" + name;
            FileUtils.write(new File(file), name + "\r\n", Charsets.UTF_8, true);
            FileUtils.write(new File(file), "Parameters: " + (i + 1) + "\r\n", Charsets.UTF_8, true);
            ParameterSelection ps = new ParameterSelection();
            Object[][] talbe = ps.getTable();
            for (int j = 0; j < 10; j++) {

                //分支定界
                byte[] processByte = FileUtils.readFileToByteArray(new File("data/10/" + name));
                Process process = (Process) SerializationUtils.deserialize(processByte);
                BranchAndBound branchAndBound = new BranchAndBound();
                Double num = branchAndBound.execute(process);
                double[] branchAndBoundBestResources = branchAndBound.getBestResources();

                StringBuilder builder = new StringBuilder();
                builder.append("[");
                for (int k = 0; k < branchAndBoundBestResources.length; k++) {
                    builder.append(branchAndBoundBestResources[k]).append(", ");
                }
                builder.delete(builder.length() - 2, builder.length() - 1);
                builder.append("]");
                FileUtils.write(new File(file), "分支定界:" + "\r\n", Charsets.UTF_8, true);
                FileUtils.write(new File(file), String.valueOf(num), Charsets.UTF_8, true);
                FileUtils.write(new File(file), builder.toString() + "\r\n\r\n", Charsets.UTF_8, true);

            }
        }

    }

}
