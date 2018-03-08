package com.trasher;

/**
 * Created by RoyChan on 2018/2/24.
 */

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class TreadLocalTest {

 static ThreadLocal<HashMap> threadLocal = new ThreadLocal<HashMap>(){

 @Override

 protected HashMap initialValue() {

 System.out.println(Thread.currentThread().getName()+"initialValue");

 return new HashMap();

 }

 };


    @Test
    public void main() throws InterruptedException {

        Thread[] runs = new Thread[15];

        T1 t = new T1(1);

        for (int i = 0; i < runs.length; i++) {

            runs[i] = new Thread(t);

        }

        for (int i = 0; i < runs.length; i++) {
            runs[i].start();
        }


        for (int i = 0; i < runs.length; i++) {
            runs[i].join();
        }

        Thread.sleep(10000);

    }





    public static class T1 implements Runnable {

        private final static Map map = new HashMap();

        int id;

        public T1(int id) {

            this.id = id;

        }

        public void run() {

// Map map = threadLocal.get();

            for (int i = 0; i < 20; i++) {

                map.put(i, i + id * 100);

                try {

                    Thread.sleep(100);

                } catch (Exception ex) {

                }

            }

            System.out.println(Thread.currentThread().getName()

                    + "# map.size()=" + map.size() + " # " + map);

        }

    }

}

 

 
