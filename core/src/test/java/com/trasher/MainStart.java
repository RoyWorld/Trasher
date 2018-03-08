package com.trasher;

import com.trasher.algorithm.AOVNetwork;
import com.trasher.thread.AppStart;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by RoyChan on 2018/2/7.
 */
public class MainStart {

    private AOVNetwork adjTableToAOVNetwork(Integer[][] tableModel) {
        AOVNetwork aovNetwork = new AOVNetwork(tableModel.length);

        for (int i = 0; i < tableModel.length; i++) {
            for (int j = 0; j < tableModel[i].length; j++) {
                if (tableModel[i][j] == 1) {
                    aovNetwork.addEdge(i, j);
                }
            }
        }

        return aovNetwork;
    }

    @Test
    public void testDB2() throws SQLException {
        AppStart appStart = new AppStart();
        appStart.run();
    }

    @Test
    public void test() throws SQLException, InterruptedException {

        Thread thread = new Thread(() -> {
            while (true){
                System.out.println("lllll");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        thread.join();
    }

    private final Logger logger  = LoggerFactory.getLogger(MainStart.class);

    @Test
    public void testLog(){
        for (int i = 0; i < 1000; i++) {
            logger.debug("------ Starting Ant------");
        }
    }

    Integer[] a = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99};

    Integer[] b = new Integer[]{45,71,48,88,26,76,52,85,77,40,10,44,96,43,39,78,18,69,33,28,67,0,63,82,64,25,7,62,20,47,17,49,65,30,36,70,29,55,23,4,87,37,5,31,68,8,80,93,95,54,66,15,6,12,3,56,11,74,73,60,86,16,83,94,84,46,34,91,53,58,14,81,92,51,27,19,90,79,61,22,50,97,72,13,98,38,21,9,32,89,41,42,75,1,57,2,99,24,35,59};


    @Test
    public void test1(){
//        qSort(b, 0, b.length-1);
//        iSort(b);
//        iSort2(b, 1);
        mSort(b, 0, b.length-1);
        System.out.println(Arrays.toString(b));
    }


    public void qSort(Integer[] a, int lo, int hi){
        if (lo < hi){
            int p = partition(a, lo, hi);
            qSort(a, lo, p - 1);
            qSort(a, p + 1, hi);
        }
    }

    private int partition(Integer[] a, int lo, int hi){
        int pivot = a[hi];
        int i = lo - 1;
        for (int j = lo; j <= hi - 1; j++) {
            if (a[j] < pivot){
                i++;
                a[i] = swap(a[i], a[j]);
            }
        }
        a[i+1] = swap(a[hi], a[i+1]);
        return i + 1;
    }


    /**
     *
     * @param a
     */
    public void iSort(Integer[] a){
        for (int i = 2; i < a.length; i++) {
            for (int j = i; j > 0 && a[j] < a[j-1]; j--) {
                a[j-1] = swap(a[j], a[j] = a[j-1]);
            }
        }
    }


    public void iSort2(Integer[] a, int index){
        index++;
        for (int j = index; j > 0 && a[j] < a[j-1]; j--) {
            a[j-1] = swap(a[j], a[j] = a[j-1]);
        }
        if (index == a.length-1){
            return;
        }else {
            iSort2(a, index);
        }
    }

    public void mSort(Integer[] a, int start, int end){
        int m = a.length / 2;

        mSort(a, start, m);
        mSort(a, m+1, end);

        Integer b[] = Arrays.copyOf(a, m);
        int i = 1, j = m + 1, k = 1;
        while (i <= m && j < a.length){
            a[k++] = (a[j] < b[i]) ? a[j++] : b[i++];
        }
        while (i <= m)
            a[k++] = b[i++];
    }

    @Test
    public void testt2(){
        Integer[] a = new Integer[]{1, 2};
        a[1] = swap(a[0], a[0] = a[1]);
        System.out.println(Arrays.toString(a));
    }

    private int swap(int a, int b){
        return a;
    }

    @Test
    public void testLock() throws InterruptedException {
        int count = 20;
        Thread[] threads = new Thread[count];
        TicketLock ticketLock = new TicketLock();
        for (int i = 0; i < count; i++) {
            final String name = "name-" + i;
            threads[i] = new Thread(() -> {
                try {
                    int myticket = ticketLock.lock(name);
                    System.out.println(String.format("Thread: %s get Lock, ticketNumber: %s", name, myticket));
                    Thread.sleep(100);
                    ticketLock.unlock();
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < count; i++) {
            threads[i].join();
        }
    }

}

class TicketLock {
    private AtomicInteger serviceNum = new AtomicInteger();
    private AtomicInteger ticketNum  = new AtomicInteger();
    private static final ThreadLocal<Integer> LOCAL = new ThreadLocal<Integer>();

    public int lock(String name) {
        int myticket = ticketNum.getAndIncrement();
        LOCAL.set(myticket);
        System.out.println(String.format("Thread: %s wait for Lock, ticketNumber: %s", name, myticket));
        while (myticket != serviceNum.get()) {
        }
        return myticket;
    }

    public void unlock() {
        int myticket = LOCAL.get();
        serviceNum.compareAndSet(myticket, myticket + 1);
    }
}
