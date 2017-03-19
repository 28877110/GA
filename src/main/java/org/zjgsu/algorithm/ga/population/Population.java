package org.zjgsu.algorithm.ga.population;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.zjgsu.algorithm.ga.model.Process;

import java.util.List;

import static org.zjgsu.algorithm.ga.utils.Parameter.POPULATION_SIZE;

/**
 * Created by wuhanqing on 2017/3/19.
 */
@Getter
@Setter
public class Population {

    private List<Chromsome> chromsomeList = Lists.newArrayList();

    private Chromsome best;

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

}
