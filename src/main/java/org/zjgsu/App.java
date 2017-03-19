package org.zjgsu;

import org.zjgsu.algorithm.ga.model.Process;
import org.zjgsu.algorithm.ga.model.logic.Logic;

import java.util.List;

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
        System.out.println( "Hello World!" );
    }
}
