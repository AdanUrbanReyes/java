import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiscardOldestPolicy {

    public static final byte AVAILABLE_PROCESSORS = (byte) Runtime.getRuntime().availableProcessors();
    public static final int MILLISECONDS_PER_TASK = 3000;

    static class Task implements Runnable {

        @Override
        public void run() {
            final String tn = Thread.currentThread().getName();
            System.out.printf("starting to run the task on thread %s\n", tn);
            try {
                Thread.sleep(MILLISECONDS_PER_TASK);
                System.out.printf("ending of run the task on thread %s\n", tn);
            } catch (InterruptedException ie) {
                System.out.printf("thread %s can't wait %d milliseconds before increment the count\n", tn, MILLISECONDS_PER_TASK);
            }
        }

    }

    /**
     * Create a Pool using a queue with just one slot, so if we send more than one task
     * at the same time we will have rejections in this case that rejection will be
     * handler by DiscardOldesPolicy class which if the queue is full and
     * there is not thread to take one task, it will remove the oldest task
     * on the queue and set the received
     *
     * @param args
     */
    public static void main(final String... args) {
        final String tn = Thread.currentThread().getName();
        System.out.printf("starting %s thread with %d available processors\n", tn, AVAILABLE_PROCESSORS);
        final ExecutorService es = new ThreadPoolExecutor(1, 1, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());
        for (int i = 0; i < AVAILABLE_PROCESSORS; i++) {
            es.execute(new Task());
        }
        es.shutdown();
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        final int ats = MILLISECONDS_PER_TASK / 1000 * AVAILABLE_PROCESSORS;
        try {
            es.awaitTermination(ats, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            System.out.printf("unable to await %d seconds for executor service termination\n", ats);
        }
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        System.out.printf("ending %s thread\n", tn);
    }

}
