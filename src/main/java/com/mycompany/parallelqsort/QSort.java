/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parallelqsort;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Artur
 */
public class QSort {
    
    private final ForkJoinPool forkJoinPool;
    private boolean debug;

    public QSort(int parallel,boolean debug) {
        this.forkJoinPool=new ForkJoinPool(parallel);
        this.debug=debug;
    }
    
    public double[] sort(double[] arr){
        int left=0;
        int right=arr.length-1;
        int index=partition(arr, left, right);
        QSortAction parallelTask1=null;
        QSortAction parallelTask2=null;
        if (right-index>1){            
            parallelTask1=new QSortAction(this,arr, index+1, right);
            forkJoinPool.execute(parallelTask1);
        }
        if (index-left>1){
            parallelTask2=new QSortAction(this,arr, left, index-1);
            forkJoinPool.execute(parallelTask2);
        }
        if (parallelTask1!=null)
            parallelTask1.join();
        if (parallelTask2!=null)
            parallelTask2.join();
        return arr;
    }
    
    private double[] sort(double[] arr,int left,int right){
        int index=partition(arr, left, right);
        QSortAction parallelTask1=null;
        QSortAction parallelTask2=null;
        if (right-index>1){            
            parallelTask1=new QSortAction(this,arr, index+1, right);
            forkJoinPool.execute(parallelTask1);
        }
        if (index-left>1){
            parallelTask2=new QSortAction(this,arr, left, index-1);
            forkJoinPool.execute(parallelTask2);
        }
        if (parallelTask1!=null)
            parallelTask1.join();
        if (parallelTask2!=null)
            parallelTask2.join();
        return arr;
    }
    
    private int partition(double[] arr,int left,int right){
        int baseIndex=findBaseElIndex(arr,left,right);
        double baseEl=arr[baseIndex];
        while (right>left){
            while (arr[left]<=baseEl && right>left)
                left++;
            while (arr[right]>baseEl && right>left)
                right--;
            if (right==baseIndex)
                baseIndex=left;
            else if (left==baseIndex)
                baseIndex=right;
            double exchange=arr[right];
            arr[right]=arr[left];
            arr[left]=exchange;
        }
        while (arr[right]>baseEl)
            right--;
        double exchange=arr[right];
        arr[right]=arr[baseIndex];
        arr[baseIndex]=exchange;
        if (this.isDebug())
            System.out.println(Thread.currentThread().getName()+":"+printArr(arr));
        return right;
    }
    
    private static String printArr(double[] arr){
        String str="[";
        for (double el:arr)
            str=str+el+",";
        str=str.substring(0, str.length()-1)+"]";
        return str;
    }
    
    private static int findBaseElIndex(double[] arr,int start,int finish){
        double head=arr[start];
        double tail=arr[finish];
        int middleIndex=Math.floorDiv(finish-start,2);
        double middle=arr[middleIndex];      
        if ((tail>=head && tail<=middle) || (tail>=middle && tail<=head))
            return finish;
        if ((middle>=head && middle<=tail) || (middle>=tail && middle<=head))
            return middleIndex;
        return start;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    private static class QSortAction extends RecursiveAction{
        
        private final QSort qSort;
        private final double[] arr;
        private final int left;
        private final int right;

        public QSortAction(QSort qSort, double[] arr, int left, int right) {
            this.qSort = qSort;
            this.arr = arr;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            qSort.sort(arr, left, right);
        }
    
    }
    
}
