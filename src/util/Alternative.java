package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Alternative {
    private int id;
    private int numOfMsrs;
    private int currentIndex = 0;
    public double[] msrs;
    private double[] alfaDeviations;
    private double mean;
    private double sumOfMsr;
    //private double alfaDeviation;

    public Alternative() {
    }

    public Alternative(int id, double[] msrs, int numOfMsrs) {
        this.id = id;
        this.msrs = msrs;
        this.numOfMsrs = numOfMsrs;
        sumOfMsr = 0;
        for (int i = 0; i < this.numOfMsrs; i++) {
            sumOfMsr += msrs[i];
        }
        mean = sumOfMsr / this.numOfMsrs;
        //System.out.println("model.Alternative" + id + " " + mean);
    }

    /*public void addMsrAt(int index, double value) {
        msrs[index] = value;
        currentIndex++;
    }*/

    public int getCurrentIndex() {
        return currentIndex;
    }

    public double getMean() {
        return mean;
    }

    public double getSumOfMsr() {
        return sumOfMsr;
    }
}
