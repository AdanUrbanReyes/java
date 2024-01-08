/*
    javac ShutdownHook.java
    jar cfe ShutdownHook.jar ShutdownHook *.class
    java -jar ShutdownHook.jar
 */
public class ShutdownHook {
    
    public static void main(String[] args) {
        ShutDownTask shutDownTask = new ShutDownTask();
        Runtime.getRuntime().addShutdownHook(shutDownTask);
        System.out.println("Starting to lose time...");
        while (true) {
            try{
                System.out.println("Lossing the time ...");
                Thread.sleep(1000);
            }catch(InterruptedException ie){
                System.out.println(String.format("Error while lossing the time; %s", ie));
            }
        }
    }

    private static class ShutDownTask extends Thread {
        @Override
        public void run() {
            Integer ms = 7000;
            try{
                ms = Integer.parseInt(System.getenv("TIME_TO_CLEAN_SECONDS")) * 1000;
            }catch(NumberFormatException nfe){
                System.out.println(String.format("Cant read parse the TIME_TO_CLEAN_SECONDS enviroment variable to Integer; %s", System.getenv("TIME_TO_CLEAN_SECONDS")));
            }
            System.out.println(String.format("Taking %d ms to clean bullshit", ms));
            try{
                Thread.sleep(ms);
            }catch(InterruptedException ie){
                System.out.println(String.format("Error while taking time to clean bullshit; %s", ie));
            }finally{
                System.out.println("Clean bullshit is done");
            }
        }
    }
}