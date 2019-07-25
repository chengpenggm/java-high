package org.cp;

/**
 * create by CP on 2019/7/23 0023.
 */
public class ThreadYield {

    public static Integer i = 10;

    public static void main(String[] args) {
        Thread t2 = new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("T2...");
                }
            }
        };
        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ": "+i);
                    i--;
                    if (i<5) {
                        try {
                            t2.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(t2.isAlive());
                    if (i<0) {
                        break;
                    }
                }
            }
        };
        t1.setName("T1 ");
        t1.start();


//        t2.start();
    }
}
