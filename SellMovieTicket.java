import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BoxOffice implements Runnable {
    private static int tickets = 30;
    private int boxNum;

    public BoxOffice(int boxNum) {
        this.boxNum = boxNum;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (BoxOffice.class) {
                if (tickets > 0) {
                    tickets--;
                    System.out.println("Box " + boxNum + " Sold one ticket!!! " + "Leaving " + tickets + " tickets");
                }
                else            break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class SellMovieTicket {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 1; i < 5; i++) {
            BoxOffice boxOffice = new BoxOffice(i);
            executor.execute(boxOffice);
        }
        executor.shutdown();
    }
}
