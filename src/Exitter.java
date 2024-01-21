/* Do not modify (will be rewritten by the checker). */

/**
 * Class used for forcefully exiting the program after a given time.
 */
public class Exitter extends Thread {
    private final long sleepTime;

    public Exitter(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void run() {
        try {
            Thread.sleep(1000L * sleepTime);
            System.out.println("Forcefully exiting program");
            System.out.println(Main.score.get() + "/120");
            System.exit(1);
        } catch (InterruptedException ignored) {
        }
    }
}
