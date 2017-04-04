package org.zjgsu.algorithm.ga.population;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Process;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.zjgsu.algorithm.ga.utils.Parameter.POPULATION_SIZE;

/**
 * Created by wuhanqing on 2017/3/19.
 */
@Getter
@Setter
public class Population {

    private List<Chromsome> chromsomeList = Lists.newArrayList();

    private Process process;

    public static Population init(Process process) {
        Population population = new Population(POPULATION_SIZE, process);
        population.setProcess(process);
        return population;
    }

    public Population(Integer size, Process process) {
        for (int i = 0; i < size; i++) {
            chromsomeList.add(new Chromsome(process));
        }
    }

    public void sort() {
        Collections.sort(chromsomeList, new Comparator<Chromsome>() {
            @Override
            public int compare(Chromsome o1, Chromsome o2) {
                if (o1.getFitness() != null && o2.getFitness() == null) {
                    return 1;
                }
                if (o1.getFitness() == null && o2.getFitness() != null) {
                    return -1;
                }
                if (o1.getFitness() == null && o2.getFitness() == null) {
                    return 0;
                }
                if (o1.getFitness() > o2.getFitness()) {
                    return 1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

}
