package org.zjgsu;

import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.population.Population;


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




        System.out.println( "Hello World!" );
    }
}
