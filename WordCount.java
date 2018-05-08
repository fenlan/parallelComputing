import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeoutException;

public class WordCount {

    private static class Counter extends RecursiveTask<Integer> {

        private int threshold = 4;

        private String[] array;

        private int index0 = 0;
        private int index1 = 0;

        public Counter(String[] array, int index0, int index1) {
            this.array = array;
            this.index0 = index0;
            this.index1 = index1;
        }

        @Override
        protected Integer compute() {
            int num = 0;
            if ((index1 - index0) <= threshold) {
                for (int i = index0; i <= index1; i++) {
                    if (array[i].equals("book")) {
                        num++;
                    }
                }
            } else {
                int mid = index0 + (index1 - index0) / 2;
                Counter lMax = new Counter(array, index0, mid);
                Counter rMax = new Counter(array, mid + 1, index1);

                lMax.fork();
                rMax.fork();

                int lm = lMax.join();
                int rm = rMax.join();

                num = lm + rm;

            }

            return num;
        }
    }

    public static void main(String ... args) throws ExecutionException, InterruptedException, TimeoutException {

        String file1 = "and,with,we,me,university,with,book,computer,country,book";
        String file2 = "bag,boy,book,school,teacher,student,book,book";
        ForkJoinPool pool = new ForkJoinPool();

        String[] array1 = file1.split(",");
        String[] array2 = file2.split(",");

        Counter task1 = new Counter(array1, 0, array1.length - 1);
        Counter task2 = new Counter(array2, 0, array2.length - 1);

        Future<Integer> future1 = pool.submit(task1);
        Future<Integer> future2 = pool.submit(task2);

        System.out.println("list1 : " + future1.get() + " list2 : " + future2.get());
        System.out.println("WordCount : " + (future1.get() + future2.get()));
    }

}