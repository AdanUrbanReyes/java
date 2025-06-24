import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;


public class Callable {

    public static final byte AVAILABLE_PROCESSORS = (byte) Runtime.getRuntime().availableProcessors();
    public static final int MILLISECONDS_PER_TASK = 1000;

    static class Task implements java.util.concurrent.Callable<Integer> {

        private int target;

        public Task(int target) {
            this.target = target;
        }

        @Override
        public Integer call() throws Exception {
            final String tn = Thread.currentThread().getName();
            System.out.printf("starting to run the task on thread %s\n", tn);
            int c = 0;
            while (c != target) {
                try {
                    System.out.printf("task on thread %s wating for %d milliseconds\n", tn, MILLISECONDS_PER_TASK);
                    Thread.sleep(MILLISECONDS_PER_TASK);
                } catch (InterruptedException ie) {
                    System.out.printf("thread %s can't wait %d milliseconds before increment the count\n", tn, MILLISECONDS_PER_TASK);
                }
                c++;
            }
            System.out.printf("ending of run the task on thread %s\n", tn);
            return c;
        }

    }

    public static void main(final String... args) {
        final String tn = Thread.currentThread().getName();
        System.out.printf("starting %s thread with %d available processors\n", tn, AVAILABLE_PROCESSORS);
        final ExecutorService es = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS);
        final List<Future<Integer>> fs = new ArrayList<>();
        for (int i = 0; i < AVAILABLE_PROCESSORS; i++) {
            fs.add(es.submit(new Task(i)));
        }
        Integer c;
        for (int i = 0; i < AVAILABLE_PROCESSORS; i++) {
            try {
                c = fs.get(i).get(AVAILABLE_PROCESSORS, TimeUnit.SECONDS);
                System.out.printf("task %d return %d\n", i, c);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                System.out.printf("unable to get the count of %d task\n", i);
            }
        }
        es.shutdown();
        System.out.printf("ending %s thread\n", tn);
    }

}