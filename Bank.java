import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private static Lock lock = new ReentrantLock();
    private static int count = 100;

    static class Counter extends Thread {
        private String people;
        private int num;

        public Counter(String name, int num) {
            people = name;
            this.num = num;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                if (count >= num) {
                    count -= num;
                    System.out.println(people + " took away $" + num + " from counter and now Leaving " + count);
                }
                else    System.out.println("Don't have enough money!!! Leaving " + count);
            } finally {
                lock.unlock();
            }
        }
    }

    static class ATM extends Thread {
        private String people;
        private int num;

        public ATM(String name, int num) {
            people = name;
            this.num = num;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                if (count >= num) {
                    count -= num;
                    System.out.println(people + " took away $" + num + " from ATM and now Leaving " + count);
                }
                else    System.out.println("Don't have enough money!!! Leaving " + count);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 1; i < 20; i++) {
            executor.execute(new Counter("ZhangSan", i));
            executor.execute(new ATM("LiSi", 2*i));
        }
        executor.shutdown();
    }
}
