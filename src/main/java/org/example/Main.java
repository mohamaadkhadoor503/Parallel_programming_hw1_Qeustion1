package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;



public class Main {

// serial function
    public static int countOccurrencesSerial(int[] arr, int num) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                count++;
            }
        }
        return count;
    }


    // parallel function using fork/join

    public static class Counter extends RecursiveTask<Integer> {
        private int[] arr;
        private int num;
        private int lo, hi;

        public Counter(int[] arr, int num, int lo, int hi) {
            this.arr = arr;
            this.num = num;
            this.lo = lo;
            this.hi = hi;
        }

        protected Integer compute() {
            if (hi - lo <= 100000) {
                int count = 0;
                for (int i = lo; i < hi; i++) {
                    if (arr[i] == num) {
                        count++;
                    }
                }
                return count;
            } else {
                int mid = lo + (hi - lo) / 2;
                Counter left = new Counter(arr, num, lo, mid);
                Counter right = new Counter(arr, num, mid, hi);
                left.fork();
                int rightResult = right.compute();
                int leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }

    public static int countOccurrencesParallel(int[] arr, int num) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new Counter(arr, num, 0, arr.length));
    }



    //stream with serial
    public static long countOccurrencesStreamSerial(int[] arr, int num) {
        long start = System.currentTimeMillis();
        long count = Arrays.stream(arr).filter(n -> n == num).count();
        long end = System.currentTimeMillis();
        System.out.println("Serial Stream execution time: " + (end - start) + " ms");
        return count;
    }

    //stream with Parallel

    public static long countOccurrencesStreamParallel(int[] arr, int num) {
        long start = System.currentTimeMillis();
        long count = Arrays.stream(arr).parallel().filter(n -> n == num).count();
        long end = System.currentTimeMillis();
        System.out.println("Parallel Stream execution time: " + (end - start) + " ms");
        return count;
    }




    public static void main(String[] args) {
        int[] arr = new int[1000000000];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(100000) + 1;
        }
        Scanner myObj = new Scanner(System.in);  // Create a Scanner input
        System.out.println("Enter number to find the number of occurrences :\n");
        int num = myObj.nextInt();

        //test 1
        long start = System.currentTimeMillis();
        int count = countOccurrencesSerial(arr, num);
        long end = System.currentTimeMillis();
        System.out.println("Number of occurrences of " + num + " is " + count);
        System.out.println("Serial execution time: " + (end - start) + " ms");


        //test 2
         start = System.currentTimeMillis();
         count = countOccurrencesParallel(arr, num);
         end = System.currentTimeMillis();
        System.out.println("Number of occurrences of " + num + " is " + count);
        System.out.println("Parallel execution time: " + (end - start) + " ms");

        //test 3-1

        long count1 = countOccurrencesStreamSerial(arr, num);
        System.out.println("Number of occurrences of " + num + " is " + count1);

        //test 3-2
        count1 = countOccurrencesStreamParallel(arr, num);
        System.out.println("Number of occurrences of " + num + " is " + count1);
    }

}














