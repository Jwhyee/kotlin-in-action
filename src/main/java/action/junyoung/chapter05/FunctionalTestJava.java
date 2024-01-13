package action.junyoung.chapter05;

public class FunctionalTestJava {
    public static void postponeComputation(int delay, Runnable computation) throws InterruptedException {
        Thread.sleep(delay);
        computation.run();
    }
}
