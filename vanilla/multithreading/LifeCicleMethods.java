import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class LifeCicleMethods {

    public static final byte AVAILABLE_PROCESSORS = (byte) Runtime.getRuntime().availableProcessors();
    public static final int MILLISECONDS_PER_TASK = 1000;

    static class Task implements Runnable {

        @Override
        public void run() {
            final String tn = Thread.currentThread().getName();
            System.out.printf("starting to run the task on thread %s\n", tn);
            try {
                Thread.sleep(MILLISECONDS_PER_TASK);
                System.out.printf("ending of run the task on thread %s\n", tn);
            } catch (InterruptedException ie) {
                System.out.printf("thread %s can't wait %d milliseconds to complete\n", tn, MILLISECONDS_PER_TASK);
            }
        }

    }

    public static void main(final String... args) {
        final String tn = Thread.currentThread().getName();
        System.out.printf("starting %s thread with %d available processors\n", tn, AVAILABLE_PROCESSORS);
        final ExecutorService es = Executors.newFixedThreadPool(1);
        for (int i = 0; i < AVAILABLE_PROCESSORS; i++) {
            es.execute(new Task());
        }
        es.shutdown();//initiate the shutdown
        try {
            es.execute(new Task());//throw an execption case the shutdown signal to the pool was sended
        } catch (RejectedExecutionException ree) {
            System.out.printf("the pool is shutdown (%b); so you can not send more task\n", es.isShutdown());
        }
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());// will return false if there are some task on the queue to be executed; true if the pool is empty
        try {
            es.awaitTermination(AVAILABLE_PROCESSORS, TimeUnit.SECONDS);// main tread wait to continue
        } catch (InterruptedException ie) {
            System.out.printf("unable to await executor service termination\n");
        }
        System.out.printf("is the pool terminated? %b\n", es.isTerminated());
        System.out.printf("ending %s thread\n", tn);
    }

}