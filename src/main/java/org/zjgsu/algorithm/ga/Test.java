package org.zjgsu.algorithm.ga;

import com.mathworks.toolbox.javabuilder.MWException;
import fmincon.Class1;

/**
 * Created by wuhanqing on 2017/3/26.
 */
public class Test {

    public static void main(String[] args) throws MWException {
        String f="22*x(10)+30*x(11)";
        Double[] x0 = new Double[]{0d,0d,1d,1d,1d,1d,0d,1d,0d,35d,33d};
        Double[][] a = {{12d,15d,12d,4d,0d,0d,0d,0d,0d,-1.253,0d},{0d,0d,0d,0d,3d,9d,14d,5d,10d,0d,-1.253}};
        double[] b = new double[]{0,0};

        double[][] aeq = {{1,0,0,0,1,0,0,0,0,0,0},{0,1,0,0,0,1,0,0,0,0,0},{0,0,1,0,0,0,1,0,0,0,0},{0,0,0,0,0,0,0,1,0,0,0},{0,0,0,1,0,0,0,0,1,0,0}};
        double[] beq = {1,1,1,1,1};
        double[] lb = {0,0,0,0,0,0,0,0,0,0,0};
        double[] ub = {1,1,1,1,1,1,1,1,1,35,31};
        Class1 fmincon = new Class1();
        Object[] result = fmincon.myfmincon(4, f, x0, a, b, aeq, beq, lb, ub);
        System.out.println();
    }

}
