package org.zjgsu;

import org.zjgsu.algorithm.ga.cross.Cross;
import org.zjgsu.algorithm.ga.fitness.Fitness;
import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.population.Population;
import org.zjgsu.algorithm.ga.utils.Parameter;
import org.zjgsu.algorithm.ga.variation.Variation;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Process process = new Process();
        process.generate();

        Population population = Population.init(process);

        Fitness.compute(population);

        for (int i = 0; i < Parameter.MAX_ITERATIONS; i++) {
            Fitness.compute(population);

            Cross.cross(population);

            Variation.variation(population);

            //精英保留
        }


        System.out.println( "Hello World!" );
    }
}
