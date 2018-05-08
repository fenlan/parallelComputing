import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeoutException;

public class SumOfArr {

    private static class ArrSum extends RecursiveTask<Integer> {

        private int threshold = 4;

        private char[] array;

        private int index0 = 0;
        private int index1 = 0;

        public ArrSum(char[] array, int index0, int index1) {
            this.array = array;
            this.index0 = index0;
            this.index1 = index1;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if ((index1 - index0) <= threshold) {

                for (int i = index0;i <= index1; i ++) {
                    if (Character.isDigit(array[i])) {
                        sum += Character.getNumericValue(array[i]);
                    }
                }

            } else {
                int mid = index0 + (index1 - index0) / 2;
                ArrSum lMax = new ArrSum(array, index0, mid);
                ArrSum rMax = new ArrSum(array, mid + 1, index1);

                lMax.fork();
                rMax.fork();

                int lm = lMax.join();
                int rm = rMax.join();

                sum = lm + rm;

            }

            return sum;
        }
    }

    public static void main(String ... args) throws ExecutionException, InterruptedException, TimeoutException {

        ForkJoinPool pool = new ForkJoinPool();

        char[] array1 = {'*', '%', '3', '#', '6', '~', '!', '2'};
        char[] array2 = {'&', '￥','@', '1', '4', ':', '2', '1'};

        ArrSum task1 = new ArrSum(array1, 0, array1.length - 1);
        ArrSum task2 = new ArrSum(array2, 0, array2.length - 1);

        Future<Integer> future1 = pool.submit(task1);
        Future<Integer> future2 = pool.submit(task2);

        System.out.println("list1 : " + future1.get() + " list2 : " + future2.get());
        System.out.println(future1.get() > future2.get() ? "list1" : "list2");
    }

}