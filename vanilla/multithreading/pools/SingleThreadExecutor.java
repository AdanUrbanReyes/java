import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleThreadExecutor {

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
     * Create a blocking queue where all sended task will be stored, 
     * then one single thread on pool will pull and process one by one until it finish.
     * 
     * @param args
     */
    public static void main(final String... args) {
        final String tn = Thread.currentThread().getName();
        System.out.printf("starting %s thread with %d available processors\n", tn, AVAILABLE_PROCESSORS);
        final ExecutorService es = Executors.newSingleThreadExecutor();
        for (byte t = 0; t < AVAILABLE_PROCESSORS; t++) {
            es.execute(new Task());
        }
        es.shutdown();
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        final int ats = MILLISECONDS_PER_TASK * AVAILABLE_PROCESSORS;
        try {
            es.awaitTermination(ats, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            System.out.printf("unable to await %d seconds for executor service termination\n", ats);
        }
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        System.out.printf("ending %s thread\n", tn);
    }

}