public class WaysOfCreateThread {

    static class ByInheritance extends Thread {

        public ByInheritance(final String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.printf("running thread : %s; created by extended from Thread class\n", Thread.currentThread().getName());
        }

    }

    static class ByInterface implements Runnable {

        @Override
        public void run() {
            System.out.printf("running thread : %s; created by implementing Runnable interface\n", Thread.currentThread().getName());
        }

    }

    public static void newByInterface() {
        final Thread bit = new Thread(new ByInterface(), "ByInteface0");
        bit.start();
    }

    public static void newByInheritance() {
        final Thread bit = new ByInheritance("ByInheritance0");
        bit.start();
    }

    public static void newByLambda() {
        final Thread blt = new Thread(() -> {
            System.out.printf("running thread : %s; created by using lambda function\n", Thread.currentThread().getName());
        }, "ByLambda0");
        blt.start();
    }

    public static void main(final String... args) {
        System.out.printf("starting main method\n");
        newByInterface();
        newByInheritance();
        newByLambda();
        System.out.printf("ending main method\n");
    }

}
