import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CachedThreadPool {

    public static final byte AVAILABLE_PROCESSORS = (byte) Runtime.getRuntime().availableProcessors();
    public static final int MILLISECONDS_PER_TASK = 7000;

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
     * If there is no thread that could take the task, 
     * a new thread is created and add it to the pool.
     * If thread is IDLE (not busy, or no task to execute) for 60 seconds,
     * then terminate the thread and remove it from pool.
     *
     * @param args
     */
    public static void main(String... args) {
        final String tn = Thread.currentThread().getName();
        System.out.printf("starting %s thread with %d available processors\n", tn, AVAILABLE_PROCESSORS);
        final ExecutorService es = Executors.newCachedThreadPool();
        for (byte t = 0; t < AVAILABLE_PROCESSORS; t++) {
            es.execute(new Task());
        }
        es.shutdown();
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        final int ats = MILLISECONDS_PER_TASK;
        try {
            es.awaitTermination(ats, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            System.out.printf("unable to await %d seconds for executor service termination\n", ats);
        }
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        System.out.printf("ending %s thread\n", tn);
    }

}