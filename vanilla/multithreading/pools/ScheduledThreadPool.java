import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;

public class ScheduledThreadPool {

    public static final int MILLISECONDS_PER_TASK = 7000;

    static class Task implements Runnable {

        private String type;

        public Task(String type) {
            this.type = type;
        }

        @Override
        public void run() {
            final String tn = Thread.currentThread().getName();
            System.out.printf("starting to run the task on %s %s %s\n", type, tn, LocalDateTime.now());
            try {
                Thread.sleep(MILLISECONDS_PER_TASK);
                System.out.printf("ending of run the task on %s %s %s\n", type, tn, LocalDateTime.now());
            } catch (InterruptedException ie) {
                System.out.printf("%s %s can't wait %d milliseconds before increment the count %s\n", type, tn, MILLISECONDS_PER_TASK, LocalDateTime.now());
            }
        }

    }

    /**
     * Create a schedule pool threads will be executed according to the schedule setted
     *
     * @param args
     */
    public static void main(String... args) {
        final String tn = Thread.currentThread().getName();
        System.out.printf("starting %s thread\n", tn);
        final ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
        ses.schedule(new Task("single"), 3, TimeUnit.SECONDS);//this task will be run after 3 seconds of delay
        ses.scheduleAtFixedRate(new Task("fixedRate"), 15, 9, TimeUnit.SECONDS);// task to run repeatedly every 9 seconds; the first task will have 15 seconds of delay
        ses.scheduleWithFixedDelay(new Task("fixedDelay"), 15, 5, TimeUnit.SECONDS); //task to run repeatedly every 5 seconds AFTER previous task compleate; the first task will have 15 seconds of delay
        System.out.printf("ending %s thread\n", tn);
    }

}