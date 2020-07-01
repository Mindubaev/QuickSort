/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parallelqsort;

/**
 *
 * @author Artur
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[] arr=generateArr(1000, -1000, 1000);
        QSort qSort=new QSort(4, true);
        long start=System.nanoTime();
        qSort.sort(arr);
        long finish=System.nanoTime();
        System.out.println("Execution time:"+(finish-start)+" nanoseconds");
    }
    
    public static double[] generateArr(int amount,double min,double max){
        double delta=max-min;
        double[] arr=new double[amount];
        for (int i=0;i<amount;i++){
            arr[i]=min+Math.random()*delta;
        }
        return arr;
    }
    
}
